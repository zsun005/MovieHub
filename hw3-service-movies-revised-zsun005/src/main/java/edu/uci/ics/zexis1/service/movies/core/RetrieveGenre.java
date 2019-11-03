package edu.uci.ics.zexis1.service.movies.core;

import edu.uci.ics.zexis1.service.movies.MovieService;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.GenreModel;
import edu.uci.ics.zexis1.service.movies.models.GenreRetrieveResponseModel;
import edu.uci.ics.zexis1.service.movies.models.SearchMoviesResponseModel;
import edu.uci.ics.zexis1.service.movies.models.VerifyPrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RetrieveGenre {
    public static GenreRetrieveResponseModel retrieveGenre(String email){
//        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 3);
        GenreRetrieveResponseModel responseModel;
//        int resultCode = verifyPrivilegeResponseModel.getResultCode();
//        if(resultCode != 140){
//            responseModel = new GenreRetrieveResponseModel(141, null);
//            return responseModel;
//        }
        String query = "SELECT * FROM genres";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Try Query " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList<GenreModel> genres = new ArrayList<>();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                GenreModel genre = new GenreModel(id, name);
                genres.add(genre);
            }

            responseModel = new GenreRetrieveResponseModel(219, genres);
            return responseModel;
        } catch (SQLException e){
            responseModel = new GenreRetrieveResponseModel(-1, null);
            return responseModel;
        }
    }

    public static GenreRetrieveResponseModel retrieveGenreByMovieId(String movieId, String email){
        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 3);
        GenreRetrieveResponseModel responseModel;
        int resultCode = verifyPrivilegeResponseModel.getResultCode();
        if(resultCode != 140){
            responseModel = new GenreRetrieveResponseModel(141, null);
            return responseModel;
        }
        String query = "SELECT id, name FROM genres LEFT OUTER JOIN genres_in_movies ON genres.id = genres_in_movies.genreId WHERE movieId = ?";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ServiceLogger.LOGGER.info("Try Query " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList<GenreModel> genres = new ArrayList<>();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                GenreModel genre = new GenreModel(id, name);
                genres.add(genre);
            }

            responseModel = new GenreRetrieveResponseModel(219, genres);
            return responseModel;
        } catch (SQLException e){
            responseModel = new GenreRetrieveResponseModel(-1, null);
            return responseModel;
        }

    }
}
