package edu.uci.ics.zexis1.service.idm.core;

import edu.uci.ics.zexis1.service.idm.IDMService;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VerifyPrivilege {
    public static int getPrivilegeLevel(String email)
    {
        try
        {
            String query = "SELECT plevel AS plevel FROM users WHERE email = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1, email);
            int plevel = 0;
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                plevel = rs.getInt("plevel");
            return plevel;

        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to insert status.");
            e.printStackTrace();
            return 0;
        }
    }
}
