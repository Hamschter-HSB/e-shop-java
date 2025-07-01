package net.eshop.ui.viewmodel;

import net.eshop.domain.BulkArticle;
import net.eshop.domain.Customer;
import net.eshop.domain.StaffMember;
import net.eshop.domain.dataaccess.DataPersister;
import net.eshop.domain.events.StockChange;
import net.eshop.ui.DialogUtils;
import net.eshop.ui.events.LogoutListener;
import net.eshop.ui.events.RegistrationListener;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ShopMainViewModel {

    private static final Logger logger = Logger.getLogger(ShopMainViewModel.class.getName());

    private final DataPersister dataPersister;
    private RegistrationListener registrationListener;
    private LogoutListener logoutListener;

    public ShopMainViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public void registerButtonClickHandler(String userName, char[] charPassword, JPanel registrationMainPanel) {

        if (userName.isBlank() || charPassword.length == 0) {
            DialogUtils.emptyInputFields(registrationMainPanel);
            logger.info("Registration failed for staff member " + userName + ". Empty input field(s)");
            return;
        }

        String password = new String(charPassword);

        Customer customer = dataPersister.findUserByCredentials(userName, password, Customer.class);

        if (customer != null) {

            logger.info("Register process failed for customer with chosen name: " + userName);

            JOptionPane.showMessageDialog(registrationMainPanel,
                    "Username is already taken.",
                    "Unavailable Username",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<StaffMember> staffMembers = dataPersister.readAllStaffMembers();
        int staffMemberID = 1;

        try {
            staffMemberID = staffMembers.getLast().getNumber() + 1;
        } catch (NoSuchElementException ignored) {
        }

        StaffMember newStaffMember = new StaffMember(staffMemberID, userName, password);
        dataPersister.createStaffMember(newStaffMember);

        registrationMainPanel.setVisible(false);

        if (registrationListener != null)
            registrationListener.onRegistrationSuccess();

        logger.info("Registered customer " + userName + " successfully.");
    }

    public void logOutCurrentUser() {
        if (logoutListener != null) {
            logoutListener.onLogoutSuccess();

            System.clearProperty("CURRENT_USER");
            System.clearProperty("CURRENT_USER_ID");

            logger.info("Successfully logged out user");
        }
    }

    public String[][] loadArticles() {

        List<BulkArticle> bulkArticles = dataPersister.readAllBulkArticles();
        String[][] table = new String[bulkArticles.size()][6];

        AtomicInteger counter = new AtomicInteger(0);

        bulkArticles.forEach(stockChange -> {
            table[counter.get()][0] = String.valueOf(stockChange.getArticleNumber());
            table[counter.get()][1] = String.valueOf(stockChange.getName());
            table[counter.get()][2] = String.valueOf(stockChange.getDescription());
            table[counter.get()][3] = String.valueOf(stockChange.getStock());
            table[counter.get()][4] = String.valueOf(stockChange.getPrice());
            table[counter.get()][5] = String.valueOf(stockChange.getBulkSize());
            counter.incrementAndGet();
        });

        return table;
    }

    public String[][] loadStocks() {

        List<StockChange> stockChanges = dataPersister.readAllStockChanges();
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

        dataPersister.getCustomer().getShoppingBasket().getArticleMap().forEach((key, value) -> {
            BulkArticle bulkArticle = dataPersister.readBulkArticle(key);
            articleAndAmount.put(bulkArticle, value);
        });

        String[][] table = new String[articleAndAmount.size()][5];
        AtomicInteger counter = new AtomicInteger(0);

        articleAndAmount.forEach((bulkArticle, amount) -> {
            table[counter.get()][0] = String.valueOf(bulkArticle.getArticleNumber());
            table[counter.get()][1] = String.valueOf(bulkArticle.getName());
            table[counter.get()][2] = String.valueOf(bulkArticle.getDescription());
            table[counter.get()][3] = String.valueOf(bulkArticle.getPrice());
            table[counter.get()][4] = String.valueOf(amount);
            counter.incrementAndGet();
        });

        return table;
    }

    public void removeArticleFromCart(int articleID) {
        dataPersister.getCustomer().getShoppingBasket().removeFromArticleMap(articleID);
    }

    public void setRegisteredStaffMember(RegistrationListener registrationListener) {
        this.registrationListener = registrationListener;
    }

    public void setLogoutListener(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }
}
