package net.eshop.dataccess;

/**
 * This interface is used for CRUD-Operations (create, read, update and delete)
 */
interface DAO<T> {

    void create(T type);

    void read(int id);

    void update(int id);

    void delete(int id);
}
