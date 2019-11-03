package edu.uci.ics.zexis1.service.idm.core;

import edu.uci.ics.zexis1.service.idm.IDMService;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IDMLoginCore {
    public static boolean emailAndPassCheck(String email, String password)
    {
        try{
            String query = "SELECT count(*) AS num FROM users WHERE email = ? AND pword = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);

            ps.setString(1, email);
            ps.setString(2, password);

            ServiceLogger.LOGGER.info("Trying query" + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeed.");

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
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve student records.");
            e.printStackTrace();
            return false;
        }
    }
    public static String queryCheckEmail(String email) {
        try{
            String query = "SELECT salt AS salt FROM users WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);

            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query" + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeed.");

            String salt = "";
            if(rs.next())
                salt = rs.getString("salt");
//            int num = 0;
            ServiceLogger.LOGGER.info("Get salt: " + salt);

            return salt;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve student records.");
            e.printStackTrace();
            return "";
        }
    }
}
