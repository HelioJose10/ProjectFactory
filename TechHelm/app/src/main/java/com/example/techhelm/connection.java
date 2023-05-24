package com.example.techhelm;

import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;

public class connection {
    private static final String url = "jdbc:mysql://<hostname>:<port>/<database>";
    private static final String user = "<username>";
    private static final String pass = "<password>";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}
