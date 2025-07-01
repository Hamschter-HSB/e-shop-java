package net.eshop.ui.view;

import net.eshop.domain.BulkArticle;
import net.eshop.ui.viewmodel.EditArticleStockDialogViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.logging.Logger;

public class EditArticleStockDialogView extends JDialog {

    private static final Logger logger = Logger.getLogger(LoginAndRegistrationView.class.getName());

    private final EditArticleStockDialogViewModel editArticleStockDialogViewModel;
    private final Frame parent;

    public EditArticleStockDialogView(EditArticleStockDialogViewModel editArticleStockDialogViewModel, Frame parent) {
        super(parent, "Edit article stock", true);
        this.editArticleStockDialogViewModel = editArticleStockDialogViewModel;
        this.parent = parent;
        setLayout(new GridLayout(1, 6));
    }

    public void openEditArticleStockDialog() {

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

        add(new JLabel("Article:"));
        add(articleID);
        add(articleName);

        add(new JLabel("Stock:"));
        add(stock);

        JButton updateStockButton = new JButton("Update Stock");
        updateStockButton.addActionListener(actionEvent -> {
            editArticleStockDialogViewModel.updateArticleStock((Integer) articleID.getSelectedItem(), (Integer) stock.getValue());
            this.dispose();
        });
        add(updateStockButton);

        pack();
        setLocationRelativeTo(parent);

        this.setVisible(true);
    }

}
