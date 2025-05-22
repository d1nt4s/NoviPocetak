package com.example.novipocetak.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/novipocetak2";
    private static final String USER = "d1ntes";
    private static final String PASSWORD = "qwerty123"; // prilagodi ako koristiš lozinku

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
