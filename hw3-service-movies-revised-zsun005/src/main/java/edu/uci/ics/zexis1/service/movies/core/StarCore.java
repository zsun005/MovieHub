package edu.uci.ics.zexis1.service.movies.core;

import edu.uci.ics.zexis1.service.movies.MovieService;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class StarCore {
    private final static String idPrefix = "ss";
    private static int id = 1;

    public static BasicResponseModel addNewStar(AddStarRequestModel requestModel, String email){
        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 3);
        BasicResponseModel responseModel;
        int resultCode = verifyPrivilegeResponseModel.getResultCode();
        if(resultCode != 140){
            responseModel = new BasicResponseModel(141);
            return responseModel;
        }
        String name = requestModel.getName();
        Integer birthYear = requestModel.getBirthYear();
        if(starExist(name, birthYear)){
            responseModel = new BasicResponseModel(222);
            return responseModel;
        }

        String starId;
        int starIdMiddleZeros = 7;
        String starpostfix = String.valueOf(id++);
        starId = idPrefix;
        int zeros = 7 - starpostfix.length();
        for(int i = 0; i < zeros; i++)
            starId += "0";

        starId += starpostfix;

        String query = "INSERT INTO stars (id, name, birthYear) VALUES(?, ?, ?)";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, starId);
            ps.setString(2, name);
            if(birthYear == null)
                ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, birthYear);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ps.execute();
            responseModel = new BasicResponseModel(220);
            return responseModel;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
            responseModel = new BasicResponseModel(221);
            return responseModel;
        }

    }

    public static boolean starExist(String name, Integer birthYear){
        String query = "SELECT COUNT(*) AS num FROM stars WHERE name = ? AND birthYear = ?";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, name);
            if(birthYear == null)
                ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, birthYear);
            ServiceLogger.LOGGER.info("Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int num = rs.getInt("num");
                return num != 0;
            }
            return false;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Falied");
            return true;
        }
    }

    public static StarResponseModel searchStarById(String starId, String email){
        StarResponseModel responseModel;
        ArrayList<StarModel> stars = new ArrayList<>();
        String query = "SELECT * FROM stars WHERE id = ?";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, starId);
            ServiceLogger.LOGGER.info("Trying Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            StarModel star;
            if(rs.next()){
                String name = rs.getString("name");
                if(name == null){
                    responseModel = new StarResponseModel(213, null);
                    return responseModel;
                }
                Integer birthYear = rs.getInt("birthYear");
                star = new StarModel(starId, name, birthYear);
                stars.add(star);
            }
            responseModel = new StarResponseModel(213, stars);
            return responseModel;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
            responseModel = new StarResponseModel(-1, null);
            return responseModel;
        }
    }

    public static BasicResponseModel addStarToMovie(AddStarToMovieRequestModel requestModel, String email){
        BasicResponseModel responseModel;
        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 3);
        int resultCode = verifyPrivilegeResponseModel.getResultCode();
        if(resultCode != 140){
            responseModel = new BasicResponseModel(141);
            return responseModel;
        }
        String movieId = requestModel.getMovieid();
        String starId = requestModel.getStarid();
        if(!movieExist(movieId)){
            responseModel = new BasicResponseModel(211);
            return responseModel;
        }
        if(star_alreday_in_movie(movieId, starId)){
            responseModel = new BasicResponseModel(232);
            return responseModel;
        }

        String query = "INSERT INTO stars_in_movies (starId, movieId) VALUES(?, ?)";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, starId);
            ps.setString(2, movieId);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query Succeed");
            responseModel = new BasicResponseModel(230);
            return responseModel;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
            responseModel = new BasicResponseModel(231);
            return responseModel;
        }



    }
    public static boolean star_alreday_in_movie(String movieId, String starId){
        String query = "SELECT COUNT(*) FROM stars_in_movies WHERE movieId = ? AND starId = ?";
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ps.setString(2, starId);
            ServiceLogger.LOGGER.info("Trying Query " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                int num = rs.getInt("num");
                return num != 0;
            }

        } catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
        }

        return true;
    }
    public static boolean movieExist(String title)
    {
        String query = "SELECT COUNT(*) AS num FROM movies WHERE title = ?";

        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, title);
            ServiceLogger.LOGGER.info("Trying Query " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                int num = rs.getInt("num");
                return num != 0;
            }

        } catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
        }

        return true;

    }
}
