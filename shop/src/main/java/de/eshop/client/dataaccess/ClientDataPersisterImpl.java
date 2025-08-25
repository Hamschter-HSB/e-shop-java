package de.eshop.client.dataaccess;

import de.eshop.client.connection.ClientConnectionToServer;
import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.dataaccess.DataPersister;
import de.eshop.shared.dataaccess.StockChangeDAO;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.Customer;
import de.eshop.shared.domain.StaffMember;
import de.eshop.shared.domain.User;
import de.eshop.shared.domain.events.StockChange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class ClientDataPersisterImpl implements DataPersister {

    private static final Logger logger = Logger.getLogger(ClientDataPersisterImpl.class.getName());

    private final DAO<BulkArticle> bulkArticleDAO;
    private final DAO<StaffMember> staffDAO;
    private final DAO<Customer> customerDAO;
    private final StockChangeDAO stockChangeDAO;

    private Customer customer;

    public ClientDataPersisterImpl(ClientConnectionToServer clientConnectionToServer) {

        bulkArticleDAO = new BulkArticleDAOImpl(clientConnectionToServer);
        staffDAO = new StaffMembersDAOImpl(clientConnectionToServer);
        customerDAO = new CustomerDAOImpl(clientConnectionToServer);
        stockChangeDAO = new StockChangeDAOImpl(clientConnectionToServer);
    }

    /* (Bulk)-Article */

    public synchronized void createBulkArticle(BulkArticle bulkArticle) {
        try {
            bulkArticleDAO.create(bulkArticle);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public synchronized BulkArticle readBulkArticle(int id) {
        try {
            return bulkArticleDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public synchronized List<BulkArticle> readAllBulkArticles() {
        try {
            return bulkArticleDAO.readAll();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public synchronized void updateBulkArticle(BulkArticle bulkArticle) {
        try {
            bulkArticleDAO.update(bulkArticle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void deleteBulkArticle(int bulkArticle) {
        try {
            bulkArticleDAO.delete(bulkArticle);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    /* StaffMember */
    public synchronized void createStaffMember(StaffMember staffMember) {
        try {
            staffDAO.create(staffMember);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public synchronized StaffMember readStaffMember(int id) {
        try {
            return staffDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public synchronized void createCustomer(Customer customer) {
        try {
            customerDAO.create(customer);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public synchronized Customer readCustomer(int id) {
        try {
            return customerDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public synchronized void createStockChange(StockChange stockChange) {
        try {
            stockChangeDAO.create(stockChange);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public synchronized List<StockChange> readAllStockChanges() {
        try {
            return stockChangeDAO.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized int sendReadLastCreatedBulkArticleIDToClient() {
        return stockChangeDAO.readLastCreatedBulkArticleID();
    }

    public synchronized Customer getCustomer() {
        return customer;
    }

    public synchronized void setCurrentCustomer(int customerID) {
        customer = readCustomer(customerID);
    }

    public synchronized List<Customer> readAllCustomers() {
        try {
            return customerDAO.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<StaffMember> readAllStaffMembers() {
        try {
            return staffDAO.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized <T extends User> T findUserByCredentials(String userName, String password, Class<T> clazz) {

        List<T> allUsers = new ArrayList<>();

        if (!(clazz.equals(Customer.class) || clazz.equals(StaffMember.class))) {
            throw new IllegalArgumentException("Unknown class " + clazz.getName() + ". Not a sub type of class " + User.class.getName());
        }

        if (clazz.equals(Customer.class)) {
            allUsers = (List<T>) readAllCustomers();
        }

        if (clazz.equals(StaffMember.class)) {
            allUsers = (List<T>) readAllStaffMembers();
        }

        T userTryingToLogin = null;

        try {
            userTryingToLogin = allUsers.stream()
                    .filter(u -> u.getName().equals(userName) && u.getPassword().equals(password))
                    .toList().getFirst();
        } catch (NoSuchElementException ignored) {
        }

        return userTryingToLogin;
    }
}
