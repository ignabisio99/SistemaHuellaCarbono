package domain.repositories.daos;

import java.util.List;

public interface DAO<T> {
    void agregar(T unObjeto);
    void modificar(T unObjeto);
    void eliminar(T unObjeto);
    List<T> buscarTodos();
    T buscar(int id);
}
