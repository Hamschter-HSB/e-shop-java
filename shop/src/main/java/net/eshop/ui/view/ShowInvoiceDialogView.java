package net.eshop.ui.view;

import net.eshop.domain.BulkArticle;
import net.eshop.ui.viewmodel.ShowInvoiceDialogViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.logging.Logger;

public class ShowInvoiceDialogView extends JDialog{
    private static final Logger logger = Logger.getLogger(LoginAndRegistrationView.class.getName());

    private final ShowInvoiceDialogViewModel showInvoiceDialogViewModel;
    private final Frame parent;

    public ShowInvoiceDialogView(ShowInvoiceDialogViewModel showInvoiceDialogViewModel, Frame parent) {
        super(parent, "Invoice", true);
        this.showInvoiceDialogViewModel = showInvoiceDialogViewModel;
        this.parent = parent;
        setLayout(new BorderLayout());
    }

    public void openShowInvoiceDialog() {
        JPanel invoicepanel = new JPanel();
        invoicepanel.setLayout(new BoxLayout(invoicepanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("INVOICE", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        invoicepanel.add(title);
        invoicepanel.add(new JSeparator());
        // invoiceNumber ist eine zufällige 5-stellige Zahl zwischen 100000 und 999999
        int invoiceNumber = 100000 + (int)(Math.random() * 900000);
        JLabel invoiceNumberLabel = new JLabel("Rechnungs Nr: " + invoiceNumber);
        invoiceNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        invoicepanel.add(invoiceNumberLabel);

        JLabel thanks = new JLabel("Thanks for your Purchase!");
        thanks.setAlignmentX(Component.CENTER_ALIGNMENT);
        invoicepanel.add(thanks);

        invoicepanel.add(new JSeparator());

        // TODO Artikel

        Map<BulkArticle, Integer> articlesInBasket = showInvoiceDialogViewModel.getAndUpdateAllArticlesInShoppingBasketForUser();

        for (Map.Entry<BulkArticle, Integer> entry : articlesInBasket.entrySet()) {
            BulkArticle article = entry.getKey();
            int quantity = entry.getValue();
            String line = String.format("%s x%d  %s", article.getName(), quantity, article.getPrice());
            JLabel item = new JLabel(line);
            item.setAlignmentX(Component.CENTER_ALIGNMENT);
            invoicepanel.add(item);
        }

        invoicepanel.add(new JSeparator());

        JLabel totalLabel = new JLabel("<html><b>Total: " + showInvoiceDialogViewModel.getTotal() + "</b></html>");
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        invoicepanel.add(totalLabel);

        JLabel disclaimer = new JLabel("<html><i>*mit dem Kauf wird jegliche Garantie ausgeschlossen</i></html>");
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        invoicepanel.add(Box.createRigidArea(new Dimension(0, 10)));
        invoicepanel.add(disclaimer);

        JLabel footer = new JLabel("Hamschter Inc.");
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        invoicepanel.add(footer);

        add(invoicepanel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(actionEvent -> {
            showInvoiceDialogViewModel.updateStocksAndEmptyShoppingBasket();
            this.dispose();
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
