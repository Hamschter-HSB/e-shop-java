package net.eshop.ui.view;

import net.eshop.ui.viewmodel.ShopMainViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ShopMainView {

    private static final Logger logger = Logger.getLogger(ShopMainView.class.getName());

    private final ShopMainViewModel shopMainViewModel;

    public ShopMainView(ShopMainViewModel shopMainViewModel) {
        this.shopMainViewModel = shopMainViewModel;
    }

    public JPanel shop(){
        // Panels
        JPanel shopMainPanel = new JPanel(new BorderLayout());

        JPanel shopSidePanel = new JPanel(new BorderLayout());

        JPanel shopCenterPanel = new JPanel();

        // Content of shopMainPanel.NORTH
        JLabel shopLabel = new JLabel("Shop");
        JButton cartButton = new JButton("Cart");

        // Content of shopSidePanel
        JTextField searchTextField = new JTextField();
        JButton searchButton = new JButton("Search");

        JLabel hamschterLabel = new JLabel("Hamschter Inc.");
        JLabel logoutLabel = new JLabel("<HTML><U>Logout</U></HTML>");
        logoutLabel.setForeground(Color.BLUE);

        // Filling shopMainPanel
        shopMainPanel.add(shopLabel, BorderLayout.NORTH);
        shopMainPanel.add(cartButton, BorderLayout.NORTH);

        shopMainPanel.add(shopSidePanel, BorderLayout.WEST);
        shopMainPanel.add(shopCenterPanel, BorderLayout.CENTER);

        // Filling shopSidePanel
        shopSidePanel.add(searchTextField, BorderLayout.NORTH);
        shopSidePanel.add(searchButton, BorderLayout.NORTH);

        shopSidePanel.add(hamschterLabel, BorderLayout.SOUTH);
        shopSidePanel.add(logoutLabel, BorderLayout.SOUTH);

        shopMainPanel.setVisible(true);

        return shopMainPanel;
    }
}
