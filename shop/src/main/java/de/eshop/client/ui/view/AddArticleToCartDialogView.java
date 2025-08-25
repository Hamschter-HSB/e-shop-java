package de.eshop.client.ui.view;

import de.eshop.shared.domain.BulkArticle;
import de.eshop.client.ui.viewmodel.AddArticleToCartDialogViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.logging.Logger;

public class AddArticleToCartDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(AddArticleToCartDialogView.class.getName());

    private final AddArticleToCartDialogViewModel addArticleToCartDialogViewModel;
    private final Frame parent;

    public AddArticleToCartDialogView(AddArticleToCartDialogViewModel addArticleToCartDialogViewModel, Frame parent) {
        super(parent, "Add article to cart", true);
        this.addArticleToCartDialogViewModel = addArticleToCartDialogViewModel;
        this.parent = parent;
        setLayout(new BorderLayout());
    }

    public void openAddArticleToCartDialog(int articleId) {

        BulkArticle selectedBulkArticle = addArticleToCartDialogViewModel.loadBulkArticle(articleId);

        JPanel gridPanel = new JPanel(new GridLayout(6, 2));

        JLabel name = new JLabel("Name:");
        JLabel description = new JLabel("Description:");
        JLabel price = new JLabel("Price:");
        JLabel bulkSize = new JLabel("Bulk size:");
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, selectedBulkArticle.getStock() / selectedBulkArticle.getBulkSize(), 1);
        JSpinner amountSpinner = new JSpinner(model);

        gridPanel.add(name);
        gridPanel.add(new JLabel(selectedBulkArticle.getName()));

        gridPanel.add(description);
        gridPanel.add(new JLabel(selectedBulkArticle.getDescription()));

        gridPanel.add(price);
        gridPanel.add(new JLabel(String.format(Locale.US, "%.2f €", selectedBulkArticle.getPrice())));

        gridPanel.add(bulkSize);
        gridPanel.add(new JLabel(String.valueOf(selectedBulkArticle.getBulkSize())));

        gridPanel.add(new Label("   "));
        gridPanel.add(new Label("   "));

        gridPanel.add(amountSpinner);

        JButton addToCartButton = new JButton("Add to cart");
        addToCartButton.addActionListener(actionEvent -> {
            addArticleToCartDialogViewModel.addBulkArticleToCart(articleId, (Integer) amountSpinner.getValue());
            this.dispose();
        });

        gridPanel.add(addToCartButton);



        add(new Label("   "),BorderLayout.WEST);
        add(new Label("   "),BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(new Label("   "),BorderLayout.SOUTH);
        add(new Label("   "),BorderLayout.EAST);


        pack();
        setLocationRelativeTo(parent);

        this.setVisible(true);
    }

}
