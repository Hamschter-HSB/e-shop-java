package de.eshop.client.ui.events;

import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.ShoppingBasket;

/**
 * {@link javax.security.auth.callback.Callback} interface for successful adding a {@link BulkArticle article} to a {@link ShoppingBasket}.
 * <p>
 * Called after a {@link BulkArticle article} has successfully been added to a {@link ShoppingBasket}.
 */
@FunctionalInterface
public interface AddArticleToCartListener {

    void onOpenAddArticleToCartDialogView(int bulkArticleID);
}
