package de.eshop.client.ui.view;

import de.eshop.client.ui.DialogUtils;
import de.eshop.client.ui.viewmodel.EditArticleStockDialogViewModel;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.exceptions.ArticleNotFoundException;
import de.eshop.shared.domain.exceptions.NoMultipleOfBulkException;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditArticleStockDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(EditArticleStockDialogView.class.getName());

    private final EditArticleStockDialogViewModel editArticleStockDialogViewModel;
    private final Frame parent;

    public EditArticleStockDialogView(EditArticleStockDialogViewModel editArticleStockDialogViewModel, Frame parent) {
        super(parent, "Edit article stock", true);
        this.editArticleStockDialogViewModel = editArticleStockDialogViewModel;
        this.parent = parent;
        setLayout(new BorderLayout());
    }

    public void openEditArticleStockDialog() {

        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        Dimension dimension = new Dimension(150, 30);
        Dimension dimensionLeft = new Dimension(50, 30);

        JPanel leftColumnPanel = new JPanel(new GridLayout(2,1, 5, 15));
        JPanel rightColumnPanel = new JPanel(new GridLayout(2,2, 15, 15));

        HashMap<Integer, BulkArticle> allArticlesHashMap = editArticleStockDialogViewModel.getArticleNumberToArticle();
        Integer[] allArticleIds = allArticlesHashMap.keySet().toArray(new Integer[0]);

        JComboBox<Integer> articleID = new JComboBox<>();
        articleID.setModel(new DefaultComboBoxModel<>(allArticleIds));

        BulkArticle selectedItem = allArticlesHashMap.get(articleID.getSelectedItem());
        JLabel articleName = new JLabel(selectedItem.getName());

        SpinnerNumberModel model = new SpinnerNumberModel(selectedItem.getStock(), 0, Integer.MAX_VALUE, selectedItem.getBulkSize());
        JSpinner stock = new JSpinner(model);

        articleID.addActionListener(actionEvent -> {
            BulkArticle newSelectedItem = allArticlesHashMap.get(articleID.getSelectedItem());
            model.setValue(newSelectedItem.getStock());
            articleName.setText(newSelectedItem.getName());
        });

        JLabel articleLabel = new JLabel("Article:");
        JLabel stockLabel = new JLabel("Stock:");
        articleLabel.setPreferredSize(dimensionLeft);
        stock.setPreferredSize(dimensionLeft);

        articleID.setPreferredSize(dimension);
        articleName.setPreferredSize(dimension);
        stock.setPreferredSize(dimension);


        leftColumnPanel.add(articleLabel);
        leftColumnPanel.add(stockLabel);

        rightColumnPanel.add(articleID);
        rightColumnPanel.add(articleName);


        rightColumnPanel.add(stock);

        JButton updateStockButton = new JButton("Update Stock");
        updateStockButton.setPreferredSize(dimension);
        updateStockButton.addActionListener(actionEvent -> {
            try {
                editArticleStockDialogViewModel.updateArticleStockAndCreateStockChange((Integer) articleID.getSelectedItem(), (Integer) stock.getValue());
                this.dispose();
            } catch (ArticleNotFoundException exception) {
                logger.log(Level.SEVERE, "An Error has occurred: Article " + exception.getArticleNumber() + " was not found!");
                System.out.println("Could not find article with number: " + exception.getArticleNumber() + ".");
            } catch (NoMultipleOfBulkException exception) {
                logger.log(Level.SEVERE, "An Error has occurred: Bulk size for article " + exception.getArticleNumber() + " is " + exception.getActualBulkSize() + ", which is not a multiple of " + exception.getStockSize() + "!");
                System.out.println("Bulk size for article " + exception.getArticleNumber() + " is " + exception.getActualBulkSize() + ", which is not a multiple of " + exception.getStockSize() + "!");
                DialogUtils.stockIsNotMultipleOfBulkSize(mainPanel);
            }
        });
        rightColumnPanel.add(updateStockButton);

        mainPanel.add(leftColumnPanel);
        mainPanel.add(new JLabel("            "));
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
