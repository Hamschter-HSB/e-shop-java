package de.eshop.server.connection;

import de.eshop.server.dataaccess.ServerDataPersisterImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnectionToClient {

    private static final Logger logger = Logger.getLogger(ServerConnectionToClient.class.getName());
    private static final String SERVER_PREFIX = " - [DAO-SERVER] -> ";

    // Data structure for communication
    private ServerClientSocketIOManager serverClientSocketIOManager;
    private final Socket clientSocket;
    private BufferedReader in;
    private PrintStream out;

    public ServerConnectionToClient(Socket clientSocket, ServerDataPersisterImpl serverDataPersisterImpl) {

        this.clientSocket = clientSocket;

        // Initialize I/O-Streams:
        try {
            in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            out = new PrintStream(this.clientSocket.getOutputStream());
            this.serverClientSocketIOManager = new ServerClientSocketIOManager(serverDataPersisterImpl, in, out);
        } catch (IOException exception) {
            try {
                this.clientSocket.close();
            } catch (IOException e2) {
            }
            logger.log(Level.SEVERE, "Exception why providing stream: ", exception);
            return;
        }

        logger.info("Connected to client " + this.clientSocket.getInetAddress() + ":" + this.clientSocket.getPort());
    }

    /**
     * Method for handling communication with the client according to the
     * specified communication protocol.
     */
    public void processRequests() {

        String clientInput = "";

        out.println(SERVER_PREFIX + "Connection to DAO server succeeded!");

        do {
            try {
                clientInput = in.readLine();
            } catch (IOException exception) {
                if (exception.getMessage() != null && exception.getMessage().contains("Connection reset")) {
                    logger.fine(SERVER_PREFIX + "client connection terminated.");
                } else {
                    logger.log(Level.SEVERE, SERVER_PREFIX + " unexpected error while reading clients: ", exception);
                }
                break;
            }

            if (clientInput == null)
                clientInput = "quit";

            if("quit".equals(clientInput))
                return;

            if(clientInput.isEmpty())
                continue;

            String[] splitClientInput = clientInput.split("\\.");

            if(splitClientInput.length != 2)
                continue;

            String DAOType = splitClientInput[0];
            String CRUDType = splitClientInput[1];

            DAORequestType requestType = null;

            switch (DAOType) {
                case "STAFF_MEMBER" -> requestType = DAORequestType.STAFF_MEMBER;
                case "CUSTOMER" -> requestType = DAORequestType.CUSTOMER;
                case "BULK_ARTICLE" -> requestType = DAORequestType.BULK_ARTICLE;
                case "STOCK_CHANGE" -> requestType = DAORequestType.STOCK_CHANGE;
                default -> logger.log(Level.SEVERE, "ERROR: No DAORequestType: " + DAOType);
            }

            switch (CRUDType) {
                case "CREATE" -> {
                    create(requestType);
                    logger.info(SERVER_PREFIX + "CREATE for " + DAOType);
                }
                case "READ" -> {
                    read(requestType);
                    logger.info(SERVER_PREFIX + "READ for " + DAOType);
                }
                case "UPDATE" -> {
                    update(requestType);
                    logger.info(SERVER_PREFIX + "UPDATE for " + DAOType);
                }
                case "READ_ALL" -> {
                    readAll(requestType);
                    logger.info(SERVER_PREFIX + "READ_ALL for " + DAOType);
                }
                //StockChange only
                case "READ_LAST_CREATED_BULK_ARTICLE_ID" -> sendReadLastCreatedBulkArticleIDToClient();
                default -> {
                    System.out.println("WRONG REQUEST: " + requestType);
                }
            }
        } while (!(clientInput.equals("quit")));

        logger.info(SERVER_PREFIX + "Connection to client " + clientSocket.getInetAddress()
                + ":" + clientSocket.getPort() + " closed.");

        closeConnection();
    }

    private void create(DAORequestType requestType) {
        try {
            serverClientSocketIOManager.sendCreateRequestTypeToClient(requestType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void read(DAORequestType requestType) {
        int id = 0;
        try {
            id = Integer.parseInt(in.readLine());
        } catch (Exception exception) {
            logger.info(SERVER_PREFIX + "error while trying reading from client:");
            System.out.println(exception.getMessage());
        }

        serverClientSocketIOManager.sendReadRequestTypeToClient(requestType, id);
    }

    private void update(DAORequestType requestType) {
        try {
            serverClientSocketIOManager.sendUpdateRequestTypeToClient(requestType);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void readAll(DAORequestType requestType) {
        serverClientSocketIOManager.sendReadAllRequestTypeToClient(requestType);
    }

    //StockChange only
    private void sendReadLastCreatedBulkArticleIDToClient() {
        serverClientSocketIOManager.sendReadLastCreatedBulkArticleIDToClient(DAORequestType.STOCK_CHANGE);
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
        } catch (IOException ignored) {}

        if (out != null) out.close();

        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException ignored) {}
    }
}
