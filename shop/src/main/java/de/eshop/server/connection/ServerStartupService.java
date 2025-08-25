package de.eshop.server.connection;

import de.eshop.server.dataaccess.ServerDataPersisterImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStartupService {

    private static final Logger logger = Logger.getLogger(ServerStartupService.class.getName());

    private final List<Socket> connectedCallbackClients = new CopyOnWriteArrayList<>();

    // SINGLETON: Each client uses the SAME ServerDataPersister. (Server side FileDAOs CRUD-Operations are synchronized)
    private final ServerDataPersisterImpl serverDataPersisterImpl = new ServerDataPersisterImpl();

    public final static int DEFAULT_SERVER_PORT = 6789;

    private ServerSocket serverSocket;

    public ServerStartupService(int portToListenTo) {

        int port = (portToListenTo == 0) ? DEFAULT_SERVER_PORT : portToListenTo;

        try {
            // Create server socket
            serverSocket = new ServerSocket(port);

            // Print server data
            InetAddress inetAddress = InetAddress.getLocalHost();
            logger.info("Host: " + inetAddress.getHostName());
            logger.info("Server *" + inetAddress.getHostName() + "* is listening on port " + port);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "An Error has occurred while setting up the server socket. System exits now: " + exception);
            System.exit(1);
        }
    }

    /**
     * Method for accepting connection requests from clients.
     * The method repeatedly queries whether connection requests are pending
     * and then creates a ClientRequestProcessor object with the client socket
     * created for this connection.
     */
    public void acceptClientConnectRequests() {

        try {
            while (true) {

                Socket clientSocket = serverSocket.accept();

                Thread thread = new Thread(() -> {
                    ServerConnectionToClient serverConnectionToClient = new ServerConnectionToClient(clientSocket, serverDataPersisterImpl);
                    serverConnectionToClient.processRequests();
                });

                thread.start();

            }
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "An Error Occurred during waiting for connection: " + exception.getMessage());
            System.exit(1);
        }
    }

    public void acceptCallbackConnections(int callbackPort) {

        new Thread(() -> {

            try (ServerSocket callbackServerSocket = new ServerSocket(callbackPort)) {
                logger.info("Server is listening for CALLBACK connections on port " + callbackPort);

                while (true) {
                    Socket callbackSocket = callbackServerSocket.accept();
                    logger.info("Callback client connected: " + callbackSocket.getInetAddress());

                    connectedCallbackClients.add(callbackSocket);

                    // Starting ServerCallbackConnection
                    Thread callbackThread = new Thread(() -> {
                        ServerCallbackConnection callbackConnection =
                                new ServerCallbackConnection(callbackSocket, serverDataPersisterImpl);
                        callbackConnection.processRequests();
                    });

                    callbackThread.start();
                }

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to accept callback connections: " + e.getMessage());
            }
        }).start();
    }

}
