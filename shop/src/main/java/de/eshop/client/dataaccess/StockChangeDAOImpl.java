package de.eshop.client.dataaccess;

import de.eshop.client.connection.ClientConnectionToServer;
import de.eshop.shared.dataaccess.StockChangeDAO;
import de.eshop.shared.domain.events.StockChange;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class StockChangeDAOImpl implements StockChangeDAO {

    private static final Logger logger = Logger.getLogger(StockChangeDAOImpl.class.getName());

    private final ClientConnectionToServer clientConnectionToServer;

    public StockChangeDAOImpl(ClientConnectionToServer clientConnectionToServer) {
        this.clientConnectionToServer = clientConnectionToServer;
    }

    @Override
    public void create(StockChange type) throws IOException {
        clientConnectionToServer.createStockChange(type);
    }

    @Override
    public StockChange read(int id) throws IOException {
        return null;
    }

    @Override
    public List<StockChange> readAll() throws IOException {
        return clientConnectionToServer.readAllStockChanges();
    }

    @Override
    public void update(StockChange type) throws IOException {

    }

    @Override
    public void delete(int id) throws IOException {

    }

    @Override
    public int readLastCreatedBulkArticleID() {
        return clientConnectionToServer.readLastCreatedBulkArticleID();
    }
}
