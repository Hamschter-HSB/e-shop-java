package net.eshop.ui.view;

import net.eshop.ui.events.UIBackListener;
import net.eshop.ui.viewmodel.ShopMainViewModel;
import net.eshop.ui.viewmodel.ViewModelUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.logging.Logger;

public class ShopMainView {

    private static final Logger logger = Logger.getLogger(ShopMainView.class.getName());

    private final ShopMainViewModel shopMainViewModel;
    private UIBackListener uiBackListener;

    private final JPanel shopMainPanel = new JPanel(new BorderLayout());
    private JScrollPane currentCenterPane;

    public ShopMainView(ShopMainViewModel shopMainViewModel) {
        this.shopMainViewModel = shopMainViewModel;
    }

    public JPanel shop() {

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel storeLabel = new JLabel("STORE", SwingConstants.CENTER);
        storeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        headerPanel.add(storeLabel, BorderLayout.CENTER);

        JButton cartButton = new JButton("Cart");
        headerPanel.add(cartButton, BorderLayout.EAST);
        shopMainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar (left)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        sidebar.add(searchPanel);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(new JLabel("Hamschter Inc."));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(actionEvent -> shopMainViewModel.logOutCurrentUser());

        sidebar.add(logoutButton);

        shopMainPanel.add(sidebar, BorderLayout.WEST);

        currentCenterPane = articleListJScrollPane();

        shopMainPanel.add(currentCenterPane, BorderLayout.CENTER);
        shopMainPanel.setVisible(true);

        return shopMainPanel;
    }

    public JPanel staffMemberRegistration() {

        JPanel staffMemberRegistrationMainPanel = new JPanel(new GridBagLayout());
        staffMemberRegistrationMainPanel.setPreferredSize(new Dimension(350, 200));

        JPanel staffMemberRegisterChildPanel = new JPanel();
        staffMemberRegisterChildPanel.setPreferredSize(new Dimension(350, 200));
        staffMemberRegisterChildPanel.setLayout(new BoxLayout(staffMemberRegisterChildPanel, BoxLayout.Y_AXIS));

        JTextField userNameTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton backButton = new JButton("Back");
        JButton registerButton = new JButton("Register");
        Dimension buttonDimension = new Dimension(200, 35);

        // Registration for customer
        registerButton.addActionListener(actionEvent -> shopMainViewModel.registerButtonClickHandler(userNameTextField.getText(), passwordField.getPassword(), staffMemberRegistrationMainPanel));

        // Back to Menu
        backButton.addActionListener(actionEvent -> {

            if (uiBackListener != null) {
                uiBackListener.onReturn();
                staffMemberRegisterChildPanel.setVisible(false);
            }
        });

        registerButton.setPreferredSize(buttonDimension);
        backButton.setPreferredSize(buttonDimension);
        registerButton.setMaximumSize(buttonDimension);
        backButton.setMaximumSize(buttonDimension);

        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));
        staffMemberRegisterChildPanel.add(userNameTextField);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));
        staffMemberRegisterChildPanel.add(passwordField);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));
        staffMemberRegisterChildPanel.add(registerButton);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));
        staffMemberRegisterChildPanel.add(backButton);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));

        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        staffMemberRegistrationMainPanel.add(staffMemberRegisterChildPanel);

        staffMemberRegistrationMainPanel.setVisible(true);

        return staffMemberRegistrationMainPanel;
    }

    private JScrollPane articleListJScrollPane() {

        // Table
        String[] columns = {"Article number", "Article name", "Description", "Stock", "Price", "Bulk size"};
        String[][] data = shopMainViewModel.loadArticles();

        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        JTable table = new JTable(defaultTableModel);

        if (!ViewModelUtils.currentUserIsStaffMember())
            table.removeColumn(table.getColumnModel().getColumn(3));

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(defaultTableModel);
        table.setRowSorter(sorter);
//            table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
//            table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        return new JScrollPane(table);
    }

    private JScrollPane stockJScrollPane() {

        // Table
        String[] columns = {"id", "Day of year", "Number", "Old amount", "New amount", "Edited by User"};
        String[][] data = shopMainViewModel.loadStocks();

        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        JTable table = new JTable(defaultTableModel);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(defaultTableModel);
        table.setRowSorter(sorter);

        return new JScrollPane(table);
    }

    public void activateStockChangesPanel() {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = stockJScrollPane(), BorderLayout.CENTER);
        shopMainPanel.revalidate();
        shopMainPanel.repaint();
    }

    public void activateArticleListPanel() {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = articleListJScrollPane(), BorderLayout.CENTER);
        shopMainPanel.revalidate();
        shopMainPanel.repaint();
    }

    public void setUiBackListener(UIBackListener uiBackListener) {
        this.uiBackListener = uiBackListener;
    }
}
