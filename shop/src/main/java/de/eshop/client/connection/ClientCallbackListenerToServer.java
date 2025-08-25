package de.eshop.client.connection;

import de.eshop.shared.domain.exceptions.ServerInputReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a {@link java.net.Socket socket connection} to a server for reading access only and helps clients to receive UI updates.
 */
public class ClientCallbackListenerToServer {

    private static final Logger logger = Logger.getLogger(ClientCallbackListenerToServer.class.getName());
    private static final String CLIENT_PREFIX = " - [CLIENT] -> ";

    // Data structure for communication
    private Socket socket = null;
    private BufferedReader in;
    private PrintStream out;

    // CallBack
    private Runnable clientUIUpdater;

    /**
     * Constructor that establishes the connection to the server (socket) and creates input and output streams for communication with the server.
     *
     * @param host Host on which the server is running
     * @param port Port on which the server will listen for connection requests
     */
    ClientCallbackListenerToServer(String host, int port) {
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

    /**
     * Method for handling communication with the client according to the
     * specified communication protocol.
     */
    public void processRequests() {

        String clientInput = "";

        out.println(CLIENT_PREFIX + "Connection to callback server succeeded!");

        do {
            try {
                clientInput = in.readLine();
            } catch (IOException exception) {
                if (exception.getMessage() != null && exception.getMessage().contains("Connection reset")) {
                    logger.fine(CLIENT_PREFIX + "client connection terminated.");
                } else {
                    logger.log(Level.SEVERE, CLIENT_PREFIX + " unexpected error while reading clients: ", exception);
                }
                break;
            }

            if (clientInput == null)
                clientInput = "quit";

            if ("quit".equals(clientInput))
                return;

            if (clientInput.isEmpty())
                continue;

            switch (clientInput) {
                case "UPDATE_VIEWS" -> {
                    if(clientUIUpdater != null) {
                        clientUIUpdater.run();
                    }
                    logger.info(CLIENT_PREFIX + "DEBUG -----------------------> UPDATE UIS");
                }
                default -> System.out.println("WRONG REQUEST: " + clientInput);
            }
        } while (!(clientInput.equals("quit")));

        logger.info(CLIENT_PREFIX + "Connection to client " + socket.getInetAddress()
                + ":" + socket.getPort() + " closed.");

        closeConnection();
    }

    public void closeConnection() {
        out.println("Client quits.");

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

    public void setClientUIUpdater(Runnable clientUIUpdater) {
        this.clientUIUpdater = clientUIUpdater;
    }

}
