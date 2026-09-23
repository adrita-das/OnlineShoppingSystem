package database;

import java.sql.Connection;
import java.sql.DriverManager;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    private static final Dotenv dotenv = Dotenv.configure()
            .directory(System.getProperty("user.dir"))
            .load();

    public static Connection getConnection() {

        String url = dotenv.get("DB_URL");
        String username = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        try {

            return DriverManager.getConnection(
                    url,
                    username,
                    password
            );

        } catch (Exception e) {

            System.out.println("Database connection failed");
            System.out.println(e.getMessage());

            return null;
        }
    }
}