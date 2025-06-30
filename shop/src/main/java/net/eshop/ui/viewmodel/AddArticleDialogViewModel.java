package net.eshop.ui.viewmodel;

import net.eshop.domain.BulkArticle;
import net.eshop.domain.dataaccess.DataPersister;

import java.util.logging.Logger;

public class AddArticleDialogViewModel {

    private static final Logger logger = Logger.getLogger(AddArticleDialogViewModel.class.getName());

    private final DataPersister dataPersister;

    public AddArticleDialogViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public void createBulkArticle(String name, String description, int stockSize, double price, int bulkSize) {
        BulkArticle bulkArticle = new BulkArticle(-1, name, description, stockSize, price, bulkSize);
        dataPersister.createBulkArticle(bulkArticle);
    }
}
