package net.eshop.ui.viewmodel;

import net.eshop.domain.dataaccess.DataPersister;

import javax.swing.*;
import java.util.logging.Logger;

public class ShopMainViewModel {

    private static final Logger logger = Logger.getLogger(ShopMainViewModel.class.getName());

    private final DataPersister dataPersister;

    public ShopMainViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public void registerButtonClickHandler(String userName, char[] charPassword, String address, JPanel registrationMainPanel) {
        

    }
}
