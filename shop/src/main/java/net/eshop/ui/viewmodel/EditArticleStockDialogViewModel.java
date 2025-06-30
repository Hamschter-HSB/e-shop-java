package net.eshop.ui.viewmodel;

import net.eshop.domain.BulkArticle;
import net.eshop.domain.dataaccess.DataPersister;

import java.util.HashMap;
import java.util.logging.Logger;

public class EditArticleStockDialogViewModel {

    private static final Logger logger = Logger.getLogger(EditArticleStockDialogViewModel.class.getName());

    private final DataPersister dataPersister;

    public EditArticleStockDialogViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public void updateArticleStock(int articleID, int newStock) {

        BulkArticle bulkArticle = dataPersister.readBulkArticle(articleID);

        bulkArticle.setStock(newStock);
        dataPersister.updateBulkArticle(bulkArticle);
    }

    public HashMap<Integer, BulkArticle> getArticleNumberToArticle(){
        HashMap<Integer, BulkArticle> allArticlesHashMap = new HashMap<>();
        dataPersister.readAllBulkArticles().forEach(article -> allArticlesHashMap.put(article.getArticleNumber(), article));
        return allArticlesHashMap;
    }
}
