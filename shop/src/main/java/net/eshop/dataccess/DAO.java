package net.eshop.dataccess;

import java.io.IOException;

/**
 * This interface is used for CRUD-Operations (create, read, update and delete)
 */
interface DAO<T> {

    void create(T type) throws IOException;

    void read(int id);

    void update(int id);

    void delete(int id);
}
