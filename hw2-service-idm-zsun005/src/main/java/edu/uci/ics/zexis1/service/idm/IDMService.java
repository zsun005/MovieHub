package edu.uci.ics.zexis1.service.idm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import edu.uci.ics.zexis1.service.idm.configs.Configs;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.models.ConfigsModel;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.internal.util.ExceptionUtils;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IDMService {
    public static IDMService idmService;

    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection con = null;
    private static Configs configs = new Configs();

    public static void main(String[] args) {
        idmService = new IDMService();
        idmService.initService(args);
    }

    private void initService(String[] args) {
        // Validate arguments
        validateArguments(args);
        // Exec the arguments
        execArguments(args);
        // Initialize logging
        initLogging();
        ServiceLogger.LOGGER.config("Starting service...");
        configs.currentConfigs();
        // Connect to database
        connectToDatabase();
        // Initialize HTTP server
        initHTTPServer();

        if (con != null) {
            ServiceLogger.LOGGER.config("Service initialized.");
        } else {
            ServiceLogger.LOGGER.config("Service initialized with error(s)");
        }
    }

    private void validateArguments(String[] args) {
        boolean isConfigOptionSet = false;
        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "--default":
                case "-d":
                    if (i + 1 < args.length) {
                        exitAppFailure("Invalid arg after " + args[i] + " option: " + args[i + 1]);
                    }
                case "--config":
                case "-c":
                    if (!isConfigOptionSet) {
                        isConfigOptionSet = true;
                        ++i;
                    } else {
                        exitAppFailure("Conflicting configuration file arguments.");
                    }
                    break;

                default:
                    exitAppFailure("Unrecognized argument: " + args[i]);
            }
        }
    }

    private void execArguments(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                switch (args[i]) {
                    case "--config":
                    case "-c":
                        // Config file specified. Load it.
                        getConfigFile(args[i + 1]);
                        ++i;
                        break;
                    case "--default":
                    case "-d":
                        System.err.println("Default config options selected.");
                        configs = new Configs();
                        break;
                    default:
                        exitAppFailure("Unrecognized argument: " + args[i]);
                }
            }
        } else {
            System.err.println("No config file specified. Using default values.");
            configs = new Configs();
        }
    }

    private void getConfigFile(String configFile) {
        try {
            System.err.println("Config file name: " + configFile);
            configs = new Configs(loadConfigs(configFile));
            System.err.println("Configuration file successfully loaded.");
        } catch (NullPointerException e) {
            System.err.println("Config file not found. Using default values.");
            configs = new Configs();
        }
    }

    private ConfigsModel loadConfigs(String file) {
        System.err.println("Loading configuration file...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfigsModel configs = null;

        try {
            configs = mapper.readValue(new File(file), ConfigsModel.class);
        } catch (IOException e) {
            exitAppFailure("Unable to load configuration file.");
        }
        return configs;
    }

    private void initLogging() {
        try {
            ServiceLogger.initLogger(configs.getOutputDir(), configs.getOutputFile());
        } catch (IOException e) {
            exitAppFailure("Unable to initialize logging.");
        }
    }

    private void initHTTPServer() {
        ServiceLogger.LOGGER.config("Initializing HTTP server...");
        String scheme = configs.getScheme();
        String hostName = configs.getHostName();
        int port = configs.getPort();
        String path = configs.getPath();

        try {
            ServiceLogger.LOGGER.config("Building URI from configs...");
            URI uri = UriBuilder.fromUri(scheme + hostName + path).port(port).build();
            ServiceLogger.LOGGER.config("Final URI: " + uri.toString());
            ResourceConfig rc = new ResourceConfig().packages("edu.uci.ics.zexis1.service.idm.resources");
            ServiceLogger.LOGGER.config("Set Jersey resources.");
            rc.register(JacksonFeature.class);
            ServiceLogger.LOGGER.config("Set Jackson as serializer.");
            ServiceLogger.LOGGER.config("Starting HTTP server...");
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);
            server.start();
            ServiceLogger.LOGGER.config("HTTP server started.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
//    private void connectToDatabase() {
//        ServiceLogger.LOGGER.config("Connecting to database...");
//        String driver = "";
//
//        if (!configs.isDbConfigValid()) {
//            ServiceLogger.LOGGER.config("Database configurations not valid. Cannot connect to database.");
//            return;
//        }
//
//        switch (configs.getDbDriver().toLowerCase()) {
//            case "mysql":
//                ServiceLogger.LOGGER.config("Using MySQL driver: " + MYSQL_DRIVER);
//                driver = MYSQL_DRIVER;
//                break;
//
//            default:
//                ServiceLogger.LOGGER.severe("No database driver found. Cannot connect to a database.");
//                return;
//        }
//
//        try {
////            driver = configs.getDbDriver().toLowerCase();
//            ServiceLogger.LOGGER.config("Using driver: " + driver);
//            Class.forName(driver);
//            ServiceLogger.LOGGER.config("Database URL: " + configs.getDbUrl());
//            con = DriverManager.getConnection(configs.getDbUrl(), configs.getDbUsername(), configs.getDbPassword());
//            ServiceLogger.LOGGER.config("Connected to database: " + configs.getDbUrl());
//        } catch (ClassNotFoundException | SQLException | NullPointerException e) {
//            ServiceLogger.LOGGER.severe("Unable to connect to database.\n" + ExceptionUtils.exceptionStackTraceAsString(e));
//        }
//    }
private void connectToDatabase() {
    ServiceLogger.LOGGER.config("Connecting to database...");
    // Set the host that the database is running on (localhost, matt-smith-v4.ics.uci.edu, etc...)
    String hostName = configs.getDbHostname();
    // Set the port that your program's MySQL traffic will use. Default is 3306. This example code is using 3307
    // because it assumes you will be using an SSH tunnel to connect to the database, and you will be running your
    // program from your laptop, and not on Openlab. Check piazza and the homework documentation for details.
    int port = configs.getDbPort();
    // Set the user account your program will use to connect to the desired database.
    String username = configs.getDbUsername();
    // Set the corresponding password for the above user account
    String password = configs.getDbPassword();
    // Set the name of the database to connect to
    String dbName = configs.getDbName();
    // Set the query string containing configurations for THIS connection to the database. The below string is 100%
    // ABSOLUTELY NECESSARY to connect to any MySQL server on Openlab. This is not strictly needed for your MySQL
    // server on your local machine.
    String settings = configs.getDbSetting();
    // This is the database driver our program will be using to connect to a database. Your program could connect to
    // any number of types of databases, including Oracle, DB2, or Sybase. We're using MySQL, and so must use the
    // MySQL driver.
    String driver = configs.getDbDriver();

    try {
        // The JDBC driver must be registered in memory before your program can use it. Registering the driver is
        // the process by which the driver's class file is loaded into memory, so it can be utilized as an
        // implementation of the JDBC interfaces. The registration must happen only once in your program. The most
        // common way to register a driver is to use Java's Class.forName() method to dynamically load the driver's
        // class file into memory, which automatically registers it. This method is the most preferred way because
        // it allows you to make the driver registration configurable and portable.
        Class.forName(driver);

        // A url for the database you wish to connect to must be supplied to the Connection class in its constructor.
        // The URL format is dependent on the driver being used in your program. We are using the MySQL driver, so
        // the url must be of the form "jdbc:mysql://hostname/databaseName. The URL must be constructed from the
        // parameters supplied in our YAML configuration file.
        String url = "jdbc:mysql://" + hostName + ":" + port + "/" + dbName + settings;
        ServiceLogger.LOGGER.config("Database URL: " + url);

        // Once the driver has been loaded into memory, and the URL has been constructed, we can now establish a
        // connection using the DriverManager.getConnection() method. In this example, we are creating only a single
        // connection to our database. This means that every request that comes to our web application must share
        // only a single connection for executing queries. This is obviously a huge bottleneck in the design. For
        // right now, this will suffice for our programs. But later in the course, we will be introducing you to
        // "connection pooling", which is a means of creating and distributing access to many database connections
        // among the functions of our web application. It also worth mentioning that under NORMAL operating conditions,
        // the best practice is to explicitly close all connections to the database to end a database session. Java's
        // garbage collection will handle this automatically, but it is considered VERY poor practice. For right now,
        // we are allowing Java to handle closing stale connections.
        con = DriverManager.getConnection(url, username, password);
        ServiceLogger.LOGGER.config("Connected to database: " + con.toString());
    } catch (Exception e) {
        // Listing the exception types individually allows you to log exactly what exception was thrown, and call
        // different methods to handle the exception if so desired.
        e.printStackTrace();
        if (e instanceof ClassCastException) {
            ServiceLogger.LOGGER.warning("Unable to load class for driver \"" + driver + "\".");
        }
        if (e instanceof SQLException) {
            ServiceLogger.LOGGER.warning("Access problem while loading driver \"" + driver + "\" into memory.");
        }
        if (e instanceof NullPointerException) {
            ServiceLogger.LOGGER.warning("Unable to instantiate driver: " + driver);
        }
        ServiceLogger.LOGGER.warning("Connection to database " + dbName + " failed.");
    }
}
//     jdbc:mysql://localhost:3306/?user=cs122b_db134

    private void exitAppFailure(String message) {
        System.err.println("ERROR: " + message);
        System.err.println("Usage options: ");
        System.err.println("\tSpecify configuration file:");
        System.err.println("\t\t--config [file]");
        System.err.println("\t\t-c");
        System.err.println("\tUse default configuration:");
        System.err.println("\t\t--default");
        System.err.println("\t\t-d");
        System.exit(-1);
    }

    public static Connection getCon() {
        return con;
    }

    public static Configs getConfigs() {
        return configs;
    }
}