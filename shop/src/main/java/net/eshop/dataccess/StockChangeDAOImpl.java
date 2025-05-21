package net.eshop.dataccess;

import net.eshop.domain.events.StockChange;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockChangeDAOImpl implements DAO<StockChange> {

    private static final Logger logger = Logger.getLogger(StockChangeDAOImpl.class.getName());

    private final String REG_EX = ".*=";

    public static final String DATA_PATH = "Data";
    public static final String STOCK_CHANGE = "Stockchange";

    private final File file = new File(STOCK_CHANGE);

    public StockChangeDAOImpl() {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error creating file \"" + STOCK_CHANGE + "\"", e);
            }
        }
    }

    @Override
    public void create(StockChange stockChange) throws IOException {

        int id = stockChange.getId();

        if (containsStockChange(id)) {
            logger.info(MessageFormat.format("StockChange with id {0} does already exist!", id));
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write(id + ".id=" + id + "\n");
            bufferedReader.write(id + ".dayOfYear=" + stockChange.getDayOfYear() + "\n");
            bufferedReader.write(id + ".articleNumber=" + stockChange.getArticleNumber() + "\n");
            bufferedReader.write(id + ".oldAmount=" + stockChange.getOldAmount() + "\n");
            bufferedReader.write(id + ".newAmount=" + stockChange.getNewAmount() + "\n");
            bufferedReader.write(id + ".userID=" + stockChange.getUserID() + "\n");
        }
    }

    @Override
    public StockChange read(int id) throws IOException {
        return null;
    }

    @Override
    public List<StockChange> readAll() throws IOException {
        List<StockChange> stockChanges = new ArrayList<>();
        return stockChanges;
    }

    @Override
    public void update(StockChange article) throws IOException {
    }

    @Override
    public void delete(int id) throws IOException {
    }

    private boolean containsStockChange(int id) throws IOException {

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateArticles = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateArticles.add(line);
            });

            //if the list is not empty. a article is a duplicate
            return !duplicateArticles.isEmpty();
        }
    }
}
