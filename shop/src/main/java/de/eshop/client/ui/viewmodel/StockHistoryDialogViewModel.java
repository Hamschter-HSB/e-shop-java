package de.eshop.client.ui.viewmodel;

import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.events.StockChange;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class StockHistoryDialogViewModel {

    private static final Logger logger = Logger.getLogger(StockHistoryDialogViewModel.class.getName());

    private final ClientDataPersisterImpl clientDataPersisterImpl;

    public StockHistoryDialogViewModel(ClientDataPersisterImpl clientDataPersisterImpl) {
        this.clientDataPersisterImpl = clientDataPersisterImpl;
    }

    public HashMap<Integer, BulkArticle> getArticleNumberToArticle() {


        HashMap<Integer, BulkArticle> allArticlesHashMap = new HashMap<>();
        clientDataPersisterImpl.readAllBulkArticles().forEach(article -> allArticlesHashMap.put(article.getArticleNumber(), article));
        return allArticlesHashMap;
    }

    private Map<String, Integer> graphData(int articleID) {

        Map<String, Integer> data = new LinkedHashMap<>();
        List<StockChange> allStockChanges = clientDataPersisterImpl.readAllStockChanges().stream().toList();

        AtomicInteger currentDayOfYear = new AtomicInteger(LocalDateTime.now().getDayOfYear());
        List<StockChange> allStockChangesFromLast30Days = new ArrayList<>();

        allStockChanges.forEach(stockChange -> {
            if (stockChange.getArticleNumber() == articleID && (currentDayOfYear.get() - stockChange.getDayOfYear()) <= 30)
                allStockChangesFromLast30Days.add(stockChange);
        });

        int bestand = clientDataPersisterImpl.readBulkArticle(articleID).getStock();

        for (int i = 1; i <= 30; i++) {

            // Für CurrentDayOfYear alle stockchanges finden, die die gleiche DayOfYearHaben
            List<StockChange> allStockChangesOfCurrentDay = new ArrayList<>();
            allStockChangesFromLast30Days.forEach(stockChange -> {
                if (stockChange.getDayOfYear() == currentDayOfYear.get()) {
                    allStockChangesOfCurrentDay.add(stockChange);
                }
            });

            AtomicInteger overallStockChangeForCurrentDay = new AtomicInteger(0);
            allStockChangesOfCurrentDay.forEach(stockChange -> {
                overallStockChangeForCurrentDay.set(overallStockChangeForCurrentDay.get() + (stockChange.getNewAmount() - stockChange.getOldAmount()));
            });

            bestand -= overallStockChangeForCurrentDay.get();

            // Die Summe an den DayOfYear an der Stelle i setzen

            data.put(String.valueOf(i), bestand);
            currentDayOfYear.decrementAndGet();
        }
        return data;
    }

    public JPanel createGraph(int articleID) {
        return new SimpleGraph(graphData(articleID));
    }
}

class SimpleGraph extends JPanel {

    private final Map<String, Integer> data;

    public SimpleGraph(Map<String, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(800, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int padding = 60;
        int labelPadding = 20;

        // Achsen
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X
        g2.drawLine(padding, padding, padding, height - padding); // Y

        // Achsenbeschriftung
        g2.drawString("Day", width / 2, height - 10);
        g2.drawString("Stock", 10, height / 2);

        // Y-Skalierung
        int maxValue = data.values().stream().mapToInt(i -> i).max().orElse(1000);
        int yDivisions = 5;

        for (int i = 0; i <= yDivisions; i++) {
            int y = height - padding - i * (height - 2 * padding) / yDivisions;
            int value = i * maxValue / yDivisions;
            g2.drawLine(padding - 5, y, padding + 5, y);
            g2.drawString(String.valueOf(value), 15, y + 5);
        }

        // X-Skalierung (z. B. jeden 5. Tag beschriften)
        int xStep = (width - 2 * padding) / (data.size() - 1);
        int i = 0;
        int prevX = 0, prevY = 0;

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int x = padding + i * xStep;
            int y = height - padding - (entry.getValue() * (height - 2 * padding) / maxValue);

            // Punkt & Linie
            g2.fillOval(x - 3, y - 3, 6, 6);
            if (i > 0) {
                g2.drawLine(prevX, prevY, x, y);
            }

            // Nur jeden 5. Tag beschriften (optional)
            if ((i + 1) % 5 == 0 || i == 0 || i == data.size() - 1) {
                g2.drawString(entry.getKey(), x - 5, height - padding + labelPadding);
            }

            // Bestand anzeigen
            g2.drawString(String.valueOf(entry.getValue()), x - 10, y - 10);

            prevX = x;
            prevY = y;
            i++;
        }
    }
}
