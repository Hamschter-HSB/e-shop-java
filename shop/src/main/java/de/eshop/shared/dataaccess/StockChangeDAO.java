package de.eshop.shared.dataaccess;

import de.eshop.shared.domain.events.StockChange;

/**
 * This interface is used for {@link StockChange stock chagnes} only! {@link StockChange stock changes} need an article id to be created.
 * Therefore, a method is necessary that provides the last id for an article.
 */
public interface StockChangeDAO extends DAO<StockChange> {

    int readLastCreatedBulkArticleID();
}
