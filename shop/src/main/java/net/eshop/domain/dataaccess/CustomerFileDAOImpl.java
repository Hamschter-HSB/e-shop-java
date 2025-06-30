package net.eshop.domain.dataaccess;

import net.eshop.domain.BulkArticle;
import net.eshop.domain.Customer;
import net.eshop.domain.ShoppingBasket;
import net.eshop.domain.User;
import net.eshop.exceptions.UserNotFoundException;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerFileDAOImpl implements DAO<Customer> {

    private static final Logger logger = Logger.getLogger(CustomerFileDAOImpl.class.getName());

    private final String REG_EX = ".*=";

    public static final String DATA_PATH = "Data";
    public static final String CUSTOMERS = "Customers";

    private final File file = new File(CUSTOMERS);

    public CustomerFileDAOImpl() {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error creating file \"" + CUSTOMERS + "\"", e);
            }
        }
    }

    @Override
    public void create(Customer customer) throws IOException {

        List<Customer> customers = readAll();
        int id = 1;

        try {
            id = customers.getLast().getNumber() + 1;
        } catch (NoSuchElementException ignored) {
        }

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write(id + ".id=" + id + "\n");
            bufferedReader.write(id + ".name=" + customer.getName() + "\n");
            bufferedReader.write(id + ".password=" + customer.getPassword() + "\n");
            bufferedReader.write(id + ".address=" + customer.getAddress() + "\n");
            //TODO Add shopping basket using a OWN DAO
        }
    }

    @Override
    public Customer read(int id) throws IOException {

        if (!containsUser(id))
            throw new UserNotFoundException(MessageFormat.format("No user with number {0}", id));

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateArticles = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateArticles.add(line);
            });

            assert duplicateArticles.size() == 4;

            int number = Integer.parseInt(duplicateArticles.get(0).replaceAll(REG_EX, ""));
            String userName = duplicateArticles.get(1).replaceAll(REG_EX, "");
            String password = duplicateArticles.get(2).replaceAll(REG_EX, "");
            String address = duplicateArticles.get(3).replaceAll(REG_EX, "");

            return new Customer(number, userName, password, address, new ShoppingBasket(new HashMap<>()));
        }
    }

    @Override
    public List<Customer> readAll() throws IOException {

        List<Customer> allCustomers = new ArrayList<>();
        Set<Integer> allCustomerIds = new HashSet<>();

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            AtomicInteger counter = new AtomicInteger();

            bufferedReader.lines().forEach(line -> {
                if (counter.getAndIncrement() % 4 == 0) {
                    allCustomerIds.add(Integer.parseInt(line.replaceAll(REG_EX, "")));
                }
            });

            allCustomerIds.forEach(userID -> {
                try {
                    allCustomers.add(read(userID));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return allCustomers;
    }

    @Override
    public void update(Customer type) {

    }

    @Override
    public void delete(int id) {

    }

    private boolean containsUser(int id) throws IOException {

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateStaffMembers = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateStaffMembers.add(line);
            });

            return !duplicateStaffMembers.isEmpty();
        }
    }
}
