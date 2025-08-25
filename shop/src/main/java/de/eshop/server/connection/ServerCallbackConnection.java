package de.eshop.server.connection;

import de.eshop.server.dataaccess.ServerDataPersisterImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a {@link Socket socket connection} to a server for reading access only and helps clients to receive UI updates.
 */
public class ServerCallbackConnection {

    private static final Logger logger = Logger.getLogger(ServerCallbackConnection.class.getName());
    private static final String SERVER_PREFIX = " - [CALLBACK-SERVER] -> ";

    // Data structure for communication
    private final ServerDataPersisterImpl serverDataPersister;
    private final Socket clientSocket;
    private BufferedReader in;
    private PrintStream out;

    public ServerCallbackConnection(Socket clientSocket, ServerDataPersisterImpl serverDataPersister) {

        this.serverDataPersister = serverDataPersister;
        this.clientSocket = clientSocket;

        // Initialize I/O-Streams:
        try {
            in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            out = new PrintStream(this.clientSocket.getOutputStream());
        } catch (IOException exception) {
            try {
                this.clientSocket.close();
            } catch (IOException ignored) {
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
        out.println(SERVER_PREFIX + "Connection to callback-server succeeded!");
        serverDataPersister.registerClient(this);

        try {
            String line;
            while ((line = in.readLine()) != null) {
                if ("quit".equalsIgnoreCase(line)) {
                    break;
                }

                logger.info(SERVER_PREFIX + "Received from client: " + line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, SERVER_PREFIX + " error in callback communication", e);
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
        } catch (IOException ignored) {
        }

        if (out != null) out.close();

        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                serverDataPersister.unregisterClient(this);
            }
        } catch (IOException ignored) {
        }
    }

    public void sendUpdate() {
        if (out != null) {
            out.println("UPDATE_VIEWS");
        }
    }
}
