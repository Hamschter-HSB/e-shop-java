package net.eshop.ui.view;

import net.eshop.domain.dataaccess.DataPersister;
import net.eshop.ui.viewmodel.AddArticleDialogViewModel;
import net.eshop.ui.viewmodel.LoginAndRegistrationViewModel;
import net.eshop.ui.viewmodel.ShopMainViewModel;

import javax.swing.*;
import java.awt.*;
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

    private final LoginAndRegistrationViewModel loginAndRegistrationViewModel;
    private final LoginAndRegistrationView loginAndRegistrationView;
    private final ShopMainViewModel shopMainViewModel;
    private final ShopMainView shopMainView;
    private final AddArticleDialogViewModel addArticleDialogViewModel;


    public UIManager() {

        DataPersister dataPersister = new DataPersister();

        loginAndRegistrationViewModel = new LoginAndRegistrationViewModel(dataPersister);
        loginAndRegistrationView = new LoginAndRegistrationView(loginAndRegistrationViewModel);

        shopMainViewModel = new ShopMainViewModel(dataPersister);
        shopMainView = new ShopMainView(shopMainViewModel);

        addArticleDialogViewModel = new AddArticleDialogViewModel(dataPersister);
    }

    public void start() {

        setup();

        mainFrame.add(loginAndRegistrationView.login());

        JPanel shopMainViewJPanel = shopMainView.shop();
        // Starts MainShopView after log in
        loginAndRegistrationViewModel.setLoggedIn(() -> {
            mainFrame.add(shopMainViewJPanel);

            mainFrame.setMenuBar(menuBar);

            if ("STAFF_MEMBER".equals(System.getProperty("CURRENT_USER")))
                menuBar.add(staffMember);
        });

        shopMainViewModel.setRegisteredStaffMember(() -> mainFrame.add(shopMainViewJPanel));

        // Logout current user (revokes CURRENT_USER property)
        shopMainViewModel.setLogoutListener(() -> {
                    mainFrame.getContentPane().removeAll();
                    mainFrame.add(loginAndRegistrationView.login());
                    mainFrame.revalidate();
                    mainFrame.repaint();

                    mainFrame.setMenuBar(null);
                    menuBar.remove(staffMember);
                }
        );

        // Back from staff member registration ui
        shopMainView.setUiBackListener(() -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(shopMainViewJPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Register staff member
        registerStaffMember.addActionListener(actionEvent -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(shopMainView.staffMemberRegistration());
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // ManageArticles/AddArticle
        addArticles.addActionListener(actionEvent -> new AddArticleDialogView(addArticleDialogViewModel, mainFrame).openAddArticleDialog());

        // Open stock changes in ShopMainView
        stockChanges.addActionListener(actionEvent -> {
            shopMainView.activateStockChangesPanel();
        });

        // Open articles in ShopMainView
        articles.addActionListener(actionEvent -> {
            shopMainView.activateArticleListPanel();
        });

        mainFrame.setVisible(true);
    }

    private void setup() {

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setTitle("Hamschter Inc.");

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainMenu.add(articles);
        menuBar.add(mainMenu);

        staffMember.add(registerStaffMember);
        staffMember.add(manageArticlesMenu);
        staffMember.add(stockChanges);

        manageArticlesMenu.add(addArticles);
    }
}
