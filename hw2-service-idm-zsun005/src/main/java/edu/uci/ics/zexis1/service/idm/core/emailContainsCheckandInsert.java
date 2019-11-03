package edu.uci.ics.zexis1.service.idm.core;

import edu.uci.ics.zexis1.service.idm.IDMService;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.models.HashedPassUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class emailContainsCheckandInsert {
    public static boolean queryCheckEmail(String email) {
        try{
            String query = "SELECT count(*) AS num FROM users WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);

            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query" + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeed.");

            int num = 0;
            if(rs.next())
                num = rs.getInt("num");
//            int num = 0;
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
    public static boolean insertEmailtoDB(HashedPassUser hPassuser, int plevel, String salt, int status)
    {
        String hPass = hPassuser.getHashedPassword();
        String hUser = hPassuser.getEmail();
        try{
        String query = "INSERT INTO users (email, plevel, salt, pword, status)" +
                       " VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = IDMService.getCon().prepareStatement(query);
        ps.setString(1, hUser);
        ps.setInt(2, plevel);
        ps.setString(3, salt);
        ps.setString(4, hPass);
        ps.setInt(5, status);
        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ps.execute();
        return true;

    }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve student records.");
            e.printStackTrace();

        }
        return false;
    }
}
