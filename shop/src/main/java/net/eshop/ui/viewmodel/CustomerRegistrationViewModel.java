package net.eshop.ui.viewmodel;

import net.eshop.domain.Customer;
import net.eshop.domain.ShoppingBasket;
import net.eshop.domain.dataaccess.DataPersister;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class CustomerRegistrationViewModel {

    private static final Logger logger = Logger.getLogger(CustomerRegistrationViewModel.class.getName());

    private final DataPersister dataPersister;

    public CustomerRegistrationViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public void registerButtonClickHandler(String userName, char[] charPassword, JPanel registrationMainPanel, String address) {

        String password = getPasswordStringFromPasswordField(charPassword);

        Customer customer = dataPersister.findUserByCredentials(userName, password, Customer.class);

        if (customer != null) {

            logger.info("Register process failed for customer with chosen name: " + userName);

            JOptionPane.showMessageDialog(registrationMainPanel,
                    "Username is already taken.",
                    "Unavailable Username",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Customer> customers = dataPersister.readAllCustomers();
        int customerId = 1;

        try {
            customerId = customers.getLast().getNumber() + 1;
        } catch (NoSuchElementException ignored) {
        }

        Customer newCustomer = new Customer(customerId, userName, password, address, new ShoppingBasket(new HashMap<>()));
        dataPersister.createCustomer(newCustomer);

        registrationMainPanel.setVisible(false);

        System.setProperty("CURRENT_USER", "CUSTOMER");
        System.setProperty("CURRENT_USER_ID", String.valueOf(newCustomer.getNumber()));

        logger.info("Registered customer " + userName + " successfully.");
    }

    protected static String getPasswordStringFromPasswordField(char[] password) {

        StringBuilder stringBuilder = new StringBuilder();

        for (char c : password)
            stringBuilder.append(c);

        return stringBuilder.toString();
    }
}
