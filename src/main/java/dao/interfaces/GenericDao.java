package dao.interfaces;

import java.util.List;

public interface GenericDao<T, ID> {

    T create(T t) throws Exception;
    T read(ID id) throws Exception;
    List<T> readAll() throws Exception;
    T update(T t) throws Exception;
    void delete(ID id) throws Exception;
}