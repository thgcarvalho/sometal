package br.com.grandev.acesso.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import br.com.grandev.acesso.ReadProperties;

public class WebConnectionFactory {
    //static reference to itself
    private static WebConnectionFactory instance = new WebConnectionFactory();
    private Properties propDown = null;
     
    //private constructor
    private WebConnectionFactory() {
        try {
        	this.propDown = ReadProperties.load();
			System.out.println("jdbc classname=" + this.propDown.getProperty(ReadProperties.WEB_JDBC));
			System.out.println("jdbc connstr=" + this.propDown.getProperty(ReadProperties.WEB_CONNSTR));
			Class.forName(this.propDown.getProperty(ReadProperties.WEB_JDBC));
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
     
    private Connection createConnection() {
		Connection connection = null;
		try {
			System.out.println("Creating SQL connection...");
			connection = DriverManager.getConnection(
					this.propDown.getProperty(ReadProperties.WEB_CONNSTR),
					this.propDown.getProperty(ReadProperties.WEB_USER),
					this.propDown.getProperty(ReadProperties.WEB_PASSWORD));
		} catch (SQLException excp) {
			excp.printStackTrace();
			System.out.println("ERROR: Unable to Connect to Database.");
		}
        return connection;
    }   
     
    public static Connection getConnection() {
        return instance.createConnection();
    }
}

