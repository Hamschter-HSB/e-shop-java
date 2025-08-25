package de.eshop.client.ui.view;

import de.eshop.client.ui.viewmodel.ShowInvoiceDialogViewModel;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.exceptions.ArticleOutOfStockException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class ShowInvoiceDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(ShowInvoiceDialogView.class.getName());

    private final ShowInvoiceDialogViewModel showInvoiceDialogViewModel;
    private final Frame parent;

    public ShowInvoiceDialogView(ShowInvoiceDialogViewModel showInvoiceDialogViewModel, Frame parent) {
        super(parent, "Invoice", true);
        this.showInvoiceDialogViewModel = showInvoiceDialogViewModel;
        this.parent = parent;
        setLayout(new BorderLayout());
    }

    public void openShowInvoiceDialog() throws ArticleOutOfStockException {
        JPanel invoicepanel = new JPanel();
        invoicepanel.setLayout(new BoxLayout(invoicepanel, BoxLayout.Y_AXIS));

        // Invoice Title

        JLabel title = new JLabel("INVOICE", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(title);
        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(new JSeparator());

        // Invoice Metadata
        // invoiceNumber ist eine zufällige 5-stellige Zahl zwischen 100000 und 999999
        int invoiceNumber = 100000 + (int) (Math.random() * 900000);
        LocalDateTime now = LocalDateTime.now();
        JLabel invoiceNumberLabel = new JLabel("   Invoice Nr.: " + invoiceNumber + "   ");
        JLabel invoiceDateTimeLabel = new JLabel("   at " + now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss   ")));
        invoiceNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        invoiceDateTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel thanks = new JLabel("   Thank you for your Purchase, " + showInvoiceDialogViewModel.getCurrentUserName() +  "!   ");
        thanks.setAlignmentX(Component.CENTER_ALIGNMENT);

        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(invoiceNumberLabel);
        invoicepanel.add(invoiceDateTimeLabel);
        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(thanks);
        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(new JSeparator());

        // Invoice bought Articles

        Map<BulkArticle, Integer> articlesInBasket = showInvoiceDialogViewModel.getAllArticlesInShoppingBasketForUser();

        if (articlesInBasket.isEmpty()) {

            JOptionPane.showMessageDialog(invoicepanel,
                    "You have no articles in your shopping basket. Please add some articles first and click \"buy\".",
                    "Shopping basket is empty!",
                    JOptionPane.INFORMATION_MESSAGE);

            logger.info("No items in shopping basket.");
            return;
        }

        invoicepanel.add(new JLabel(" "));
        for (Map.Entry<BulkArticle, Integer> entry : articlesInBasket.entrySet()) {
            BulkArticle article = entry.getKey();
            int quantity = entry.getValue();
            String line = String.format(Locale.US, "%-20s x%5d %10.2f", article.getName(), quantity, article.getPrice());
            JLabel item = new JLabel("   "+line + " €   ");
            item.setFont(new Font("Monospaced", Font.PLAIN, 12));
            item.setAlignmentX(Component.CENTER_ALIGNMENT);
            invoicepanel.add(item);
        }
        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(new JSeparator());

        //Total and Disclaimer
        //TotalPrice has to be defined BEFORE updateStocksAndEmptyShoppingBasketForNotExceededArticleStock(), since updateStocksAndEmptyShoppingBasketForNotExceededArticleStock()
        //removes all articles in a shopping basket.
        String totalPrice = showInvoiceDialogViewModel.totalPriceOfSoppingBasket();
        String articleWithExceededStock = showInvoiceDialogViewModel.updateStocksAndEmptyShoppingBasketForNotExceededArticleStock();

        if (!articleWithExceededStock.isEmpty()) {

            JOptionPane.showMessageDialog(invoicepanel,
                    "One ore more articles are exceeded and REMOVED from your shopping basket. These are: " + articleWithExceededStock,
                    "Article stock exceeded!",
                    JOptionPane.INFORMATION_MESSAGE);

            logger.info("One ore more articles are exceeded.");
            throw new ArticleOutOfStockException("One ore more articles are exceeded and REMOVED from your shopping basket. These are: " + articleWithExceededStock, articleWithExceededStock);
        }

        JLabel totalLabel = new JLabel("<html><b>Your total is:   " + totalPrice + " €</b></html>", SwingConstants.CENTER);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel disclaimer = new JLabel("<html><i>*This purchase excludes any warranty.</i></html>");
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        disclaimer.setHorizontalAlignment(SwingConstants.CENTER);


        JLabel footer = new JLabel("-- Hamschter Inc. --");
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(totalLabel);
        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(disclaimer);
        invoicepanel.add(new JLabel(" "));
        invoicepanel.add(footer);

        this.add(invoicepanel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(actionEvent -> this.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
