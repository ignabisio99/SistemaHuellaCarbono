package domain.repositories;

import domain.repositories.daos.DAO;
import lombok.Setter;

import java.util.List;

public class Repositorio<T> {
    @Setter
    protected DAO<T> dao;

    public Repositorio(DAO<T> dao) {
        this.dao = dao;
    }

    public void agregar(T unObjeto) {
        dao.agregar(unObjeto);
    }

    public void modificar(T unObjeto) {
        dao.modificar(unObjeto);
    }

    public void eliminar(T unObjeto) {
        dao.eliminar(unObjeto);
    }

    public T buscar(int id) {
        return dao.buscar(id);
    }

    public List<T> buscarTodos() {
        return dao.buscarTodos();
    }
}
