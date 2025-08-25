package de.eshop.client.connection;

import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.Customer;
import de.eshop.shared.domain.StaffMember;
import de.eshop.shared.domain.events.StockChange;
import de.eshop.shared.domain.exceptions.ServerInputReadException;
import de.eshop.shared.serialization.network.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnectionToServer {

    private static final Logger logger = Logger.getLogger(ClientConnectionToServer.class.getName());
    private static final String CLIENT_PREFIX = " - [CLIENT] -> ";

    // Data structure for communication
    private Socket socket = null;
    private BufferedReader in;
    private PrintStream out;

    //Serializers
    private final CustomerSerializer customerSerializer = new CustomerSerializer();
    private final StaffMemberSerializer staffMemberSerializer = new StaffMemberSerializer();
    private final BulkArticleSerializer bulkArticleSerializer = new BulkArticleSerializer();
    private final StockChangeSerializer stockChangeSerializer = new StockChangeSerializer();

    /**
     * Constructor that establishes the connection to the server (socket) and creates input and output streams for communication with the server.
     *
     * @param host Host on which the server is running
     * @param port Port on which the server will listen for connection requests
     */
    ClientConnectionToServer(String host, int port) {
        try {
            // Socket object creation for communication with host/port
            socket = new Socket(host, port);

            // Creation of Stream object for tex-I/O
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException exception) {

            logger.log(Level.SEVERE, "Error while trying to open Sockets/Streams to the server!: " + exception);

            if (socket != null) {
                try {
                    socket.close();
                    logger.log(Level.INFO, "Socket closed");
                } catch (IOException ioe) { /* Fehlerbehandlung */ }
            }
            System.exit(1);
        }

        logger.log(Level.INFO, "Successfully connected to server: " + socket.getInetAddress() + ":" + socket.getPort());

        // Read greetings from server
        try {
            String message = in.readLine();
            System.out.println(message);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "An Error occurred while trying to read an input from server.");
            throw new ServerInputReadException("An Error occurred while trying to read an input from server: " + exception);
        }
    }

    public void closeConnection() {
        out.println("Client quits.");

        try {
            if (in != null) in.close();
        } catch (IOException ignored) {
        }

        if (out != null) out.close();

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Client-Socket was closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createStaffMember(StaffMember staffMember) {
        out.println("STAFF_MEMBER.CREATE");

        // "0" because the ID is automatically assigned correctly by the DAO when the StaffMember is created. However, for correct transfer, we need Player ID = "0"
        out.println("0");
        out.println(staffMember.getName());
        out.println(staffMember.getPassword());
    }

    public StaffMember readStaffMemberByID(int id) throws IOException {
        out.println("STAFF_MEMBER.READ");
        out.println(id);

        StaffMember staffMember = staffMemberSerializer.read(in);
        logger.info(CLIENT_PREFIX + "read staff member | UserID:" +" " + staffMember.getNumber() + " UserName: " + staffMember.getName());

        return staffMember;
    }

    public void deleteStaffMember(int id) {
        out.println("STAFF_MEMBER.DELETE");
        out.println(id);
    }

    public List<StaffMember> readAllStaffMembers() throws IOException {
        out.println("STAFF_MEMBER.READ_ALL");
        return SerializerHelper.readList(staffMemberSerializer, in);
    }

    public void createCustomer(Customer customer) {
        out.println("CUSTOMER.CREATE");

        // "0" because the ID is automatically assigned correctly by the DAO when the Customer is created. However, for correct transfer, we need Player ID = "0"
        out.println("0");
        out.println(customer.getName());
        out.println(customer.getPassword());
        out.println(customer.getAddress());
    }

    public Customer readCustomerByID(int id) throws IOException {

        out.println("CUSTOMER.READ");
        out.println(id);

        Customer customer = customerSerializer.read(in);
        logger.info(CLIENT_PREFIX + "read customer | UserID:" +" " + customer.getNumber() + " UserName: " + customer.getName());

        return customer;
    }

    public void deleteCustomer(int id) {
        out.println("CUSTOMER.DELETE");
        out.println(id);
    }

    public List<Customer> readAllCustomers() throws IOException {
        out.println("CUSTOMER.READ_ALL");
        return SerializerHelper.readList(customerSerializer, in);
    }

    // BulkArticle
    public void createBulkArticle(BulkArticle bulkArticle) {
        out.println("BULK_ARTICLE.CREATE");

        // Since update() deletes and adds a new article, we have to check if the
        // article is new (-1) or already exists (!=(-1)) and treat it accordingly (See BulkArticleFileDAOImpl)
        out.println("-1");
        out.println(bulkArticle.getName());
        out.println(bulkArticle.getDescription());
        out.println(bulkArticle.getStock());
        out.println(bulkArticle.getPrice());
        out.println(bulkArticle.getBulkSize());
    }

    public BulkArticle readBulkArticleByID(int id) throws IOException {

        out.println("BULK_ARTICLE.READ");
        out.println(id);

        BulkArticle bulkArticle = bulkArticleSerializer.read(in);
        logger.info(CLIENT_PREFIX + "read bulk article | ID: " + bulkArticle.getArticleNumber() + " Name: " + bulkArticle.getName() + " Description: " + bulkArticle.getDescription() + " Stock: " + bulkArticle.getStock() + " Price: " + bulkArticle.getPrice() + " BulkSize: " + bulkArticle.getBulkSize());

        return bulkArticle;
    }

    public void updateBulkArticle(BulkArticle bulkArticle) {
        out.println("BULK_ARTICLE.UPDATE");
        bulkArticleSerializer.write(out, bulkArticle);
    }

    public List<BulkArticle> readAllBulkArticles() throws IOException {
        out.println("BULK_ARTICLE.READ_ALL");
        return SerializerHelper.readList(bulkArticleSerializer, in);
    }

    public void createStockChange(StockChange stockChange) {
        out.println("STOCK_CHANGE.CREATE");

        // "0" because the ID is automatically assigned correctly by the DAO when the StockChange is created. However, for correct transfer, we need Player ID = "0"
        out.println("0");
        out.println(stockChange.getDayOfYear());
        out.println(stockChange.getArticleNumber());
        out.println(stockChange.getOldAmount());
        out.println(stockChange.getNewAmount());
        out.println(stockChange.getUserID());
        out.println(stockChange.getUserType().name());
    }

    public List<StockChange> readAllStockChanges() throws IOException {
        out.println("STOCK_CHANGE.READ_ALL");
        return SerializerHelper.readList(stockChangeSerializer, in);
    }

    public int readLastCreatedBulkArticleID() {
        out.println("STOCK_CHANGE.READ_LAST_CREATED_BULK_ARTICLE_ID");

        int lastCreatedBulkArticleID;

        try {
            lastCreatedBulkArticleID = Integer.parseInt(in.readLine());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return lastCreatedBulkArticleID;
    }
}
