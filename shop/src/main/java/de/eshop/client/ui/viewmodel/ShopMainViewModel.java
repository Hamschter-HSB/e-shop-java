package de.eshop.client.ui.viewmodel;

import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.client.hashing.PasswordHasher;
import de.eshop.client.ui.DialogUtils;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.StaffMember;
import de.eshop.shared.domain.events.StockChange;
import de.eshop.shared.domain.exceptions.StaffMemberNotFoundException;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ShopMainViewModel {

    private static final Logger logger = Logger.getLogger(ShopMainViewModel.class.getName());

    private final ClientDataPersisterImpl clientDataPersisterImpl;
    private Runnable registrationListener;
    private Runnable logoutListener;

    public ShopMainViewModel(ClientDataPersisterImpl clientDataPersisterImpl) {
        this.clientDataPersisterImpl = clientDataPersisterImpl;
    }

    public void registerButtonClickHandler(JTextField userNameTextField, JPasswordField passwordField , JPanel registrationMainPanel) throws StaffMemberNotFoundException {

        String userName = userNameTextField.getText();
        char[] charPassword = passwordField.getPassword();

        if (userName.isBlank() || charPassword.length == 0) {
            DialogUtils.emptyInputFields(registrationMainPanel);
            logger.info("Registration failed for staff member " + userName + ". Empty input field(s)");
            return;
        }

        String password = PasswordHasher.hashPassword(new String(charPassword));

        StaffMember staffMember = clientDataPersisterImpl.findUserByCredentials(userName, password, StaffMember.class);

        if (staffMember != null) {

            logger.info("Register process failed for customer with chosen name: " + userName);

            JOptionPane.showMessageDialog(registrationMainPanel,
                    "Username is already taken.",
                    "Unavailable Username",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<StaffMember> staffMembers = clientDataPersisterImpl.readAllStaffMembers();
        int staffMemberID = 1;

        try {
            staffMemberID = staffMembers.getLast().getNumber() + 1;
        } catch (NoSuchElementException ignored) {
            throw new StaffMemberNotFoundException("No staff member with id " + (staffMemberID) + " was found!", staffMemberID);
        }

        StaffMember newStaffMember = new StaffMember(staffMemberID, userName, password);
        clientDataPersisterImpl.createStaffMember(newStaffMember);

        userNameTextField.setText("");
        passwordField.setText("");

        if (registrationListener != null)
            registrationListener.run();

        logger.info("Registered customer " + userName + " successfully.");
    }

    public void logOutCurrentUser() {
        if (logoutListener != null) {
            logoutListener.run();

            System.clearProperty("CURRENT_USER");
            System.clearProperty("CURRENT_USER_ID");

            logger.info("Successfully logged out user");
        }
    }

    public String[][] loadArticles() {

        List<BulkArticle> bulkArticles = clientDataPersisterImpl.readAllBulkArticles();
        String[][] table = new String[bulkArticles.size()][6];

        AtomicInteger counter = new AtomicInteger(0);

        bulkArticles.forEach(bulkArticle -> {

            //BulkArticles with stockSize of <=0 are not meant to be seen in the ShopMainViewTable at all.
            if (bulkArticle.getStock() <= 0)
                return;

            table[counter.get()][0] = String.valueOf(bulkArticle.getArticleNumber());
            table[counter.get()][1] = String.valueOf(bulkArticle.getName());
            table[counter.get()][2] = String.valueOf(bulkArticle.getDescription());
            table[counter.get()][3] = String.valueOf(bulkArticle.getStock());
            table[counter.get()][4] = String.format(Locale.US, "%.2f €", bulkArticle.getPrice());
            table[counter.get()][5] = String.valueOf(bulkArticle.getBulkSize());
            counter.incrementAndGet();
        });

        return table;
    }

    public String[][] loadSearchedArticles(String searchTerm) {

        List<BulkArticle> bulkArticles = clientDataPersisterImpl.readAllBulkArticles();
        List<BulkArticle> filteredBulkArticles =    bulkArticles.stream().filter(bulkArticle ->

                                                    bulkArticle.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                                    bulkArticle.getDescription().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                                    String.valueOf(bulkArticle.getPrice()).contains(searchTerm.toLowerCase()) ||
                                                    String.valueOf(bulkArticle.getBulkSize()).contains(searchTerm.toLowerCase()))
                                                    .toList();
        String[][] table = new String[filteredBulkArticles.size()][6];

        AtomicInteger counter = new AtomicInteger(0);

        filteredBulkArticles.forEach(bulkArticle -> {

            //BulkArticles with stockSize of <=0 are not meant to be seen in the ShopMainViewTable at all.
            if (bulkArticle.getStock() <= 0)
                return;

            table[counter.get()][0] = String.valueOf(bulkArticle.getArticleNumber());
            table[counter.get()][1] = String.valueOf(bulkArticle.getName());
            table[counter.get()][2] = String.valueOf(bulkArticle.getDescription());
            table[counter.get()][3] = String.valueOf(bulkArticle.getStock());
            table[counter.get()][4] = String.format(Locale.US, "%.2f €", bulkArticle.getPrice());
            table[counter.get()][5] = String.valueOf(bulkArticle.getBulkSize());
            counter.incrementAndGet();
        });

        return table;
    }

    public String[][] loadStocks() {

        List<StockChange> stockChanges = clientDataPersisterImpl.readAllStockChanges();
        String[][] table = new String[stockChanges.size()][6];

        AtomicInteger counter = new AtomicInteger(0);

        stockChanges.forEach(stockChange -> {
            table[counter.get()][0] = String.valueOf(stockChange.getId());
            table[counter.get()][1] = String.valueOf(stockChange.getDayOfYear());
            table[counter.get()][2] = String.valueOf(stockChange.getArticleNumber());
            table[counter.get()][3] = String.valueOf(stockChange.getOldAmount());
            table[counter.get()][4] = String.valueOf(stockChange.getNewAmount());
            table[counter.get()][5] = String.valueOf(stockChange.getUserID());
            counter.incrementAndGet();
        });

        return table;
    }

    public String[][] loadShoppingCart() {

        Map<BulkArticle, Integer> articleAndAmount = new HashMap<>();

        clientDataPersisterImpl.getCustomer().getShoppingBasket().getArticleMap().forEach((key, value) -> {
            BulkArticle bulkArticle = clientDataPersisterImpl.readBulkArticle(key);
            articleAndAmount.put(bulkArticle, value);
        });

        String[][] table = new String[articleAndAmount.size()][5];
        AtomicInteger counter = new AtomicInteger(0);

        articleAndAmount.forEach((bulkArticle, amount) -> {
            table[counter.get()][0] = String.valueOf(bulkArticle.getArticleNumber());
            table[counter.get()][1] = String.valueOf(bulkArticle.getName());
            table[counter.get()][2] = String.valueOf(bulkArticle.getDescription());
            table[counter.get()][3] = String.format(Locale.US, "%.2f €", bulkArticle.getPrice());
            table[counter.get()][4] = String.valueOf(amount);
            counter.incrementAndGet();
        });

        return table;
    }

    public void removeArticleFromCart(int articleID) {
        clientDataPersisterImpl.getCustomer().getShoppingBasket().removeFromArticleMap(articleID);
    }

    public void setRegisteredStaffMember(Runnable registrationListener) {
        this.registrationListener = registrationListener;
    }

    public void setLogoutListener(Runnable logoutListener) {
        this.logoutListener = logoutListener;
    }
}
