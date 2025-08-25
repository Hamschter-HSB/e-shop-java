package de.eshop.server.connection;

import de.eshop.server.dataaccess.ServerDataPersisterImpl;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.Customer;
import de.eshop.shared.domain.StaffMember;
import de.eshop.shared.domain.events.StockChange;
import de.eshop.shared.serialization.network.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class ServerClientSocketIOManager {

    private final ServerDataPersisterImpl serverDataPersisterImpl;
    private final BufferedReader in;
    private final PrintStream out;

    // Serialization:
    private final StaffMemberSerializer staffMemberSerializer = new StaffMemberSerializer();
    private final CustomerSerializer customerSerializer = new CustomerSerializer();
    private final BulkArticleSerializer bulkArticleSerializer = new BulkArticleSerializer();
    private final StockChangeSerializer stockChangeSerializer = new StockChangeSerializer();

    public ServerClientSocketIOManager(ServerDataPersisterImpl serverDataPersisterImpl, BufferedReader in, PrintStream out) {
        this.serverDataPersisterImpl = serverDataPersisterImpl;
        this.in = in;
        this.out = out;
    }

    //CREATE
    public void sendCreateRequestTypeToClient(DAORequestType requestType) throws IOException {

        if (requestType.equals(DAORequestType.STAFF_MEMBER)) {

            StaffMember staffMember = staffMemberSerializer.read(in);
            serverDataPersisterImpl.createStaffMember(staffMember);
        }

        if (requestType.equals(DAORequestType.CUSTOMER)) {

            Customer customer = customerSerializer.read(in);
            serverDataPersisterImpl.createCustomer(customer);
        }

        if (requestType.equals(DAORequestType.STOCK_CHANGE)) {
            StockChange stockChange = stockChangeSerializer.read(in);
            serverDataPersisterImpl.createStockChange(stockChange);
        }

        if (requestType.equals(DAORequestType.BULK_ARTICLE)) {

            BulkArticle bulkArticle = bulkArticleSerializer.read(in);
            serverDataPersisterImpl.createBulkArticle(bulkArticle);
        }
    }

    //READ
    public void sendReadRequestTypeToClient(DAORequestType requestType, int id) {

        if (requestType.equals(DAORequestType.STAFF_MEMBER)) {

            StaffMember staffMember = serverDataPersisterImpl.readStaffMember(id);
            staffMemberSerializer.write(out, staffMember);
        }

        if (requestType.equals(DAORequestType.CUSTOMER)) {

            Customer customer = serverDataPersisterImpl.readCustomer(id);
            customerSerializer.write(out, customer);
        }

        if (requestType.equals(DAORequestType.BULK_ARTICLE)) {

            BulkArticle bulkArticle = serverDataPersisterImpl.readBulkArticle(id);
            bulkArticleSerializer.write(out, bulkArticle);
        }
    }

    //UPDATE
    public void sendUpdateRequestTypeToClient(DAORequestType requestType) throws IOException {

        if (requestType.equals(DAORequestType.BULK_ARTICLE)) {

            BulkArticle bulkArticle = bulkArticleSerializer.read(in);
            serverDataPersisterImpl.updateBulkArticleSynchronized(bulkArticle);
        }
    }

    //DELETE
    public void sendDeleteRequestTypeToClient(DAORequestType requestType, int id) {

        if (requestType.equals(DAORequestType.BULK_ARTICLE))
            serverDataPersisterImpl.deleteBulkArticle(id);
    }

    //READ_ALL
    public void sendReadAllRequestTypeToClient(DAORequestType requestType) {

        if (requestType.equals(DAORequestType.STAFF_MEMBER)) {

            List<StaffMember> staffMembers = serverDataPersisterImpl.readAllStaffMembers();
            SerializerHelper.writeList(staffMemberSerializer, out, staffMembers);
        }
        if (requestType.equals(DAORequestType.CUSTOMER)) {

            List<Customer> customers = serverDataPersisterImpl.readAllCustomers();
            SerializerHelper.writeList(customerSerializer, out, customers);
        }
        if (requestType.equals(DAORequestType.STOCK_CHANGE)) {

            List<StockChange> stockChanges = serverDataPersisterImpl.readAllStockChanges();
            SerializerHelper.writeList(stockChangeSerializer, out, stockChanges);
        }
        if (requestType.equals(DAORequestType.BULK_ARTICLE)) {

            List<BulkArticle> bulkArticles = serverDataPersisterImpl.readAllBulkArticles();
            SerializerHelper.writeList(bulkArticleSerializer, out, bulkArticles);
        }
    }

    //StockChanges only -> readLastCreatedBulkArticleID()
    public void sendReadLastCreatedBulkArticleIDToClient(DAORequestType requestType) {

        if (requestType.equals(DAORequestType.STOCK_CHANGE)) {
            int lastCreatedBulkArticleID = serverDataPersisterImpl.sendReadLastCreatedBulkArticleIDToClient();
            out.println(lastCreatedBulkArticleID);
        }
    }

}


