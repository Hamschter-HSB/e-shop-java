package de.eshop.client.ui.viewmodel;

import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.ShoppingBasket;
import de.eshop.shared.domain.UserType;
import de.eshop.shared.domain.events.StockChange;
import de.eshop.shared.domain.exceptions.ArticleOutOfStockException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class ShowInvoiceDialogViewModel {

    private static final Logger logger = Logger.getLogger(ShowInvoiceDialogViewModel.class.getName());

    private final ClientDataPersisterImpl clientDataPersisterImpl;

    public ShowInvoiceDialogViewModel(ClientDataPersisterImpl clientDataPersisterImpl) {
        this.clientDataPersisterImpl = clientDataPersisterImpl;
    }

    public Map<BulkArticle, Integer> getAllArticlesInShoppingBasketForUser() {

        final Map<BulkArticle, Integer> allArticlesInShoppingBasket = new HashMap<>();
        ShoppingBasket shoppingBasket = clientDataPersisterImpl.getCustomer().getShoppingBasket();

        shoppingBasket.getArticleMap().forEach((articleID, amount) -> {
            BulkArticle bulkArticle = clientDataPersisterImpl.readBulkArticle(articleID);
            allArticlesInShoppingBasket.put(bulkArticle, amount);
        });

        return allArticlesInShoppingBasket;
    }

    public String totalPriceOfSoppingBasket() {

        ShoppingBasket shoppingBasket = clientDataPersisterImpl.getCustomer().getShoppingBasket();
        float total = 0;

        for (Map.Entry<Integer, Integer> entry : shoppingBasket.getArticleMap().entrySet()) {
            int articleID = entry.getKey();
            int amount = entry.getValue();

            BulkArticle articl = clientDataPersisterImpl.readBulkArticle(articleID);
            if (articl != null) {
                total += (float) (articl.getPrice() * amount);
            }
        }

        return String.format(Locale.US,"%.2f", total);
    }

    public String updateStocksAndEmptyShoppingBasketForNotExceededArticleStock() {

        ShoppingBasket shoppingBasket = clientDataPersisterImpl.getCustomer().getShoppingBasket();

        for (Map.Entry<Integer, Integer> entry : shoppingBasket.getArticleMap().entrySet()) {
            int articleID = entry.getKey();
            int amount = entry.getValue();

            BulkArticle bulkArticle = clientDataPersisterImpl.readBulkArticle(articleID);
            if (bulkArticle != null) {

                if (bulkArticle.getStock() < amount) {
                    shoppingBasket.removeFromArticleMap(bulkArticle.getArticleNumber());
                    return "ID: " + bulkArticle.getArticleNumber() + " Name:" + bulkArticle.getName();
                }

                int oldStockAmount = bulkArticle.getStock();
                int newStock = bulkArticle.getStock() - (amount * bulkArticle.getBulkSize());
                bulkArticle.setStock(newStock);
                bulkArticle.setArticleNumber(articleID);
                clientDataPersisterImpl.updateBulkArticle(bulkArticle);

                UserType userType = UserType.valueOf(System.getProperty("CURRENT_USER"));
                StockChange stockChange = new StockChange(0, LocalDateTime.now().getDayOfYear(), articleID, oldStockAmount, newStock, Integer.parseInt(System.getProperty("CURRENT_USER_ID")), userType);
                clientDataPersisterImpl.createStockChange(stockChange);
            }
        }

        shoppingBasket.clear();
        return "";
    }
    public String getCurrentUserName(){
        return clientDataPersisterImpl.getCustomer().getName();
    }
}
