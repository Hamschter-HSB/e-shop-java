package net.eshop.ui.viewmodel;

import net.eshop.domain.BulkArticle;
import net.eshop.domain.ShoppingBasket;
import net.eshop.domain.dataaccess.DataPersister;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ShowInvoiceDialogViewModel {

    private static final Logger logger = Logger.getLogger(AddArticleDialogViewModel.class.getName());

    private final DataPersister dataPersister;

    public ShowInvoiceDialogViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public Map<BulkArticle, Integer> getAndUpdateAllArticlesInShoppingBasketForUser() {

        final Map<BulkArticle, Integer> allArticlesInShoppingBasket = new HashMap<>();
        ShoppingBasket shoppingBasket = dataPersister.getCustomer().getShoppingBasket();

        shoppingBasket.getArticleMap().forEach((articleID, amount) -> {

            BulkArticle bulkArticle = dataPersister.readBulkArticle(articleID);
            //bulkArticle.setStock(bulkArticle.getStock() - amount);
            dataPersister.updateBulkArticle(bulkArticle);

            allArticlesInShoppingBasket.put(bulkArticle, amount);
        });

        return allArticlesInShoppingBasket;
    }

    public String getTotal() {

        ShoppingBasket shoppingBasket = dataPersister.getCustomer().getShoppingBasket();
        float total = 0;

        for (Map.Entry<Integer, Integer> entry : shoppingBasket.getArticleMap().entrySet()) {
            int articleID = entry.getKey();
            int amount = entry.getValue();

            BulkArticle articl = dataPersister.readBulkArticle(articleID);
            if (articl != null) {
                total += (float) (articl.getPrice() * (amount * articl.getBulkSize()));
            }
        }

        return String.format("%.2f", total);
    }

    public void updateStocksAndEmptyShoppingBasket() {

        ShoppingBasket shoppingBasket = dataPersister.getCustomer().getShoppingBasket();

        for (Map.Entry<Integer, Integer> entry : shoppingBasket.getArticleMap().entrySet()) {
            int articleID = entry.getKey();
            int amount = entry.getValue();

            BulkArticle bulkArticle = dataPersister.readBulkArticle(articleID);
            if (bulkArticle != null) {
                bulkArticle.setStock((bulkArticle.getStock() - (amount * bulkArticle.getBulkSize())));
                dataPersister.updateBulkArticle(bulkArticle);
            }
        }

        shoppingBasket.clear();
    }
}
