package de.eshop.server.dataaccess;

import de.eshop.server.connection.ServerCallbackConnection;
import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.dataaccess.DataPersister;
import de.eshop.shared.dataaccess.StockChangeDAO;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.Customer;
import de.eshop.shared.domain.StaffMember;
import de.eshop.shared.domain.events.StockChange;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ServerDataPersisterImpl implements DataPersister {

    private static final Logger logger = Logger.getLogger(ServerDataPersisterImpl.class.getName());

    private final BulkArticleFileDAOImpl bulkArticleDAO = new BulkArticleFileDAOImpl();
    private final DAO<StaffMember> staffDAO = new StaffMembersFileDAOImpl();
    private final DAO<Customer> customerDAO = new CustomerFileDAOImpl();
    private final StockChangeDAO stockChangeDAO = new StockChangeFileDAOImpl();

    // Observer list
    private final List<ServerCallbackConnection> connectedClients = new CopyOnWriteArrayList<>();

    /* (Bulk)-Article */

    public void createBulkArticle(BulkArticle bulkArticle) {
        try {
            bulkArticleDAO.create(bulkArticle);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public BulkArticle readBulkArticle(int id) {
        try {
            return bulkArticleDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public List<BulkArticle> readAllBulkArticles() {
        try {
            return bulkArticleDAO.readAll();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public void updateBulkArticleSynchronized(BulkArticle bulkArticle) {
        try {
            bulkArticleDAO.update(bulkArticle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBulkArticle(int bulkArticle) {
        try {
            bulkArticleDAO.delete(bulkArticle);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    /* StaffMember */

    public void createStaffMember(StaffMember staffMember) {
        try {
            staffDAO.create(staffMember);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public StaffMember readStaffMember(int id) {
        try {
            return staffDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public void createCustomer(Customer customer) {
        try {
            customerDAO.create(customer);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public Customer readCustomer(int id) {
        try {
            return customerDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public void createStockChange(StockChange stockChange) {
        try {
            stockChangeDAO.create(stockChange);
            broadcastUpdateToAllClients();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public List<StockChange> readAllStockChanges() {
        try {
            return stockChangeDAO.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int sendReadLastCreatedBulkArticleIDToClient() {
        return stockChangeDAO.readLastCreatedBulkArticleID();
    }

    public List<Customer> readAllCustomers() {
        try {
            return customerDAO.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StaffMember> readAllStaffMembers() {
        try {
            return staffDAO.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Observer pattern
    public void registerClient(ServerCallbackConnection clientConnection) {
        connectedClients.add(clientConnection);
    }

    public void unregisterClient(ServerCallbackConnection clientConnection) {
        connectedClients.remove(clientConnection);
    }

    public void broadcastUpdateToAllClients() {
        for (ServerCallbackConnection client : connectedClients) {
            client.sendUpdate();
        }
    }
}