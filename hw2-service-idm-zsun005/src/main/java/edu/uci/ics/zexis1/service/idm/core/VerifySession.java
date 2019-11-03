package edu.uci.ics.zexis1.service.idm.core;

import edu.uci.ics.zexis1.service.idm.IDMService;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.security.Session;


import java.sql.*;

public class VerifySession {
    /*
    * TODO: Check if status = 1 is needed to query
    *  */
    private static int ACTIVITE = 1;
    private static int CLOSED = 2;
    private static int EXPIRED = 3;
    private static int REVOKED = 4;
    private static int DO_NOT_FIND_USERS = -1;
    private static int DO_NOT_FIND_SESSIONS = -2;
    private static long timeout = IDMService.getConfigs().getTimeout();
    private static long expiration = IDMService.getConfigs().getExpiration();
    public static String newSessionID = null;

    public static int updateStatusAndTimestamp(Session session, String email, String sessionID)
    {
        int status_to_return = -1;

        Timestamp currentTime = session.getLastUsed();


        try {
            String queryInfo =
                    "SELECT status AS status, lastUsed, exprTime FROM sessions WHERE sessionID = ?";
            PreparedStatement psInfo = IDMService.getCon().prepareStatement(queryInfo);
            psInfo.setString(1, sessionID);

            ServiceLogger.LOGGER.info("Trying Query: " + psInfo.toString());

            ResultSet reInfo = psInfo.executeQuery();
            ServiceLogger.LOGGER.info("Query Succeed");
            int status = 0;

            Timestamp lastUsed = null;
            Timestamp exprTime = null;

            if (reInfo.next()) {
                status = reInfo.getInt("status");
                lastUsed = reInfo.getTimestamp("lastUsed");
                exprTime = reInfo.getTimestamp("exprTime");
            }

            if(status == 0)
                return -2;
            switch (status)
            {
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
            }

            ServiceLogger.LOGGER.info("Get lastUsed: " + lastUsed);
            ServiceLogger.LOGGER.info("Get Expr: " + exprTime);





            long last = currentTime.getTime() - lastUsed.getTime();
            if (currentTime.getTime() >= exprTime.getTime()) {
                // TODO: Expriation update status to be EXPIRIED.
                String query = "UPDATE sessions SET status = ?," +
                        "lastUsed = ? WHERE email = ? AND sessionID = ?";
                PreparedStatement ps = IDMService.getCon().prepareStatement(query);
                ps.setInt(1, EXPIRED);
                ps.setTimestamp(2, currentTime);
                ps.setString(3, email);
                ps.setString(4, sessionID);
                ServiceLogger.LOGGER.info("Trying Query: " + ps.toString());
                ps.execute();
                ServiceLogger.LOGGER.info("SessionID: " + sessionID + " EXPIRED");
                return EXPIRED;

            }
            else if (last > timeout) {
                // TODO: timeout update status to be REVOKE
                String query = "UPDATE sessions SET status = ?," +
                        "lastUsed = ? WHERE email = ? AND sessionID = ?";
                PreparedStatement ps = IDMService.getCon().prepareStatement(query);
                ps.setInt(1, REVOKED);
                ps.setTimestamp(2, currentTime);
                ps.setString(3, email);
                ps.setString(4, sessionID);
                ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
                ps.execute();
                ServiceLogger.LOGGER.info("SessionID: " + sessionID +  " REVOKED");
                return REVOKED;
            }
            else
            {
                if(lastUsed.getTime() + timeout > exprTime.getTime()){
                    Session newSession = Session.createSession(email);
                    newSessionID = newSession.getSessionID().toString();

                    String newQuery = "INSERT INTO sessions (email, sessionID, timeCreated, lastUsed, exprTime, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement newPs = IDMService.getCon().prepareStatement(newQuery);
                    newPs.setString(1, email);
                    newPs.setString(2, newSessionID);
                    newPs.setTimestamp(3, newSession.getTimeCreated());
                    newPs.setTimestamp(4, newSession.getLastUsed());
                    newPs.setTimestamp(5, newSession.getExprTime());
                    newPs.setInt(6, 1);
                    newPs.execute();
                    ServiceLogger.LOGGER.info("SessionID: " + sessionID + " REACTIVITE");
                    return ACTIVITE;

                }
                String query = "UPDATE sessions SET " +
                        "lastUsed = ? WHERE email = ? AND sessionID = ?";
                PreparedStatement ps = IDMService.getCon().prepareStatement(query);
                ps.setTimestamp(1, currentTime);
                ps.setString(2, email);
                ps.setString(3, sessionID);
                ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
                ps.execute();
                ServiceLogger.LOGGER.info("SessionID: " + sessionID + " ACTIVE");
                return ACTIVITE;
            }
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to update status.");
            e.printStackTrace();
            return status_to_return;
        }

    }


    public static int findStatus(String email, String sessionID)
    {
        try
        {

            String query1 =
                    "SELECT status AS status FROM sessions " +
                            "WHERE email = ? AND sessionID = ? ";
            PreparedStatement ps1 = IDMService.getCon().prepareStatement(query1);

            ps1.setString(1, email);
            ps1.setString(2, sessionID);

            ServiceLogger.LOGGER.info("Trying query: " + ps1.toString());

            ResultSet rs1 = ps1.executeQuery();

            ServiceLogger.LOGGER.info("Query Succeed.");

            int status = -2;
            if(rs1.next())
                status = rs1.getInt("status");

            return status;



        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve records.");
            e.printStackTrace();
            return -1;
        }

    }
    public static int findUsersAndSessionID(String email, String sessionID)
    {
        try {
            String query =
                    "SELECT Count(*) AS num FROM sessions WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);

            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying Query:" + ps.toString());

            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeed.");

            int num = 0;
            if (rs.next())
                num = rs.getInt("num");

            ServiceLogger.LOGGER.info("Find num: " + num);

            if (num == 0)
                return DO_NOT_FIND_USERS;
            ServiceLogger.LOGGER.info("Find Users... Verifying session");


            String query1 =
                    "SELECT count(*) AS num FROM sessions " +
                            "WHERE email = ? AND sessionID = ? ";
            PreparedStatement ps1 = IDMService.getCon().prepareStatement(query1);

            ps1.setString(1, email);
            ps1.setString(2, sessionID);

            ServiceLogger.LOGGER.info("Trying Query: " + ps1.toString());

            ResultSet rs1 = ps1.executeQuery();

            ServiceLogger.LOGGER.info("Query Succeed.");

            num = 0;
            if(rs1.next())
                num = rs1.getInt("num");

            if(num == 0)
                return DO_NOT_FIND_SESSIONS;
            else
                return num;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve records.");
            e.printStackTrace();
            return DO_NOT_FIND_SESSIONS;
        }
    }
}
