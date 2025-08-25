package de.eshop.client.ui.view;

import de.eshop.client.ui.DialogUtils;
import de.eshop.client.ui.viewmodel.AddArticleDialogViewModel;
import de.eshop.shared.domain.exceptions.NoMultipleOfBulkException;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddArticleDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(AddArticleDialogView.class.getName());

    private final AddArticleDialogViewModel addArticleDialogViewModel;
    private final Frame parent;

    public AddArticleDialogView(AddArticleDialogViewModel addArticleDialogViewModel, Frame parent) {
        super(parent, "Create article", true);
        this.addArticleDialogViewModel = addArticleDialogViewModel;
        this.parent = parent;
        setLayout(new BorderLayout());
    }

    public void openAddArticleDialog() {
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        Dimension dimension = new Dimension(150, 30);

        JPanel leftColumnPanel = new JPanel(new GridLayout(3,2, 5, 5));
        JPanel rightColumnPanel = new JPanel(new GridLayout(3,2, 5, 5));


        JTextField name = new JTextField();
        JTextField description = new JTextField();
        JTextField stock = new JTextField();
        JTextField price = new JTextField();
        JTextField bulkSize = new JTextField();

        name.setPreferredSize(dimension);
        description.setPreferredSize(dimension);
        stock.setPreferredSize(dimension);
        price.setPreferredSize(dimension);
        bulkSize.setPreferredSize(dimension);

        leftColumnPanel.add(new JLabel("Name:"));
        leftColumnPanel.add(name);
        leftColumnPanel.add(new JLabel("Bulk size:"));
        leftColumnPanel.add(bulkSize);
        leftColumnPanel.add(new JLabel("Stock:"));
        leftColumnPanel.add(stock);


        rightColumnPanel.add(new JLabel("Description:"));
        rightColumnPanel.add(description);
        rightColumnPanel.add(new JLabel("Price:"));
        rightColumnPanel.add(price);
        rightColumnPanel.add(new JLabel());



        JButton createButton = new JButton("Create article");
        createButton.addActionListener(actionEvent -> {
            try {
                addArticleDialogViewModel.createBulkArticle(name.getText(), description.getText(), Integer.parseInt(stock.getText()), Double.parseDouble(price.getText()), Integer.parseInt(bulkSize.getText()));
                this.dispose();
            } catch (NoMultipleOfBulkException exception) {
                logger.log(Level.SEVERE, "An Error has occurred: Bulk size for article " + exception.getArticleNumber() + " is " + exception.getActualBulkSize() + ", which is not a multiple of " + exception.getStockSize() + "!");
                System.out.println("Bulk size for article " + exception.getArticleNumber() + " is " + exception.getActualBulkSize() + ", which is not a multiple of " + exception.getStockSize() + "!");
                DialogUtils.stockIsNotMultipleOfBulkSize(mainPanel);
            } catch (NumberFormatException exception) {

                logger.log(Level.SEVERE, "An Error has occurred: Expected Price, bulk size and stock to be integer values.");
                JOptionPane.showMessageDialog(this,
                        "Wrong input for integer. Incorrect inputs for one or more input fields.",
                        "Wrong input",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        createButton.setPreferredSize(dimension);
        rightColumnPanel.add(createButton);


        mainPanel.add(leftColumnPanel);
        mainPanel.add(new JLabel("   "));
        mainPanel.add(rightColumnPanel);

        add(new JLabel("    "), BorderLayout.NORTH);
        add(new JLabel("    "), BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        add(new JLabel("    "), BorderLayout.EAST);
        add(new JLabel("    "), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);

        this.setVisible(true);
    }

}
