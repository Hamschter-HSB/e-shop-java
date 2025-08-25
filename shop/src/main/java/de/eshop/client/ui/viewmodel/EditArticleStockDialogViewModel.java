package de.eshop.client.ui.viewmodel;

import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.UserType;
import de.eshop.shared.domain.events.StockChange;
import de.eshop.shared.domain.exceptions.ArticleNotFoundException;
import de.eshop.shared.domain.exceptions.NoMultipleOfBulkException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditArticleStockDialogViewModel {

    private static final Logger logger = Logger.getLogger(EditArticleStockDialogViewModel.class.getName());

    private final ClientDataPersisterImpl clientDataPersisterImpl;

    public EditArticleStockDialogViewModel(ClientDataPersisterImpl clientDataPersisterImpl) {
        this.clientDataPersisterImpl = clientDataPersisterImpl;
    }

    public void updateArticleStockAndCreateStockChange(int articleID, int newStock) throws ArticleNotFoundException, NoMultipleOfBulkException {

        BulkArticle bulkArticle = clientDataPersisterImpl.readBulkArticle(articleID);

        if (bulkArticle == null) {
            logger.log(Level.SEVERE, "An Error has Occurred while updating the stock amount for an bulk article. Article ID: " + articleID + " not found. This should never happen.");
            throw new ArticleNotFoundException("Error! No article with id " + articleID + " found.", articleID);
        }

        ViewModelUtils.checkIsBulkArticleMultiple(bulkArticle.getBulkSize(), newStock);


        int oldStockAmount = bulkArticle.getStock();

        bulkArticle.setStock(newStock);

        bulkArticle.setArticleNumber(articleID);
        clientDataPersisterImpl.updateBulkArticle(bulkArticle);

        UserType userType = UserType.valueOf(System.getProperty("CURRENT_USER"));
        StockChange stockChange = new StockChange(0, LocalDateTime.now().getDayOfYear(), articleID, oldStockAmount, bulkArticle.getStock(), Integer.parseInt(System.getProperty("CURRENT_USER_ID")), userType);
        clientDataPersisterImpl.createStockChange(stockChange);
    }

    public HashMap<Integer, BulkArticle> getArticleNumberToArticle() {
        HashMap<Integer, BulkArticle> allArticlesHashMap = new HashMap<>();
        clientDataPersisterImpl.readAllBulkArticles().forEach(article -> allArticlesHashMap.put(article.getArticleNumber(), article));
        return allArticlesHashMap;
    }
}
