package de.eshop.client.connection;

public class ClientStartupService {

    public ClientConnectionToServer establishConnectionToServer(String[] optionalPort) {

        String host = "localhost";
        int port = 6789;
        if (optionalPort.length == 2) {
            host = optionalPort[0];
            try {
                port = Integer.parseInt(optionalPort[1]);
            } catch (NumberFormatException e) {
                port = 0;
            }
        }
        // Start Client to :
        return new ClientConnectionToServer(host, port);
    }

    public ClientCallbackListenerToServer establishCallbackConnectionToServer() {

        String host = "localhost";
        int port = 6790;
        // Start Client to :
        // Second connection to server for client read only access (UI-Updates)
        ClientCallbackListenerToServer callbackListener = new ClientCallbackListenerToServer(host, port);

        Thread callbackThread = new Thread(callbackListener::processRequests);
        callbackThread.setDaemon(true);
        callbackThread.start();

        return callbackListener;
    }
}
