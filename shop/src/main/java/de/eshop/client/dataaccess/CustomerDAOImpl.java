package de.eshop.client.dataaccess;

import de.eshop.client.connection.ClientConnectionToServer;
import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.domain.Customer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class CustomerDAOImpl implements DAO<Customer> {

    private static final Logger logger = Logger.getLogger(CustomerDAOImpl.class.getName());

    private final ClientConnectionToServer clientConnectionToServer;

    public CustomerDAOImpl(ClientConnectionToServer clientConnectionToServer) {
        this.clientConnectionToServer = clientConnectionToServer;
    }

    @Override
    public void create(Customer type) throws IOException {
        clientConnectionToServer.createCustomer(type);
    }

    @Override
    public Customer read(int id) throws IOException {
        return clientConnectionToServer.readCustomerByID(id);
    }

    @Override
    public List<Customer> readAll() throws IOException {
        return clientConnectionToServer.readAllCustomers();
    }

    @Override
    public void update(Customer type) throws IOException {
        // Not implemented yet
    }

    @Override
    public void delete(int id) throws IOException {
        clientConnectionToServer.deleteCustomer(id);
    }
}
