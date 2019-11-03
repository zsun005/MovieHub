package edu.uci.ics.zexis1.service.idm.core;

import edu.uci.ics.zexis1.service.idm.IDMService;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.security.Session;
import org.omg.PortableInterceptor.ACTIVE;

import java.sql.*;

public class SessionInsert {

    private static final int ACTIVTE = 1;
    private static final int CLOSED = 2;
    private static final int LOCKED = 3;
    private static final int REVOKED = 4;

    public static void insertNewSession(Session session, String email_to_insert, String sessionID)
    {
        String email = email_to_insert;
        Timestamp timeCreated = session.getTimeCreated();
        Timestamp lastUsed = session.getLastUsed();
        Timestamp exprTime = session.getExprTime();
        if(vertifyExistingSession(email))
        {
            alterStatusSession(email);
        }
        insertNewStatus(email, sessionID, timeCreated, lastUsed, exprTime);
    }
    public static void insertNewStatus(String email, String sessionID, Timestamp timeCreated, Timestamp lastUsed, Timestamp exprTime)
    {
        try
        {
            String query =
                    "INSERT INTO sessions(email, sessionID, timeCreated, lastUsed, exprTime, status)" +
                            "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, sessionID);
            ps.setTimestamp(3, timeCreated);
            ps.setTimestamp(4, lastUsed);
            ps.setTimestamp(5, exprTime);
            ps.setInt(6, ACTIVTE);
            ServiceLogger.LOGGER.info("Trying Query" + ps.toString());

            ps.execute();



        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to insert status.");
            e.printStackTrace();
        }
    }
    public static void alterStatusSession(String email)
    {
        try {
            String query =
                    "UPDATE sessions SET status = ? WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);

            ps.setInt(1, REVOKED);
            ps.setString(2, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ServiceLogger.LOGGER.info("Updating Query...");

            ps.execute();
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to update status.");
            e.printStackTrace();
        }

    }
    public static boolean vertifyExistingSession(String email)
    {
        try{
            String query = "SELECT count(*) AS num FROM sessions WHERE email = ? AND status = 1";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);

            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query:" + ps.toString());

            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeess.");

            int num = 0;
            if(rs.next())
                num = rs.getInt("num");
            ServiceLogger.LOGGER.info("Get num: " + num);

            if(num == 0)
                return false;
            else
                return true;

        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve records.");
            e.printStackTrace();
            return false;
        }
    }
}
