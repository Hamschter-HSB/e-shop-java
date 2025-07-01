package net.eshop.ui.view;

import net.eshop.domain.BulkArticle;
import net.eshop.ui.viewmodel.StockHistoryDialogViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.logging.Logger;

public class StockHistoryDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(LoginAndRegistrationView.class.getName());

    private final StockHistoryDialogViewModel stockHistoryDialogViewModel;
    private final Frame parent;

    public StockHistoryDialogView(StockHistoryDialogViewModel stockHistoryDialogViewModel, Frame parent) {
        super(parent, "Stock change for past 30 days", true);
        this.stockHistoryDialogViewModel = stockHistoryDialogViewModel;
        this.parent = parent;
        setLayout(new GridLayout(5, 5));
    }

    public void openStockHistoryDialog() {

        HashMap<Integer, BulkArticle> allArticlesHashMap = stockHistoryDialogViewModel.getArticleNumberToArticle();
        Integer[] allArticleIds = allArticlesHashMap.keySet().toArray(new Integer[0]);

        JComboBox<Integer> articleID = new JComboBox<>();
        articleID.setModel(new DefaultComboBoxModel<>(allArticleIds));

        BulkArticle selectedItem = allArticlesHashMap.get(articleID.getSelectedItem());
        JLabel articleName = new JLabel(selectedItem.getName());

        add(new JLabel("Article:"));
        add(articleID);
        add(articleName);

        JButton stockHistoryButton = new JButton("Show history");
        stockHistoryButton.addActionListener(actionEvent -> {
            JDialog stockHistoryDialog = new JDialog(this, "Stock of past 30 days", true);
            stockHistoryDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            stockHistoryDialog.add(stockHistoryDialogViewModel.createGraph((Integer) articleID.getSelectedItem()));
            stockHistoryDialog.pack();
            stockHistoryDialog.setLocationRelativeTo(parent);
            stockHistoryDialog.setVisible(true);
            this.revalidate();
            this.repaint();
        });
        add(stockHistoryButton);


        pack();
        setLocationRelativeTo(parent);

        this.setVisible(true);
    }
}
