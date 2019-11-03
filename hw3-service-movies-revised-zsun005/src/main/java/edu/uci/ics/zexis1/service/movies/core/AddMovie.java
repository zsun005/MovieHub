package edu.uci.ics.zexis1.service.movies.core;

import edu.uci.ics.zexis1.service.movies.MovieService;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class AddMovie {
    private final static String idPrefix = "cs";
    public static int id = 1;

    public static AddMovieResponseModel addMovie(AddMovieRequestModel requestModel, String email){
        ServiceLogger.LOGGER.info("Searching for students...");
        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 3);
        AddMovieResponseModel responseModel;
        int resultCode = verifyPrivilegeResponseModel.getResultCode();
        if(resultCode != 140){
            responseModel = new AddMovieResponseModel(141, null, null);
            return responseModel;
        }
        String title = requestModel.getTitle();
        String director = requestModel.getDirector();
        int year = requestModel.getYear();
        String backdrop_path = requestModel.getBackdrop_path();
        Integer budget = requestModel.getBudget();
        String overview = requestModel.getOverview();
        String poster_path = requestModel.getPoster_path();
        Integer revenue = requestModel.getRevenue();
        ArrayList<GenreModel> genres = requestModel.getGenres();
        if(movieExist(title)){
            responseModel = new AddMovieResponseModel(216, null, null);
            return responseModel;
        }
        String movieId;
        int movieIdMiddleZeros = 7;
        String moviepostfix = String.valueOf(id++);
        movieId = idPrefix;
        int zeros = 7 - moviepostfix.length();
        for(int i = 0; i < zeros; i++)
            movieId += "0";

        movieId += moviepostfix;
        if(!addMovie(movieId, title, director, year, backdrop_path, budget, overview, poster_path, revenue)){
            responseModel = new AddMovieResponseModel(215, null, null);
            return responseModel;
        }
        ArrayList<Integer> genreids = new ArrayList<>();
        for(int i = 0; i < genres.size(); i++){
            int genreid = genres.get(i).getId();
            String genreName = genres.get(i).getName();
            if(!genreExists(genreName)){
                genreid = getNewGenreId(genreName);
                updateGenre(genreid, genreName);
            }
            updateGenre_in_Movies(genreid, movieId);
            genreids.add(genreid);
        }

        responseModel = new AddMovieResponseModel(214, movieId, genreids);
        return responseModel;

    }
    public static int getNewGenreId(String name){
        String query = "SELECT MAX(id) as id FROM genres";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying Query " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int id = 0;
            if(rs.next()){
                id = rs.getInt("id");
            }
            id++;
            return id;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Falied");
            return -1;
        }
    }

    public static void updateGenre_in_Movies(int genreId, String movieId){
        String query = "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?)";
        try
        {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setInt(1, genreId);
            ps.setString(2, movieId);

            ServiceLogger.LOGGER.info("Trying Query " + ps.toString());

            ps.execute();
            ServiceLogger.LOGGER.info("Succeed");

        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Falied");
        }
    }

    public static boolean updateGenre(int id, String name){
        String query = "INSERT INTO genres (id, name) VALUES (?, ?)";
        try
        {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, name);

            ServiceLogger.LOGGER.info("Update Genre Query: " + ps.toString());

            ps.execute();

            ServiceLogger.LOGGER.info("Succeed");

            return true;

        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.info("Update Genre Fail");
            return false;
        }
    }

    public static boolean movieExist(String title)
    {
        String query = "SELECT COUNT(*) AS num FROM movies WHERE title = ? AND hidden = 0";

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
    public static void insert_into_rating(String movieId){
        String query = "INSERT INTO ratings (movieId, rating, numVotes) " +
                "VALUES (?, ?, ?)";
        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ps.setFloat(2, 0);
            ps.setInt(3, 0);
            ServiceLogger.LOGGER.info("Query: " + ps.toString());
            ps.execute();
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
        }
    }
    public static boolean addMovie(String movieId, String title, String director, int year, String backdrop_path, Integer budget, String overview, String poster_path, Integer revenue)
    {
        String query = "INSERT INTO movies (id, title, year, director, backdrop_path, budget, overview, poster_path, revenue, hidden) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try
        {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ps.setString(2, title);
            ps.setInt(3, year);
            ps.setString(4, director);
            if(backdrop_path == null)
                ps.setString(5, null);
            else ps.setString(5, backdrop_path);

            if(budget == null)
                ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, budget);

            ps.setString(7, overview);
            ps.setString(8, poster_path);
            if(revenue == null)
                ps.setNull(9, Types.INTEGER);
            else ps.setInt(9, revenue);

            ServiceLogger.LOGGER.info("Trying Inser movie Query: " + ps.toString());

            ps.execute();

            ServiceLogger.LOGGER.info("Insert query Succeed");

            insert_into_rating(movieId);

            return true;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Insert Movie Failed");
        }
        return false;
    }
    public static boolean genreExists(String name){
        String query = "SELECT count(*) AS NUM FROM genres WHERE name = ?";
        try
        {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, name);

            ServiceLogger.LOGGER.info("Find Genre Query: " + ps.toString());

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                if(rs.getInt("NUM") != 0)
                    return true;
            }

            ServiceLogger.LOGGER.info("Succeed");

            return false;

        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.info("Update Genre Fail");
            return true;
        }
    }
}
