package it.unisa.diatech.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DataSourceSingleton {

    
    private static DataSourceSingleton instance;

    
    private DataSource dataSource;

    
    private static final String JNDI_NAME = "java:comp/env/jdbc/diatech";

  
    private DataSourceSingleton() throws NamingException {
        Context initContext = new InitialContext();
        this.dataSource = (DataSource) initContext.lookup(JNDI_NAME);
    }

  
    public static synchronized DataSourceSingleton getInstance() throws NamingException {
        if (instance == null) {
            instance = new DataSourceSingleton();
        }
        return instance;
    }

  
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
