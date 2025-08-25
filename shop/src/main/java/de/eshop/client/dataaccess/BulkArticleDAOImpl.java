package de.eshop.client.dataaccess;

import de.eshop.client.connection.ClientConnectionToServer;
import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.domain.BulkArticle;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class BulkArticleDAOImpl implements DAO<BulkArticle> {

    private static final Logger logger = Logger.getLogger(BulkArticleDAOImpl.class.getName());

    private final ClientConnectionToServer clientConnectionToServer;

    public BulkArticleDAOImpl(ClientConnectionToServer clientConnectionToServer) {
        this.clientConnectionToServer = clientConnectionToServer;
    }

    @Override
    public void create(BulkArticle type) throws IOException {
        clientConnectionToServer.createBulkArticle(type);
    }

    @Override
    public BulkArticle read(int id) throws IOException {
        return clientConnectionToServer.readBulkArticleByID(id);
    }

    @Override
    public List<BulkArticle> readAll() throws IOException {
        return clientConnectionToServer.readAllBulkArticles();
    }

    @Override
    public void update(BulkArticle type) throws IOException {
        clientConnectionToServer.updateBulkArticle(type);
    }

    @Override
    public void delete(int id) throws IOException {

    }
}
