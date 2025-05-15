package net.eshop.dataccess;

import java.io.IOException;

/**
 * This interface is used for CRUD-Operations (create, read, update and delete)
 */
interface DAO<T> {

    void create(T type) throws IOException;

    T read(int id);

    void update(T type);

    void delete(int id);
}