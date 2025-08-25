package de.eshop.client.ui.view;

import de.eshop.shared.domain.BulkArticle;
import de.eshop.client.ui.viewmodel.StockHistoryDialogViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.logging.Logger;

public class StockHistoryDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(StockHistoryDialogView.class.getName());

    private final StockHistoryDialogViewModel stockHistoryDialogViewModel;
    private final Frame parent;

    public StockHistoryDialogView(StockHistoryDialogViewModel stockHistoryDialogViewModel, Frame parent) {
        super(parent, "Stock change for past 30 days", true);
        this.stockHistoryDialogViewModel = stockHistoryDialogViewModel;
        this.parent = parent;
        setLayout(new BorderLayout());
    }

    public void openStockHistoryDialog() {

        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        Dimension dimension = new Dimension(150, 30);

        HashMap<Integer, BulkArticle> allArticlesHashMap = stockHistoryDialogViewModel.getArticleNumberToArticle();
        Integer[] allArticleIds = allArticlesHashMap.keySet().toArray(new Integer[0]);

        JComboBox<Integer> articleID = new JComboBox<>();
        articleID.setModel(new DefaultComboBoxModel<>(allArticleIds));
        articleID.setPreferredSize(dimension);

        BulkArticle selectedItem = allArticlesHashMap.get(articleID.getSelectedItem());
        JLabel articleName = new JLabel(selectedItem.getName());
        articleName.setPreferredSize(dimension);

        JLabel articleLabel = new JLabel("Article:");
        articleLabel.setPreferredSize(new Dimension(50, 30));

        articleID.addActionListener(actionEvent -> {
            BulkArticle newSelectedItem = allArticlesHashMap.get(articleID.getSelectedItem());
            articleName.setText(newSelectedItem.getName());
        });

        mainPanel.add(articleLabel);
        mainPanel.add(articleID);
        mainPanel.add(new JLabel(" "));
        mainPanel.add(articleName);

        JButton stockHistoryButton = new JButton("Show history");
        stockHistoryButton.setPreferredSize(dimension);
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
        mainPanel.add(stockHistoryButton);

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
