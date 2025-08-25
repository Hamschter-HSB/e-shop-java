package de.eshop.client.ui.view;

import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.client.ui.viewmodel.*;
import de.eshop.shared.domain.exceptions.ArticleOutOfStockException;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UIManager {

    private static final Logger logger = Logger.getLogger(UIManager.class.getName());

    private final JFrame mainFrame = new JFrame("E-Shop");
    private final MenuBar menuBar = new MenuBar();
    private final Menu mainMenu = new Menu("Menu");
    private final MenuItem articles = new MenuItem("Articles");
    private final Menu staffMember = new Menu("Staff");
    private final MenuItem registerStaffMember = new MenuItem("Register staff member");
    private final Menu manageArticlesMenu = new Menu("Articles");
    private final MenuItem addArticles = new MenuItem("Add...");
    private final MenuItem stockChanges = new MenuItem("Stock changes");
    private final MenuItem stockHistory = new MenuItem("Stock history");
    private final MenuItem editStock = new MenuItem("Edit stock...");
    private final MenuItem buyArticles = new MenuItem("Buy");

    private final LoginAndRegistrationViewModel loginAndRegistrationViewModel;
    private final LoginAndRegistrationView loginAndRegistrationView;
    private final ShopMainViewModel shopMainViewModel;
    private final ShopMainView shopMainView;
    private final AddArticleDialogViewModel addArticleDialogViewModel;
    private final EditArticleStockDialogViewModel editArticleStockDialogViewModel;
    private final StockHistoryDialogViewModel stockHistoryDialogViewModel;
    private final AddArticleToCartDialogViewModel addArticleToCartDialogViewModel;
    private final ShowInvoiceDialogViewModel showInvoiceDialogViewModel;

    public UIManager(ClientDataPersisterImpl clientDataPersisterImpl) {

        loginAndRegistrationViewModel = new LoginAndRegistrationViewModel(clientDataPersisterImpl);
        loginAndRegistrationView = new LoginAndRegistrationView(loginAndRegistrationViewModel);

        shopMainViewModel = new ShopMainViewModel(clientDataPersisterImpl);
        shopMainView = new ShopMainView(shopMainViewModel);

        addArticleDialogViewModel = new AddArticleDialogViewModel(clientDataPersisterImpl);
        editArticleStockDialogViewModel = new EditArticleStockDialogViewModel(clientDataPersisterImpl);
        stockHistoryDialogViewModel = new StockHistoryDialogViewModel(clientDataPersisterImpl);
        addArticleToCartDialogViewModel = new AddArticleToCartDialogViewModel(clientDataPersisterImpl);
        showInvoiceDialogViewModel = new ShowInvoiceDialogViewModel(clientDataPersisterImpl);
    }

    public void start() {

        setup();

        JPanel loginPanel = loginAndRegistrationView.login();
        mainFrame.add(loginPanel);

        JPanel registrationPanel = loginAndRegistrationView.customerRegistration();

        JPanel shopMainViewJPanel = shopMainView.shop();

        // Starts MainShopView after log in
        loginAndRegistrationViewModel.setLoggedIn(() -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(shopMainViewJPanel);

            // We have to do this call to solve the issue that the current user is not set, before a log ing.
            // This causes that when a staff member logs in, he won't see the article stocks in the article list.
            // This simple call fixes the article list for a staff member
            shopMainView.activateArticleListPanelAfterLogIn();

            mainFrame.setMenuBar(menuBar);

            if (ViewModelUtils.currentUserIsStaffMember())
                menuBar.add(staffMember);
            else
                mainMenu.add(buyArticles);
        });

        // Back to log in view for customer
        loginAndRegistrationView.backToLoginButtonPressed(() -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(loginPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Open registration view for customer
        loginAndRegistrationView.registrationButtonPressed(() -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(registrationPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Register Customer
        shopMainViewModel.setRegisteredStaffMember(() -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(shopMainViewJPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Logout current user (revokes CURRENT_USER property)
        shopMainViewModel.setLogoutListener(() -> {
                    mainFrame.getContentPane().removeAll();
                    mainFrame.add(loginPanel);
                    mainFrame.revalidate();
                    mainFrame.repaint();

                    mainFrame.setMenuBar(null);
                    menuBar.remove(staffMember);
                    mainMenu.remove(buyArticles);
                }
        );

        // Back from staff member registration ui
        shopMainView.setUiBackListener(() -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(shopMainViewJPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
            shopMainViewJPanel.setVisible(true);
        });

        JPanel staffMemberRegistrationPanel = shopMainView.staffMemberRegistration();

        // Register staff member
        registerStaffMember.addActionListener(actionEvent -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(staffMemberRegistrationPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // ManageArticles/AddArticle
        addArticles.addActionListener(actionEvent -> {
            new AddArticleDialogView(addArticleDialogViewModel, mainFrame).openAddArticleDialog();
            shopMainView.activateArticleListPanel();
        });

        // ManageArticles/EditArticleStock
        editStock.addActionListener(actionEvent -> {
            new EditArticleStockDialogView(editArticleStockDialogViewModel, mainFrame).openEditArticleStockDialog();
            shopMainView.activateArticleListPanel();
        });

        // ManageArticles/ShowStockHistory
        stockHistory.addActionListener(actionEvent -> {
            new StockHistoryDialogView(stockHistoryDialogViewModel, mainFrame).openStockHistoryDialog();
            shopMainView.activateArticleListPanel();
        });

        buyArticles.addActionListener(actionEvent -> {
            try {
                new ShowInvoiceDialogView(showInvoiceDialogViewModel, mainFrame).openShowInvoiceDialog();
            } catch (ArticleOutOfStockException exception) {
                logger.log(Level.SEVERE, "An Error has occurred: Article's (" + exception.getArticleNameAndNumber() + ") stock is exceeded!");
                System.out.println("Article: " + exception.getArticleNameAndNumber() + " stock is exceeded!");
            }
            shopMainView.activateArticleListPanel();
        });

        // Open stock changes in ShopMainView
        stockChanges.addActionListener(actionEvent -> {
            shopMainView.activateStockChangesPanel();
        });

        // Open articles in ShopMainView
        articles.addActionListener(actionEvent -> {
            shopMainView.activateArticleListPanel();
        });

        // Add articles in AddArticleToCartDisplayView
        shopMainView.setAddArticleToCartListener((int bulkArticleID) -> {
            new AddArticleToCartDialogView(addArticleToCartDialogViewModel, mainFrame).openAddArticleToCartDialog(bulkArticleID);

            // Check which view is currently active so that the correct view (either cart or article view) can be updated after the Edit Amount of Article button click
            if (shopMainView.currentSceneIsArticleView()) shopMainView.activateArticleListPanel();
            else shopMainView.activateShoppingCartPanel();
        });

        mainFrame.setVisible(true);
    }

    private void setup() {

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setTitle("Hamschter Inc.");
        Image icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("Hamschter01_01.png")
        );
        mainFrame.setIconImage(icon);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainMenu.add(articles);
        menuBar.add(mainMenu);

        staffMember.add(registerStaffMember);
        staffMember.add(manageArticlesMenu);
        staffMember.add(stockChanges);

        manageArticlesMenu.add(addArticles);
        manageArticlesMenu.add(editStock);
        manageArticlesMenu.add(stockHistory);
    }

    public synchronized void revalidateAndUpdateUI() {
        shopMainView.activateArticleListPanel();
    }
}
