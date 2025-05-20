package net.eshop.dataccess;

import net.eshop.domain.Article;
import net.eshop.domain.Customer;
import net.eshop.domain.StaffMember;

import java.io.IOException;
import java.util.List;

public class DataPersister {

    private final DAO<Article> articleDAO = new ArticleFileDAOImpl();
    private final DAO<StaffMember> staffDAO = new StaffMembersFileDAOImpl();
    private final DAO<Customer> customerDAO = new CustomerFileDAOImpl();

    public void create(Article article) {

        try {
            articleDAO.create(article);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Article read(int id) {
        try {
            return articleDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public List<Article> readAll() {
        try {
            return articleDAO.readAll();
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
}