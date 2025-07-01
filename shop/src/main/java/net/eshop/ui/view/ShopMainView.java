package net.eshop.ui.view;

import net.eshop.ui.events.AddArticleToCartListener;
import net.eshop.ui.events.UIBackListener;
import net.eshop.ui.viewmodel.ShopMainViewModel;
import net.eshop.ui.viewmodel.ViewModelUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.logging.Logger;

public class ShopMainView {

    private static final Logger logger = Logger.getLogger(ShopMainView.class.getName());

    private final ShopMainViewModel shopMainViewModel;
    private UIBackListener uiBackListener;
    private AddArticleToCartListener articleAddToCartListener;

    private final JPanel shopMainPanel = new JPanel(new BorderLayout());
    private final JPanel headerPanel = new JPanel(new BorderLayout());

    //TODO Put both in a class
    private JScrollPane currentCenterPane;
    private JButton currentCenterPaneButton;

    public ShopMainView(ShopMainViewModel shopMainViewModel) {
        this.shopMainViewModel = shopMainViewModel;
    }

    public JPanel shop() {

        JLabel storeLabel = new JLabel("STORE", SwingConstants.CENTER);
        storeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        headerPanel.add(storeLabel, BorderLayout.CENTER);

        currentCenterPaneButton = new JButton("Cart");
        currentCenterPaneButton.addActionListener(actionEvent -> activateShoppingCartPanel());

        if (ViewModelUtils.currentUserIsStaffMember())
            currentCenterPaneButton.setVisible(false);

        headerPanel.add(currentCenterPaneButton, BorderLayout.EAST);
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
            table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRendererForArticleList());
            table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditorForArticleList());
        } else
            table.removeColumn(table.getColumnModel().getColumn(6));

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
        String[] columns = {"Article number", "Article name", "Description", "Price", "Amount", "Remove"};
        String[][] data = shopMainViewModel.loadShoppingCart();

        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        JTable table = new JTable(defaultTableModel);

        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRendererForCart());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditorForCart());

        return new JScrollPane(table);
    }

    private JButton backToArticleListButton() {

        JButton articles = new JButton("Articles");

        if (ViewModelUtils.currentUserIsStaffMember())
            articles.setVisible(false);

        return articles;
    }

    private JButton cartButton() {

        JButton cart = new JButton("Cart");

        if (ViewModelUtils.currentUserIsStaffMember())
            cart.setVisible(false);

        return cart;
    }

    public void activateStockChangesPanel() {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = stockJScrollPane(), BorderLayout.CENTER);
        shopMainPanel.revalidate();
        shopMainPanel.repaint();

        headerPanel.remove(currentCenterPaneButton);
        headerPanel.add(currentCenterPaneButton = cartButton(), BorderLayout.EAST);
        headerPanel.revalidate();
        headerPanel.repaint();

        currentCenterPaneButton.addActionListener(actionEvent -> activateShoppingCartPanel());
    }

    public void activateArticleListPanelAfterLogIn() {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = articleListJScrollPane(), BorderLayout.CENTER);
        shopMainPanel.revalidate();
        shopMainPanel.repaint();

        //TODO Not robust. We should have a state, that defines if a staff member is logged in. If not, The current user is not a staff member and the code passes.
        // Login and Logout should not be bi-directional to each other. Instead they should be seperated and stateful.
        if (ViewModelUtils.currentUserIsStaffMember())
            currentCenterPaneButton.setVisible(false);
        else
            currentCenterPaneButton.setVisible(true);
    }

    public void activateArticleListPanel() {
        activateArticleListPanelAfterLogIn();

        headerPanel.remove(currentCenterPaneButton);
        headerPanel.add(currentCenterPaneButton = cartButton(), BorderLayout.EAST);
        headerPanel.revalidate();
        headerPanel.repaint();

        currentCenterPaneButton.addActionListener(actionEvent -> activateShoppingCartPanel());
    }

    public void activateShoppingCartPanel() {
        shopMainPanel.remove(currentCenterPane);
        shopMainPanel.add(currentCenterPane = shoppingCartPane(), BorderLayout.CENTER);
        shopMainPanel.revalidate();
        shopMainPanel.repaint();

        headerPanel.remove(currentCenterPaneButton);
        headerPanel.add(currentCenterPaneButton = backToArticleListButton(), BorderLayout.EAST);
        headerPanel.revalidate();
        headerPanel.repaint();

        currentCenterPaneButton.addActionListener(actionEvent -> activateArticleListPanel());
    }

    static class ButtonRendererForArticleList extends JButton implements TableCellRenderer {
        public ButtonRendererForArticleList() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
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

    static class ButtonRendererForCart extends JButton implements TableCellRenderer {
        public ButtonRendererForCart() {
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

            button.addActionListener(actionEvent -> {
                fireEditingStopped();
                shopMainViewModel.removeArticleFromCart(articleID);
                activateShoppingCartPanel();
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            int modelRow = table.convertRowIndexToModel(row);
            articleID = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(row);
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

    public void setUiBackListener(UIBackListener uiBackListener) {
        this.uiBackListener = uiBackListener;
    }

    public void setAddArticleToCartListener(AddArticleToCartListener addArticleToCartListener) {
        articleAddToCartListener = addArticleToCartListener;
    }
}
