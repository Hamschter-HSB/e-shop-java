package de.eshop.server.main;

import de.eshop.server.connection.ServerStartupService;

public class ServerMain {

    public static void main(String[] args) {

        int port = 0;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                port = 0;
            }
        }
        ServerStartupService serverStartupService = new ServerStartupService(port);

        // Start port for callback connection
        serverStartupService.acceptCallbackConnections(6790);

        // wait for clients to connect
        serverStartupService.acceptClientConnectRequests();
    }
}
