package db;

import javax.persistence.*;
import javax.transaction.Transaction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityManagerHelper {
    private static EntityManagerFactory emf;
    private static ThreadLocal<EntityManager> threadLocal;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("db");
            threadLocal = new ThreadLocal<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static EntityManagerFactory getEntityManagerFactory() {
        if(emf == null || !emf.isOpen())
            emf = Persistence.createEntityManagerFactory("db");
        return emf;
    }

    private static ThreadLocal<EntityManager> threadLocal() {
        if(threadLocal == null)
            threadLocal = new ThreadLocal<>();
        return threadLocal;
    }

    public static EntityManager getEntityManager() {
        EntityManager manager = threadLocal().get();
        if (manager == null || !manager.isOpen()) {
            manager = getEntityManagerFactory().createEntityManager();
            threadLocal().set(manager);
        }
        return manager;
    }

    public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        if(em != null) {
            threadLocal.set(null);
            em.close();
        }
    }

    public static void closeEntityManagerFactory() {
        emf.close();
    }

    public static EntityTransaction getTransaction() {
        return EntityManagerHelper.getEntityManager().getTransaction();
    }

    public static void beginTransaction() {
        EntityTransaction transaction = getTransaction();
        if(!transaction.isActive()) transaction.begin();
    }

    public static void commit() {
        EntityTransaction transaction = getTransaction();
        if(transaction.isActive()) transaction.commit();
    }

    public static void rollback() {
        EntityTransaction transaction = getTransaction();
        if(transaction.isActive()) transaction.rollback();
    }

    public static void persist(Object unObjeto) {
        getEntityManager().persist(unObjeto);
    }

    public static void merge(Object unObjeto){
        EntityTransaction transaction = getEntityManager().getTransaction();
        if(transaction.isActive()){
            merge(unObjeto);
        }
    }

    public static void remove(Object unObjeto){
        getEntityManager().remove(unObjeto);
    }

    public static void withTransaction(Consumer<EntityManager> action) {
        try {
            beginTransaction();
            action.accept(getEntityManager());
            commit();
            closeEntityManager();
            closeEntityManagerFactory();
        } catch(Throwable e) {
            rollback();
            throw e;
        }
    }
}
