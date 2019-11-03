package edu.uci.ics.zexis1.service.api_gateway.connectionpool;

import edu.uci.ics.zexis1.service.api_gateway.GatewayService;
import edu.uci.ics.zexis1.service.api_gateway.logger.ServiceLogger;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool {
    LinkedList<Connection> connections;
    String driver;
    String url;
    String username;
    String password;

    public ConnectionPool(int numCons, String driver, String url, String username, String password) {
        connections = new LinkedList<Connection>();
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        for(int i = 0; i < numCons; i++)
            connections.add(createConnection());
    }

    public Connection requestCon() {
        if(connections.isEmpty()){
            Connection connection = createConnection();
            return connection;
        }
        Connection connection = connections.pop();
        return connection;
    }
//
    public void releaseCon(Connection con) {
        connections.add(con);
    }

    private Connection createConnection() {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            return con;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.severe("Unable to connect to database.\n" + ExceptionUtils.exceptionStackTraceAsString(e));
        }
        return null;
    }
}
