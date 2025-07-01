package net.eshop.ui.viewmodel;

import net.eshop.domain.BulkArticle;
import net.eshop.domain.dataaccess.DataPersister;

import java.util.logging.Logger;

public class AddArticleToCartDialogViewModel {

    private static final Logger logger = Logger.getLogger(AddArticleToCartDialogViewModel.class.getName());

    private final DataPersister dataPersister;

    public AddArticleToCartDialogViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public BulkArticle loadBulkArticle(int articleID) {
        return dataPersister.readBulkArticle(articleID);
    }

    public void addBulkArticleToCart(int articleID, int amount) {
        dataPersister.getCustomer().getShoppingBasket().addToArticleMap(articleID, amount);
    }
}
