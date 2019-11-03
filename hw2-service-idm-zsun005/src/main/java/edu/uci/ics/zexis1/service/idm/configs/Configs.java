package edu.uci.ics.zexis1.service.idm.configs;

import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.models.ConfigsModel;


public class Configs {

    // Service configs
    private String scheme;
    private String hostName;
    private int port;
    private String path;

    //Logger configs
    private String outputDir;
    private String outputFile;
//    private Yaml yaml;

    // database configs:
    private String dbUsername;
    private String dbPassword;
    private String dbHostname;
    private int dbPort;
    private String dbDriver;
    private String dbName;
    private String dbSetting;

    // session configs:
    private long timeout;
    private long expiration;


    public Configs(){}
    public Configs(ConfigsModel cm) throws NullPointerException{
        if(cm == null){
            throw new NullPointerException("Unable to create Configs from ConfigsModel.");
        }
        else{
            scheme = cm.getServiceConfig().get("scheme");
            if(scheme == null){
//                scheme = DEFAULT_SCHEME;
                System.err.println("Scheme not found in configuration file. Using default.");
            } else {
                System.err.println("Scheme: " + scheme);
            }

            hostName = cm.getServiceConfig().get("hostName");
            if(hostName == null){
//                hostName = DEFAULT_HOSTNAME;
                System.err.println("Hostname not found in configuration file. Using default.");
            } else {
                System.err.println("Hostname: " + hostName);
            }

            port = Integer.parseInt(cm.getServiceConfig().get("port"));
            if(port == 0){
//                port = DEFAULT_PORT;
                System.err.println("Poart is not within valid range. Using default.");
            } else if (port < 1024 || port > 65536){
//                port = DEFAULT_PORT;
                System.err.println("Port is not within valid range. Using default.");
            } else {
                System.err.println("Port: " + port);
            }

            path = cm.getServiceConfig().get("path");
            if(path == null) {
//                path = DEFAULT_PATH;
                System.err.println("Path not found in configuration file. Using default.");
            } else {
                System.err.println("Path: " + path);
            }

            dbUsername = cm.getDatabaseConfig().get("dbUsername");
            if(dbUsername == null){
//                scheme = DEFAULT_SCHEME;
                System.err.println("dbUsername not found in configuration file. Using default.");
            } else {
                System.err.println("dbUsername: " + dbUsername);
            }
            dbPassword = cm.getDatabaseConfig().get("dbPassword");
            if(dbPassword == null){
//                scheme = DEFAULT_SCHEME;
                System.err.println("dbPassword not found in configuration file. Using default.");
            } else {
                System.err.println("dbPassword: " + dbPassword);
            }

            dbHostname = cm.getDatabaseConfig().get("dbHostname");
            if(dbHostname == null){
//                scheme = DEFAULT_SCHEME;
                System.err.println("dbHostname not found in configuration file. Using default.");
            } else {
                System.err.println("dbHostname: " + dbHostname);
            }
            dbPort = Integer.parseInt(cm.getDatabaseConfig().get("dbPort"));
            if(dbPort == 0){
//                port = DEFAULT_PORT;
                System.err.println("dbPort is not within valid range. Using default.");
            } else if (dbPort < 1024 || dbPort > 65536){
//                port = DEFAULT_PORT;
                System.err.println("dbPort is not within valid range. Using default.");
            } else {
                System.err.println("dbPort: " + dbPort);
            }
            dbDriver = cm.getDatabaseConfig().get("dbDriver");
            if(dbDriver == null){
//                scheme = DEFAULT_SCHEME;
                System.err.println("dbDriver not found in configuration file. Using default.");
            } else {
                System.err.println("dbDriver: " + dbDriver);
            }

            dbName = cm.getDatabaseConfig().get("dbName");
            if(dbName == null){
//                scheme = DEFAULT_SCHEME;
                System.err.println("dbName not found in configuration file. Using default.");
            } else {
                System.err.println("dbName: " + dbName);
            }
            dbSetting = cm.getDatabaseConfig().get("dbSettings");
            if(dbSetting == null){
//                scheme = DEFAULT_SCHEME;
                System.err.println("dbSetting not found in configuration file. Using default.");
            } else {
                System.err.println("dbSettings: " + dbSetting);
            }
            // Set logger configs
            outputDir = cm.getLoggerConfig().get("outputDir");
            if (outputDir == null) {
                System.err.println("Logging output directory not found in configuration file. Using default.");
            } else {
                System.err.println("Logging output directory: " + outputDir);
            }

            outputFile = cm.getLoggerConfig().get("outputFile");
            if (outputFile == null) {
                System.err.println("Logging output file not found in configuration file. Using default.");
            } else {
                System.err.println("Logging output file: " + outputFile);
            }

            timeout = Long.parseLong(cm.getSessionConfig().get("timeout"));


            expiration = Long.parseLong(cm.getSessionConfig().get("expiration"));


            /*
    * databaseConfig:
    dbUsername: cs122b_db134
      dbPassword: b~mzRzwNdWdE
      dbHostname: localhost
      dbPort: 3307
      dbDriver: mysql
      dbName: basicdb
      * jdbc:mysql://matt-smith-v4.ics.uci.edu/cs122b_db134?autoReconnect=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST
  * */

        }

    }
    public void currentConfigs(){
        ServiceLogger.LOGGER.config("Scheme: " + scheme);
        ServiceLogger.LOGGER.config("Hostname: " + hostName);
        ServiceLogger.LOGGER.config("Port: " + port);
        ServiceLogger.LOGGER.config("Path" + path);
        ServiceLogger.LOGGER.config("Logger output directory: " + outputDir);
        ServiceLogger.LOGGER.config("Logger output file: " + outputFile);
        ServiceLogger.LOGGER.config("dbUsername: " + dbUsername);
        ServiceLogger.LOGGER.config("dbPassword: " + dbPassword);
        ServiceLogger.LOGGER.config("dbHostname: " + dbHostname );
        ServiceLogger.LOGGER.config("dbPort: " + dbPort);
        ServiceLogger.LOGGER.config("dbDriver: " + dbDriver);
        ServiceLogger.LOGGER.config("dbName: " + dbName);
    }

    public String getScheme() {
        return scheme;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbHostname() {
        return dbHostname;
    }

    public void setDbHostname(String dbHostname) {
        this.dbHostname = dbHostname;
    }

    public int getDbPort() {
        return dbPort;
    }

    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbSetting() {
        return dbSetting;
    }

    public void setDbSetting(String dbSetting) {
        this.dbSetting = dbSetting;
    }

    public long getTimeout(){return timeout;}

    public long getExpiration(){return expiration;}

    public void setTimeout(long timeout){this.timeout = timeout;}

    public void setExpiration(long expiration){this.expiration = expiration;}


}
