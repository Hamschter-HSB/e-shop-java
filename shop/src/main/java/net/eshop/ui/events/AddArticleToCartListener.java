package net.eshop.ui.events;

/**
 * {@link javax.security.auth.callback.Callback} interface for successful adding a {@link net.eshop.domain.BulkArticle article} to a {@link net.eshop.domain.ShoppingBasket}.
 * <p>
 * Called after a {@link net.eshop.domain.BulkArticle article} has successfully been added to a {@link net.eshop.domain.ShoppingBasket}.
 */
@FunctionalInterface
public interface AddArticleToCartListener {

    void onOpenAddArticleToCartDialogView(int bulkArticleID);
}
