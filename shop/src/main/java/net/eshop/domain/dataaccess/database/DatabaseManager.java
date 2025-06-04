package net.eshop.domain.dataaccess.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseManager {

    private static final String DB_PATH = "eshop.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    private final Connection connection;

    public DatabaseManager() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL);

        if (connection != null && connection.isValid(2)) {
            System.out.println("Verbindung erfolgreich!");
        } else {
            System.out.println("Verbindung fehlgeschlagen!");
        }

        creatDatabase();
    }

    private void creatDatabase() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS articles (articleNumber INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, stock INTEGER)");

//        stmt.executeUpdate("INSERT INTO articles (name, description, stock) " +
//                "VALUES ('Test', 'Testbeschreibung', 20)");
    }



}
