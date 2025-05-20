package net.eshop.dataccess;

import java.io.IOException;
import java.util.List;

/**
 * This interface is used for CRUD-Operations (create, read, update and delete)
 */
interface DAO<T> {

    void create(T type) throws IOException;

    T read(int id) throws IOException;

    List<T> readAll() throws IOException;

    void update(T type) throws IOException;

    void delete(int id) throws IOException;
}