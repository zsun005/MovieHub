package edu.uci.ics.zexis1.service.movies.core;


import edu.uci.ics.zexis1.service.movies.MovieService;

import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.MovieModel;
import edu.uci.ics.zexis1.service.movies.models.SearchMovieByIdResponseModel;
import edu.uci.ics.zexis1.service.movies.models.VerifyPrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchMovieID {
    public static SearchMovieByIdResponseModel SearchMovieById(String movieId, String email){
        SearchMovieByIdResponseModel responseModel;
        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 4);
        int resultCode = verifyPrivilegeResponseModel.getResultCode();
        int privileged = 0;
        if(resultCode == 140)
            privileged = 1;

        String query = "{CALL search_movie(?)}";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query Succeed");
            if(rs.next()){
                String id = rs.getString("id");
                if(id == null){
                    responseModel = new SearchMovieByIdResponseModel(211, null);
                    return responseModel;
                }
                String title = rs.getString("title");
                String director = rs.getString("director");
                Integer year = rs.getInt("year");
                String backdrop_path = rs.getString("backdrop_path");
                Integer budget = rs.getInt("budget");
                String overview = rs.getString("overview");
                String poster_path = rs.getString("poster_path");
                Integer revenue = rs.getInt("revenue");
                Float rating = rs.getFloat("rating");
                Integer numVotes = rs.getInt("numVotes");
                int hidden = rs.getInt("hidden");
                ServiceLogger.LOGGER.info("Line 48");
                if(hidden == 1 && privileged == 0){
                    responseModel = new SearchMovieByIdResponseModel(141, null);
                    return responseModel;
                }
                ServiceLogger.LOGGER.info("Line 53");
                String genres = rs.getString("genres");
                String stars = rs.getString("stars");

                MovieModel movie = new MovieModel(id, title, director, year, backdrop_path, budget, overview, poster_path, revenue, rating, numVotes, genres, stars);
                responseModel = new SearchMovieByIdResponseModel(210, movie);
                return responseModel;
            }
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
        }

        responseModel = new SearchMovieByIdResponseModel(-1, null);
        return responseModel;
    }
}
