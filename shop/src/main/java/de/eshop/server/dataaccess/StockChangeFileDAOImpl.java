package de.eshop.server.dataaccess;

import de.eshop.shared.dataaccess.StockChangeDAO;
import de.eshop.shared.domain.UserType;
import de.eshop.shared.domain.events.StockChange;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockChangeFileDAOImpl implements StockChangeDAO {

    private static final Logger logger = Logger.getLogger(StockChangeFileDAOImpl.class.getName());

    private final String REG_EX = ".*=";

    public static final String DATA_PATH = "Data";
    public static final String STOCK_CHANGE = "Stockchange";

    private final File file = new File(STOCK_CHANGE);

    public StockChangeFileDAOImpl() {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error creating file \"" + STOCK_CHANGE + "\"", e);
            }
        }
    }

    @Override
    public synchronized void create(StockChange stockChange) throws IOException {

        List<StockChange> stockChanges = readAll();

        int id = stockChanges.stream()
                .mapToInt(StockChange::getId)
                .max()
                .orElse(0) + 1;

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write(id + ".id=" + id + "\n");
            bufferedReader.write(id + ".dayOfYear=" + stockChange.getDayOfYear() + "\n");
            bufferedReader.write(id + ".articleNumber=" + stockChange.getArticleNumber() + "\n");
            bufferedReader.write(id + ".oldAmount=" + stockChange.getOldAmount() + "\n");
            bufferedReader.write(id + ".newAmount=" + stockChange.getNewAmount() + "\n");
            bufferedReader.write(id + ".userID=" + stockChange.getUserID() + "\n");
            bufferedReader.write(id + ".userType=" + stockChange.getUserType().name() + "\n");
        }
    }

    @Override
    public synchronized StockChange read(int id) throws IOException {

        if (!containsStockChange(id))
            return null;

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateStockChange = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateStockChange.add(line);
            });

            final String regEx = ".*=";

            int identifier = Integer.parseInt(duplicateStockChange.get(0).replaceAll(regEx, ""));
            int dayOfYear = Integer.parseInt(duplicateStockChange.get(1).replaceAll(regEx, ""));
            int articleNumber = Integer.parseInt(duplicateStockChange.get(2).replaceAll(regEx, ""));
            int oldAmount = Integer.parseInt(duplicateStockChange.get(3).replaceAll(regEx, ""));
            int newAmount = Integer.parseInt(duplicateStockChange.get(4).replaceAll(regEx, ""));
            int userID = Integer.parseInt(duplicateStockChange.get(5).replaceAll(regEx, ""));
            UserType userType = UserType.valueOf(duplicateStockChange.get(6).replaceAll(regEx, ""));

            return new StockChange(identifier, dayOfYear, articleNumber, oldAmount, newAmount, userID, userType);
        }
    }

    @Override
    public synchronized List<StockChange> readAll() throws IOException {

        List<StockChange> stockChanges = new ArrayList<>();
        Set<Integer> stockChangeIDS = new LinkedHashSet<>();

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            AtomicInteger counter = new AtomicInteger();

            bufferedReader.lines().forEach(line -> {
                if (counter.getAndIncrement() % 7 == 0) {
                    stockChangeIDS.add(Integer.parseInt(line.replaceAll(REG_EX, "")));
                }
            });

            stockChangeIDS.forEach(stockChangeID -> {
                try {
                    stockChanges.add(read(stockChangeID));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return stockChanges;
    }

    @Override
    public synchronized void update(StockChange article) throws IOException {
    }

    @Override
    public synchronized void delete(int id) throws IOException {
    }

    private synchronized boolean containsStockChange(int id) throws IOException {

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateStockChanges = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateStockChanges.add(line);
            });

            return !duplicateStockChanges.isEmpty();
        }
    }

    @Override
    public synchronized int readLastCreatedBulkArticleID() {

        List<StockChange> stockChanges = null;

        try {
            stockChanges = readAll();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        int id = 1;

        try {
            id = stockChanges.getLast().getId();
        } catch (NoSuchElementException ignored) {
        }
        return id;
    }
}
