package net.eshop.domain.dataaccess;

import net.eshop.domain.Article;
import net.eshop.domain.Customer;
import net.eshop.domain.StaffMember;
import net.eshop.domain.events.StockChange;

import java.io.IOException;
import java.util.List;

public class DataPersister {

    private final DAO<Article> articleDAO = new ArticleFileDAOImpl();
    private final DAO<StaffMember> staffDAO = new StaffMembersFileDAOImpl();
    private final DAO<Customer> customerDAO = new CustomerFileDAOImpl();
    private final DAO<StockChange> stockChangeDAO = new StockChangeDAOImpl();

    // TODO REMOVE and use own DAO. Temporary code
    int currentUserId = 1;
    private final Customer customer = readCustomer(currentUserId);

    public void createArticle(Article article) {

        try {
            articleDAO.create(article);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Article readArticle(int id) {
        try {
            return articleDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public List<Article> readAllArticles() {
        try {
            return articleDAO.readAll();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public void updateArticle(Article article) {
        try {
            articleDAO.update(article);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    // TODO REMOVE and use own DAO. Temporary code
    public Customer getCustomer() {
        return customer;
    }
}