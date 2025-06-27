package net.eshop.ui.viewmodel;

import net.eshop.domain.Customer;
import net.eshop.domain.ShoppingBasket;
import net.eshop.domain.StaffMember;
import net.eshop.domain.dataaccess.DataPersister;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class LoginAndRegistrationViewModel {

    private static final Logger logger = Logger.getLogger(LoginAndRegistrationViewModel.class.getName());

    private final DataPersister dataPersister;

    public LoginAndRegistrationViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public void registerButtonClickHandler(String userName, char[] charPassword, String address, JPanel registrationMainPanel) {

        if (userName.isBlank() || charPassword.length == 0 || address.isBlank()) {
            oneOrMoreFieldsEmptyDialog(userName, registrationMainPanel);
            return;
        }

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

    public void customerLoginButtonClickHandler(String userName, char[] charPassword, JPanel loginMainPanel) {

        if (userName.isBlank() || charPassword.length == 0) {
            oneOrMoreFieldsEmptyDialog(userName, loginMainPanel);
            return;
        }

        String password = getPasswordStringFromPasswordField(charPassword);

        Customer customer = dataPersister.findUserByCredentials(userName, password, Customer.class);

        if (customer == null) {

            logger.info("Login failed for customer " + userName);

            JOptionPane.showMessageDialog(loginMainPanel,
                    "Username or password is incorrect.",
                    "Wrong credentials",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        loginMainPanel.setVisible(false);

        System.setProperty("CURRENT_USER", "CUSTOMER");
        System.setProperty("CURRENT_USER_ID", String.valueOf(customer.getNumber()));

        logger.info("Login successfully for customer " + userName);
    }

    public void staffMemberLoginButtonClickHandler(String userName, char[] charPassword, JPanel loginMainPanel, JLabel employeeLogin) {
        employeeLogin.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (userName.isBlank() || charPassword.length == 0) {
                    oneOrMoreFieldsEmptyDialog(userName, loginMainPanel);
                    return;
                }

                String password = getPasswordStringFromPasswordField(charPassword);

                StaffMember staffMember = dataPersister.findUserByCredentials(userName, password, StaffMember.class);

                if (staffMember == null) {

                    logger.info("Login failed for staff member " + userName);

                    JOptionPane.showMessageDialog(loginMainPanel,
                            "Username or password is incorrect.",
                            "Wrong credentials",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                loginMainPanel.setVisible(false);

                System.setProperty("CURRENT_USER", "STAFF_MEMBER");
                System.setProperty("CURRENT_USER_ID", String.valueOf(staffMember.getNumber()));

                logger.info("Login successfully for staff member " + userName);
            }
        });
    }

    private static void oneOrMoreFieldsEmptyDialog(String userName, JPanel registrationMainPanel) {

        logger.info("Login failed for customer " + userName + ". Empty input field(s)");

        JOptionPane.showMessageDialog(registrationMainPanel,
                "One or more fields are empty.",
                "Empty input field(s).",
                JOptionPane.WARNING_MESSAGE);
    }

    private static String getPasswordStringFromPasswordField(char[] password) {

        StringBuilder stringBuilder = new StringBuilder();

        for (char c : password)
            stringBuilder.append(c);

        return stringBuilder.toString();
    }
}
