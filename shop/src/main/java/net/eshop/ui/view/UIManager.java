package net.eshop.ui.view;

import net.eshop.domain.dataaccess.DataPersister;
import net.eshop.ui.viewmodel.LoginAndRegistrationViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class UIManager {

    private static final Logger logger = Logger.getLogger(UIManager.class.getName());

    private final JFrame mainFrame = new JFrame("E-Shop");

    private final LoginAndRegistrationView loginAndRegistrationView;
    private final ShopUI shopUI;


    public UIManager() {

        DataPersister dataPersister = new DataPersister();

        LoginAndRegistrationViewModel loginAndRegistrationViewModel = new LoginAndRegistrationViewModel(dataPersister);
        loginAndRegistrationView = new LoginAndRegistrationView(loginAndRegistrationViewModel);

        shopUI = new ShopUI(dataPersister);
    }

    public void start() {

        setup();

        mainFrame.add(loginAndRegistrationView.login());
        mainFrame.setVisible(true);
    }

    private void setup() {

        mainFrame.setLayout(new BorderLayout());

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem menuItem = new MenuItem("Close");
        fileMenu.add(menuItem);
        menuBar.add(fileMenu);

        mainFrame.setMenuBar(menuBar);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setSize(1920, 1080);
    }
}
