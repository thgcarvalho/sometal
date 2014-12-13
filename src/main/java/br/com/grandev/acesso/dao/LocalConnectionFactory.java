package br.com.grandev.acesso.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LocalConnectionFactory {
    //static reference to itself
    private static LocalConnectionFactory instance = new LocalConnectionFactory();
//    public static final String URL = "jdbc:postgresql://localhost:5432/grandevc_sometal";
//    public static final String USER = "postgres";
//    public static final String PASSWORD = "postgres";
//    public static final String DRIVER_CLASS = "org.postgresql.Driver"; 
    
    public static final String URL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" + "C:\\Program Files (x86)\\Gerenciador de Inners 5\\INNER5.MDB";
    public static final String USER = "datatop";
    public static final String PASSWORD = "datatop";
    public static final String DRIVER_CLASS = "sun.jdbc.odbc.JdbcOdbcDriver"; 
     
    //private constructor
    private LocalConnectionFactory() {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
     
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
        }
        return connection;
    }   
     
    public static Connection getConnection() {
        return instance.createConnection();
    }
}

