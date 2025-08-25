package de.eshop.client.ui.viewmodel;

import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.UserType;
import de.eshop.shared.domain.events.StockChange;
import de.eshop.shared.domain.exceptions.NoMultipleOfBulkException;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class AddArticleDialogViewModel {

    private static final Logger logger = Logger.getLogger(AddArticleDialogViewModel.class.getName());

    private final ClientDataPersisterImpl clientDataPersisterImpl;

    public AddArticleDialogViewModel(ClientDataPersisterImpl clientDataPersisterImpl) {
        this.clientDataPersisterImpl = clientDataPersisterImpl;
    }

    public void createBulkArticle(String name, String description, int stockSize, double price, int bulkSize) throws NoMultipleOfBulkException {

        ViewModelUtils.checkIsBulkArticleMultiple(bulkSize, stockSize);

        BulkArticle bulkArticle = new BulkArticle(-1, name, description, stockSize, price, bulkSize);
        clientDataPersisterImpl.createBulkArticle(bulkArticle);

        int lastAddedArticleID = clientDataPersisterImpl.sendReadLastCreatedBulkArticleIDToClient();

        UserType userType = UserType.valueOf(System.getProperty("CURRENT_USER"));
        StockChange stockChange = new StockChange(0, LocalDateTime.now().getDayOfYear(), lastAddedArticleID, 0, bulkArticle.getStock(), Integer.parseInt(System.getProperty("CURRENT_USER_ID")), userType);
        clientDataPersisterImpl.createStockChange(stockChange);
    }
}
