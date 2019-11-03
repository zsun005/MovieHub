package edu.uci.ics.zexis1.service.movies.core;

import edu.uci.ics.zexis1.service.movies.MovieService;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.BasicResponseModel;
import edu.uci.ics.zexis1.service.movies.models.VerifyPrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class removeMovie {

    public static BasicResponseModel removeMovie(String movieId, String email){
        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 3);
        BasicResponseModel responseModel;
        int resultCode = verifyPrivilegeResponseModel.getResultCode();
        if(resultCode != 140){
            responseModel = new BasicResponseModel(141);
            return responseModel;
        }
        if(movieExist(movieId, 1)){
            responseModel = new BasicResponseModel(242);
            return responseModel;
        }
        if(!(movieExist(movieId, 1) || movieExist(movieId, 0)))
        {
            responseModel = new BasicResponseModel(241);
            return responseModel;
        }
        String query = "UPDATE movies SET hidden = 1 WHERE id = ?";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ServiceLogger.LOGGER.info("Try Query " + ps.toString());
            ps.execute();
            responseModel = new BasicResponseModel(240);
            return responseModel;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Remove Movie Failed");
        }
        responseModel = new BasicResponseModel(241);
        return responseModel;

    }




    public static boolean movieExist(String movieId, int hidden)
    {
        String query = "SELECT COUNT(*) AS num FROM movies WHERE id = ? AND hidden = ?";

        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ps.setInt(2, hidden);
            ServiceLogger.LOGGER.info("Trying Query " + ps.toString());
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                int num = rs.getInt("num");
                if(num == 0) return false;
                return true;
            }

        } catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
        }

        return true;

    }
}
