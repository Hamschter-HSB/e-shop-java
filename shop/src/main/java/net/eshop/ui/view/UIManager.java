package net.eshop.ui.view;

import net.eshop.domain.dataaccess.DataPersister;
import net.eshop.ui.viewmodel.LoginAndRegistrationViewModel;
import net.eshop.ui.viewmodel.ShopMainViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class UIManager {

    private static final Logger logger = Logger.getLogger(UIManager.class.getName());

    private final JFrame mainFrame = new JFrame("E-Shop");
    private final MenuBar menuBar = new MenuBar();
    private final Menu staffMember = new Menu("Staff");
    private final MenuItem registerStaffMember = new MenuItem("Register staff member");

    private final LoginAndRegistrationViewModel loginAndRegistrationViewModel;
    private final LoginAndRegistrationView loginAndRegistrationView;
    private final ShopMainViewModel shopMainViewModel;
    private final ShopMainView shopMainView;


    public UIManager() {

        DataPersister dataPersister = new DataPersister();

        loginAndRegistrationViewModel = new LoginAndRegistrationViewModel(dataPersister);
        loginAndRegistrationView = new LoginAndRegistrationView(loginAndRegistrationViewModel);

        shopMainViewModel = new ShopMainViewModel(dataPersister);
        shopMainView = new ShopMainView(shopMainViewModel);
    }

    public void start() {

        setup();

        mainFrame.add(loginAndRegistrationView.login());

        JPanel shopMainVJPanel = shopMainView.shop();

        // Starts MainShopView after log in
        loginAndRegistrationViewModel.setLoggedIn(() -> {
            mainFrame.add(shopMainVJPanel);

            if ("STAFF_MEMBER".equals(System.getProperty("CURRENT_USER")))
                menuBar.add(staffMember);
        });

        shopMainViewModel.setRegisteredStaffMember(() -> mainFrame.add(shopMainVJPanel));

        // Back from staff member registration ui
        shopMainView.setUiBackListener(() -> mainFrame.add(shopMainVJPanel));

        registerStaffMember.addActionListener(actionEvent -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(shopMainView.staffMemberRegistration());
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        mainFrame.setVisible(true);
    }

    private void setup() {

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setTitle("Hamschter Inc.");

        mainFrame.setMenuBar(menuBar);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        staffMember.add(registerStaffMember);
    }
}
