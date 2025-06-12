package net.eshop.domain.dataaccess;

import net.eshop.domain.Article;
import net.eshop.domain.BulkArticle;
import net.eshop.domain.Customer;
import net.eshop.domain.StaffMember;
import net.eshop.domain.events.StockChange;

import java.io.IOException;
import java.util.List;

public class DataPersister {

    private final DAO<BulkArticle> bulkArticleDAO = new BulkArticleFileDAOImpl();
    private final DAO<StaffMember> staffDAO = new StaffMembersFileDAOImpl();
    private final DAO<Customer> customerDAO = new CustomerFileDAOImpl();
    private final DAO<StockChange> stockChangeDAO = new StockChangeDAOImpl();

    // TODO REMOVE and use own DAO. Temporary code
    int currentUserId = 1;
    private final Customer customer = readCustomer(currentUserId);

    /* (Bulk)-Article */

    public void createBulkArticle(BulkArticle bulkArticle) {

        try {
            bulkArticleDAO.create(bulkArticle);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

//    public void createArticle(Article article) {
//        try {
//            bulkArticleDAO.create((BulkArticle) article);
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//    }

    public BulkArticle readBulkArticle(int id) {
        try {
            return bulkArticleDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

//    public Article readArticle(int id) {
//        try {
//            return bulkArticleDAO.read(id);
//        } catch (IOException ioException) {
//            throw new RuntimeException(ioException.getMessage());
//        }
//    }

    public List<BulkArticle> readAllBulkArticles() {
        try {
            return bulkArticleDAO.readAll();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public void updateBulkArticle(BulkArticle bulkArticle) {
        try {
            bulkArticleDAO.update(bulkArticle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public void updateArticle(Article article) {
//        try {
//            bulkArticleDAO.update((BulkArticle) article);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

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
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    // TODO REMOVE and use own DAO. Temporary code
    public Customer getCustomer() {
        return customer;
    }
}