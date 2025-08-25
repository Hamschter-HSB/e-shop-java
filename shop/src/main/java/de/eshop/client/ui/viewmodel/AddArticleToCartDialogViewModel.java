package de.eshop.client.ui.viewmodel;

import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.shared.domain.BulkArticle;

import java.util.logging.Logger;

public class AddArticleToCartDialogViewModel {

    private static final Logger logger = Logger.getLogger(AddArticleToCartDialogViewModel.class.getName());

    private final ClientDataPersisterImpl clientDataPersisterImpl;

    public AddArticleToCartDialogViewModel(ClientDataPersisterImpl clientDataPersisterImpl) {
        this.clientDataPersisterImpl = clientDataPersisterImpl;
    }

    public BulkArticle loadBulkArticle(int articleID) {
        return clientDataPersisterImpl.readBulkArticle(articleID);
    }

    public void addBulkArticleToCart(int articleID, int amount) {
        clientDataPersisterImpl.getCustomer().getShoppingBasket().addToArticleMap(articleID, amount);
    }
}
