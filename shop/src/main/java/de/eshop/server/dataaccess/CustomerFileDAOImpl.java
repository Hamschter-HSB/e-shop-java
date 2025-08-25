package de.eshop.server.dataaccess;

import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.Customer;
import de.eshop.shared.domain.ShoppingBasket;

import java.io.*;
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
    public synchronized void create(Customer customer) throws IOException {

        List<Customer> customers = readAll();

        int id = customers.stream()
                    .mapToInt(Customer::getNumber)
                    .max()
                    .orElse(0) + 1;

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write(id + ".id=" + id + "\n");
            bufferedReader.write(id + ".name=" + customer.getName() + "\n");
            bufferedReader.write(id + ".password=" + customer.getPassword() + "\n");
            bufferedReader.write(id + ".address=" + customer.getAddress() + "\n");
        }
    }

    @Override
    public synchronized Customer read(int id) throws IOException {

        if (!containsUser(id))
            return null;

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

            return new Customer(number, userName, password, address, new ShoppingBasket());
        }
    }

    @Override
    public synchronized List<Customer> readAll() throws IOException {

        List<Customer> allCustomers = new ArrayList<>();
        Set<Integer> allCustomerIds = new LinkedHashSet<>();

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
    public synchronized void update(Customer type) {

    }

    @Override
    public synchronized void delete(int id) {

    }

    private synchronized boolean containsUser(int id) throws IOException {

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
