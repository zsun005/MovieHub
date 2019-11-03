package edu.uci.ics.zexis1.service.movies.core;

import edu.uci.ics.zexis1.service.movies.MovieService;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.AddRatingRequestModel;
import edu.uci.ics.zexis1.service.movies.models.BasicResponseModel;
import edu.uci.ics.zexis1.service.movies.models.RatingModel;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingCore {
    public static BasicResponseModel updateRating(AddRatingRequestModel requestModel){
        BasicResponseModel responseModel;

        String movieId = requestModel.getId();
        Float rating = requestModel.getRating();

        if(!searchMovieByID(movieId)){
            responseModel = new BasicResponseModel(211);
            return responseModel;
        }
        RatingModel ratingModel = getRating(movieId);
        Integer numVotes = ratingModel.getNumVotes();
        Float movieRating = ratingModel.getRating();
        if(numVotes == null){
            insertRating(movieId, rating, 1);
        }
        else{
            Float newRating = (numVotes * movieRating + rating) / (numVotes + 1);
            numVotes++;
            updateRating(movieId, newRating, numVotes);
        }
        responseModel = new BasicResponseModel(250);
        return responseModel;



    }
    public static RatingModel getRating(String movieId){
        String query = "SELECT numVotes, rating FROM ratings WHERE movieId = ?";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ServiceLogger.LOGGER.info("Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            RatingModel ratingModel;
            if(rs.next()){
                 Integer numVotes = rs.getInt("numVotes");
                 Float rating = rs.getFloat("rating");
                 ratingModel = new RatingModel(movieId, rating, numVotes);
                 return ratingModel;
            }

        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Falied");
        }
        RatingModel ratingModel = new RatingModel(movieId, null, null);
        return ratingModel;
    }
    public static void insertRating(String movieId, Float rating, Integer numVotes){
        String query = "INSERT INTO ratings (movieId, rating, numVotes) VALUES (?, ?, ?)";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ps.setFloat(2, rating);
            ps.setInt(3, numVotes);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query Succeed");
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Falied");
        }
    }
    public static void updateRating(String movieId, Float rating, Integer numVotes){
        String query = "UPDATE ratings SET rating = ?, numVotes = ? WHERE movieId = ?";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setFloat(1, rating);
            ps.setInt(2, numVotes);
            ps.setString(3, movieId);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query Succeed");
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Falied");
        }
    }
    public static boolean searchMovieByID(String movieId){
        String query = "SELECT COUNT(*) AS num FROM movies WHERE id = ? AND hidden = 0";

        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
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
