package net.eshop.ui.view;

import net.eshop.ui.viewmodel.AddArticleDialogViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class AddArticleDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(LoginAndRegistrationView.class.getName());

    private final AddArticleDialogViewModel addArticleDialogViewModel;
    private final Frame parent;

    public AddArticleDialogView(AddArticleDialogViewModel addArticleDialogViewModel, Frame parent) {
        super(parent, "Create article", true);
        this.addArticleDialogViewModel = addArticleDialogViewModel;
        this.parent = parent;
        setLayout(new GridLayout(3, 2));
    }

    public void openAddArticleDialog() {

        JTextField name = new JTextField();
        JTextField description = new JTextField();
        JTextField stock = new JTextField();
        JTextField price = new JTextField();
        JTextField bulkSize = new JTextField();

        add(new JLabel("Name:"));
        add(name);

        add(new JLabel("Description:"));
        add(description);

        add(new JLabel("Stock:"));
        add(stock);

        add(new JLabel("Price:"));
        add(price);

        add(new JLabel("Bulk size:"));
        add(bulkSize);

        add(new JLabel());
        JButton createButton = new JButton("Create article");
        createButton.addActionListener(actionEvent -> {
            addArticleDialogViewModel.createBulkArticle(name.getText(), description.getText(), Integer.parseInt(stock.getText()), Integer.parseInt(price.getText()), Integer.parseInt(bulkSize.getText()));
            this.dispose();
        });
        add(createButton);

        pack();
        setLocationRelativeTo(parent);

        this.setVisible(true);
    }

}
