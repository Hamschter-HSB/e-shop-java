package net.eshop.ui;

import net.eshop.domain.dataaccess.DataPersister;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class UIManager {

    private static final Logger logger = Logger.getLogger(UIManager.class.getName());

    private final JFrame mainFrame = new JFrame("E-Shop");

    private final LoginUI loginUI;

    public UIManager() {

        DataPersister dataPersister = new DataPersister();
        loginUI = new LoginUI(dataPersister);
    }

    public void start() {

        setup();

        mainFrame.add(loginUI.loginAndRegister());
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
