package de.eshop.client.ui.view;

import de.eshop.client.ui.events.AddArticleToCartListener;
import de.eshop.client.ui.viewmodel.ShopMainViewModel;
import de.eshop.client.ui.viewmodel.ViewModelUtils;
import de.eshop.shared.domain.exceptions.StaffMemberNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShopMainView {

    private static final Logger logger = Logger.getLogger(ShopMainView.class.getName());

    private final ShopMainViewModel shopMainViewModel;
    private Runnable uiBackListener;
    private AddArticleToCartListener articleAddToCartListener;

    private final JPanel shopMainPanel = new JPanel(new BorderLayout());
    private final JPanel headerPanel = new JPanel(new BorderLayout(0, 20));
    private final JPanel buttonPanel = new JPanel(new GridBagLayout());

    private final GridBagConstraints gbc = new GridBagConstraints();
    private final Dimension currentCenterPaneButtonDimension = new Dimension(100, 40);

    private JScrollPane currentCenterPane;
    private JButton currentCenterPaneButton;
    private JLabel currentCenterPaneLabel;
    private boolean currentSceneIsArticleView = true;

    private final JTextField searchField = new JTextField(10);
    private final JButton searchButton = new JButton("Search");
    private final JPanel searchPanel = new JPanel();

    public ShopMainView(ShopMainViewModel shopMainViewModel) {
        this.shopMainViewModel = shopMainViewModel;
    }

    public JPanel shop() {

        currentCenterPaneLabel = new JLabel("STORE", SwingConstants.CENTER);
        currentCenterPaneLabel.setFont(new Font("Arial", Font.BOLD, 40));
        headerPanel.add(currentCenterPaneLabel, BorderLayout.CENTER);

        currentCenterPaneButton = new JButton("Cart");
        currentCenterPaneButton.setPreferredSize(currentCenterPaneButtonDimension);
        currentCenterPaneButton.addActionListener(actionEvent -> activateShoppingCartPanel());

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 0, 0); // padding at top
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(currentCenterPaneButton, gbc);

        if (ViewModelUtils.currentUserIsStaffMember())
            currentCenterPaneButton.setVisible(false);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        headerPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        shopMainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar (left)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        JPanel searchPanel = new JPanel();
        JLabel searchLabel = new JLabel("Article Search", SwingConstants.CENTER);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 25));

        sidebar.add(searchLabel);
        sidebar.add(Box.createVerticalStrut(15));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        sidebar.add(searchPanel);

        sidebar.add(Box.createVerticalGlue());
        JButton logoutButton = new JButton("Logout");
        sidebar.add(logoutButton);

        logoutButton.addActionListener(actionEvent -> {
            shopMainViewModel.logOutCurrentUser();
            searchField.setText("");
        });
        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(new JLabel("Hamschter Inc."));

        searchButton.addActionListener(actionEvent -> dislaySearchResults(searchField.getText()));

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
        staffMemberRegisterChildPanel.setLayout(new BoxLayout(staffMemberRegisterChildPanel, BoxLayout.Y_AXIS));

        Dimension textFieldDimension = new Dimension(400, 35);

        JLabel staffMemberRegistrationLabel = new JLabel("Staff Member Registration", SwingConstants.CENTER);
        staffMemberRegisterChildPanel.add(staffMemberRegistrationLabel);
        staffMemberRegistrationLabel.setFont(new Font("Arial", Font.BOLD, 30));

        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(40));

        // Name-Field
        JPanel namePanel = new JPanel(new GridBagLayout());
        namePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.anchor = GridBagConstraints.WEST;
        gbc1.insets = new Insets(0, 0, 5, 0);
        namePanel.add(new JLabel("Name:"), gbc1);

        gbc1.gridy++;
        gbc1.fill = GridBagConstraints.HORIZONTAL;
        gbc1.weightx = 1.0;
        JTextField userNameTextField = new JTextField(20);
        userNameTextField.setPreferredSize(textFieldDimension);
        namePanel.add(userNameTextField, gbc1);
        staffMemberRegisterChildPanel.add(namePanel);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));

        // Password-Field
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.insets = new Insets(0, 0, 5, 0);
        passwordPanel.add(new JLabel("Password:"), gbc2);

        gbc2.gridy++;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(textFieldDimension);
        passwordPanel.add(passwordField, gbc2);
        staffMemberRegisterChildPanel.add(passwordPanel);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));

        // Buttons nebeneinander erstellen
        Dimension buttonDimension = new Dimension(200, 35);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        registerButton.setPreferredSize(buttonDimension);
        backButton.setPreferredSize(buttonDimension);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        staffMemberRegisterChildPanel.add(buttonPanel);

        // Registration for customer Button Function
        registerButton.addActionListener(actionEvent -> {
            try {
                shopMainViewModel.registerButtonClickHandler(userNameTextField, passwordField, staffMemberRegistrationMainPanel);
            } catch (StaffMemberNotFoundException exception) {
                logger.log(Level.SEVERE, "An Error has occurred: No staff member with user id " + exception.getUserID() + " found!");
                System.out.println("Could not find staff member with id: " + exception.getUserID() + ".");
            }
        });

        // Back to Menu Button Function
        backButton.addActionListener(actionEvent -> {
            userNameTextField.setText("");
            passwordField.setText("");
            if (uiBackListener != null)
                uiBackListener.run();
        });

        staffMemberRegistrationMainPanel.add(staffMemberRegisterChildPanel);

        staffMemberRegistrationMainPanel.setVisible(true);

        return staffMemberRegistrationMainPanel;
    }

    private JScrollPane articleListJScrollPane() {

        String[] columns = {"Article number", "Article name", "Description", "Stock", "Price", "Bulk size", "Add to cart"};
        String[][] data = shopMainViewModel.loadArticles();

        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        JTable table = new JTable(defaultTableModel);

        if (!ViewModelUtils.currentUserIsStaffMember()) {
            table.removeColumn(table.getColumnModel().getColumn(3));
            table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditorForArticleList());
        } else
            table.removeColumn(table.getColumnModel().getColumn(6));

        //delete any rows that are empty due to a non-existent stock
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int targetColumn = 1;

        for (int row = model.getRowCount() - 1; row >= 0; row--) {
            Object value = model.getValueAt(row, targetColumn);
            if (value == null || value.toString().trim().isEmpty()) {
                model.removeRow(row);
            }
        }

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(defaultTableModel);
        table.setRowSorter(sorter);

        return new JScrollPane(table);
    }

    private JScrollPane searchedArticlePane(String searchTerm) {

        String[] columns = {"Article number", "Article name", "Description", "Stock", "Price", "Bulk size", "Add to cart"};
        String[][] data = shopMainViewModel.loadSearchedArticles(searchTerm);

        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        JTable table = new JTable(defaultTableModel);

        if (!ViewModelUtils.currentUserIsStaffMember()) {
            table.removeColumn(table.getColumnModel().getColumn(3));
            table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditorForArticleList());
        } else
            table.removeColumn(table.getColumnModel().getColumn(6));

        //delete any rows that are empty due to a non-existent stock
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int targetColumn = 1;

        for (int row = model.getRowCount() - 1; row >= 0; row--) {
            Object value = model.getValueAt(row, targetColumn);
            if (value == null || value.toString().trim().isEmpty()) {
                model.removeRow(row);
            }
        }

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(defaultTableModel);
        table.setRowSorter(sorter);

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

    private JScrollPane shoppingCartPane() {

        // Table
        String[] columns = {"Article number", "Article name", "Description", "Price", "Amount", "Edit", "Remove"};
        String[][] data = shopMainViewModel.loadShoppingCart();

        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 5;
            }
        };

        JTable table = new JTable(defaultTableModel);
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditorForArticleList());
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditorForCart());

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(defaultTableModel);
        table.setRowSorter(sorter);

        return new JScrollPane(table);
    }

    private JButton backToArticleListButton() {

        JButton articles = new JButton("Articles");

        if (ViewModelUtils.currentUserIsStaffMember())
            articles.setVisible(false);

        articles.setPreferredSize(currentCenterPaneButtonDimension);

        return articles;
    }

    private JButton cartButton() {

        JButton cart = new JButton("Cart");

        if (ViewModelUtils.currentUserIsStaffMember())
            cart.setVisible(false);

        cart.setPreferredSize(currentCenterPaneButtonDimension);

        return cart;
    }

    public void activateStockChangesPanel() {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = stockJScrollPane(), BorderLayout.CENTER);
        shopMainPanel.revalidate();
        shopMainPanel.repaint();

        headerPanel.remove(currentCenterPaneLabel);
        headerPanel.add(currentCenterPaneLabel = new JLabel("STOCK CHANGES", SwingConstants.CENTER), BorderLayout.CENTER);
        currentCenterPaneLabel.setFont(new Font("Arial", Font.BOLD, 40));
        headerPanel.revalidate();
        headerPanel.repaint();

        currentSceneIsArticleView = false;

        currentCenterPaneButton.addActionListener(actionEvent -> activateShoppingCartPanel());
    }

    public void activateArticleListPanelAfterLogIn() {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = articleListJScrollPane(), BorderLayout.CENTER);
        headerPanel.remove(currentCenterPaneLabel);
        headerPanel.add(currentCenterPaneLabel = new JLabel("STORE", SwingConstants.CENTER), BorderLayout.CENTER);
        currentCenterPaneLabel.setFont(new Font("Arial", Font.BOLD, 40));
        buttonPanel.remove(currentCenterPaneButton);
        buttonPanel.add(currentCenterPaneButton = cartButton(), gbc);
        currentCenterPaneButton.addActionListener(actionEvent -> activateShoppingCartPanel());
        shopMainPanel.revalidate();
        shopMainPanel.repaint();

        if (ViewModelUtils.currentUserIsStaffMember())
            currentCenterPaneButton.setVisible(false);
        else
            currentCenterPaneButton.setVisible(true);
    }

    public synchronized void activateArticleListPanel() {
        activateArticleListPanelAfterLogIn();

        currentSceneIsArticleView = true;

        currentCenterPaneButton.addActionListener(actionEvent -> activateShoppingCartPanel());
    }

    public void dislaySearchResults(String searchTerm) {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = searchedArticlePane(searchTerm), BorderLayout.CENTER);
        headerPanel.remove(currentCenterPaneLabel);
        headerPanel.add(currentCenterPaneLabel = new JLabel("STORE", SwingConstants.CENTER), BorderLayout.CENTER);
        currentCenterPaneLabel.setFont(new Font("Arial", Font.BOLD, 40));
        buttonPanel.remove(currentCenterPaneButton);
        buttonPanel.add(currentCenterPaneButton = cartButton(), gbc);
        currentCenterPaneButton.addActionListener(actionEvent -> activateShoppingCartPanel());
        shopMainPanel.revalidate();
        shopMainPanel.repaint();

        if (ViewModelUtils.currentUserIsStaffMember())
            currentCenterPaneButton.setVisible(false);
        else
            currentCenterPaneButton.setVisible(true);

    }

    public void activateShoppingCartPanel() {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = shoppingCartPane(), BorderLayout.CENTER);
        shopMainPanel.revalidate();
        shopMainPanel.repaint();

        buttonPanel.remove(currentCenterPaneButton);
        buttonPanel.add(currentCenterPaneButton = backToArticleListButton(), gbc);
        headerPanel.remove(currentCenterPaneLabel);
        headerPanel.add(currentCenterPaneLabel = new JLabel("SHOPPING CART", SwingConstants.CENTER), BorderLayout.CENTER);
        currentCenterPaneLabel.setFont(new Font("Arial", Font.BOLD, 40));
        headerPanel.revalidate();
        headerPanel.repaint();

        currentSceneIsArticleView = false;

        currentCenterPaneButton.addActionListener(actionEvent -> activateArticleListPanel());
    }

    class ButtonEditorForArticleList extends DefaultCellEditor {

        private final JButton button;
        private String label;
        private boolean clicked;
        private int articleID;

        public ButtonEditorForArticleList() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> {
                fireEditingStopped();
                if (articleAddToCartListener != null)
                    articleAddToCartListener.onOpenAddArticleToCartDialogView(articleID);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            int modelRow = table.convertRowIndexToModel(row);
            articleID = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
            return button;
        }

        public Object getCellEditorValue() {
            clicked = false;
            return label;
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditorForCart extends DefaultCellEditor {

        private final JButton button;
        private String label;
        private boolean clicked;
        private int articleID;

        public ButtonEditorForCart() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;

            int modelRow = table.convertRowIndexToModel(row);
            articleID = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                clicked = false;

                shopMainViewModel.removeArticleFromCart(articleID);

                SwingUtilities.invokeLater(() -> activateShoppingCartPanel());
            }
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public void setUiBackListener(Runnable uiBackListener) {
        this.uiBackListener = uiBackListener;
    }

    public void setAddArticleToCartListener(AddArticleToCartListener addArticleToCartListener) {
        articleAddToCartListener = addArticleToCartListener;
    }

    public boolean currentSceneIsArticleView(){
        return currentSceneIsArticleView;
    }
}
