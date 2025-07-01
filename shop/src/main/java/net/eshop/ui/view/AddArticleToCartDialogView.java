package net.eshop.ui.view;

import net.eshop.domain.BulkArticle;
import net.eshop.ui.viewmodel.AddArticleToCartDialogViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class AddArticleToCartDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(AddArticleToCartDialogView.class.getName());

    private final AddArticleToCartDialogViewModel addArticleToCartDialogViewModel;
    private final Frame parent;

    public AddArticleToCartDialogView(AddArticleToCartDialogViewModel addArticleToCartDialogViewModel, Frame parent) {
        super(parent, "Add article to cart", true);
        this.addArticleToCartDialogViewModel = addArticleToCartDialogViewModel;
        this.parent = parent;
        setLayout(new GridLayout(3, 2));
    }

    public void openAddArticleToCartDialog(int articleId) {

        BulkArticle selectedBulkArticle = addArticleToCartDialogViewModel.loadBulkArticle(articleId);

        JLabel name = new JLabel("Name:");
        JLabel description = new JLabel("Description:");
        JLabel price = new JLabel("Price:");
        JLabel bulkSize = new JLabel("Bulk size:");
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, selectedBulkArticle.getStock() / selectedBulkArticle.getBulkSize(), 1);
        JSpinner amountSpinner = new JSpinner(model);

        add(name);
        add(new JLabel(selectedBulkArticle.getName()));

        add(description);
        add(new JLabel(selectedBulkArticle.getDescription()));

        add(price);
        add(new JLabel(String.valueOf(selectedBulkArticle.getPrice())));

        add(bulkSize);
        add(new JLabel(String.valueOf(selectedBulkArticle.getBulkSize())));

        add(amountSpinner);

        JButton addToCartButton = new JButton("Add to cart");
        addToCartButton.addActionListener(actionEvent -> {
            addArticleToCartDialogViewModel.addBulkArticleToCart(articleId, (Integer) amountSpinner.getValue());
            this.dispose();
        });

        add(addToCartButton);

        pack();
        setLocationRelativeTo(parent);

        this.setVisible(true);
    }

}
