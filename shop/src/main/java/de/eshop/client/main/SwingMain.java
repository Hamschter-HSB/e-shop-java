package de.eshop.client.main;

import de.eshop.client.connection.ClientCallbackListenerToServer;
import de.eshop.client.connection.ClientStartupService;
import de.eshop.client.connection.ClientConnectionToServer;
import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.client.ui.view.UIManager;

public class SwingMain {

    public static void main(String[] args) {

        System.out.println("Swing E-Shop started:");

        // Establish Connection to server
        ClientStartupService clientConnectionProcessor = new ClientStartupService();
        ClientConnectionToServer clientConnectionToServer = clientConnectionProcessor.establishConnectionToServer(args);

        ClientCallbackListenerToServer clientCallbackListenerToServer = clientConnectionProcessor.establishCallbackConnectionToServer();

        //ShutDownHook for client/server connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown detected – Connection will be closed!");
            clientConnectionToServer.closeConnection();
            clientCallbackListenerToServer.closeConnection();
        }));

        ClientDataPersisterImpl clientDataPersisterImpl = new ClientDataPersisterImpl(clientConnectionToServer);

        UIManager uiManager = new UIManager(clientDataPersisterImpl);
        uiManager.start();

        clientCallbackListenerToServer.setClientUIUpdater(uiManager::revalidateAndUpdateUI);
    }
}
