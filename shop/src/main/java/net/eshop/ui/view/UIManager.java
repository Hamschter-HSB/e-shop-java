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
    private final Menu fileMenu = new Menu("File");
    private final Menu staffMember = new Menu("StaffMember");
    private final MenuItem menuItem = new MenuItem("Close");
    private final MenuItem registerStaffMember = new MenuItem("Register staff member");

    private final LoginAndRegistrationView loginAndRegistrationView;
    private final LoginAndRegistrationViewModel loginAndRegistrationViewModel;
    private final ShopMainView shopMainView;


    public UIManager() {

        DataPersister dataPersister = new DataPersister();

        loginAndRegistrationViewModel = new LoginAndRegistrationViewModel(dataPersister);
        loginAndRegistrationView = new LoginAndRegistrationView(loginAndRegistrationViewModel);

        ShopMainViewModel shopMainViewModel = new ShopMainViewModel(dataPersister);
        shopMainView = new ShopMainView(shopMainViewModel);
    }

    public void start() {

        setup();

        mainFrame.add(loginAndRegistrationView.login());

        loginAndRegistrationViewModel.setLoggedIn(() -> {
            mainFrame.add(shopMainView.shop());

            if ("STAFF_MEMBER".equals(System.getProperty("CURRENT_USER")))
                menuBar.add(staffMember);
        });

        mainFrame.setVisible(true);

    }

    private void setup() {

        mainFrame.setLayout(new BorderLayout());

        fileMenu.add(menuItem);
        staffMember.add(registerStaffMember);
        menuBar.add(fileMenu);

        mainFrame.setMenuBar(menuBar);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setSize(1920, 1080);
    }
}
