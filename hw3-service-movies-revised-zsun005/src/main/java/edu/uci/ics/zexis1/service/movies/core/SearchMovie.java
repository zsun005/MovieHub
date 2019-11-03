package edu.uci.ics.zexis1.service.movies.core;

import edu.uci.ics.zexis1.service.movies.MovieService;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.MovieModel;
import edu.uci.ics.zexis1.service.movies.models.SearchMoviesRequestModel;
import edu.uci.ics.zexis1.service.movies.models.SearchMoviesResponseModel;
import edu.uci.ics.zexis1.service.movies.models.VerifyPrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchMovie {

    public static SearchMoviesResponseModel searchMovie(SearchMoviesRequestModel requestModel, String email){
        ServiceLogger.LOGGER.info("Searching for students...");
        VerifyPrivilegeResponseModel verifyPrivilegeResponseModel = VerifyPrivillege.VerifyPrivillege(email, 3);
        SearchMoviesResponseModel responseModel;
        int resultCode = verifyPrivilegeResponseModel.getResultCode();
        ServiceLogger.LOGGER.info("Get back from verify privilege, get Result Code: " + resultCode);
        int privileged = 0;
        if(resultCode == 140)
            privileged = 1;

        ArrayList<MovieModel> movies = new ArrayList<>();

        movies = queryMovies(requestModel, privileged);
        if(movies == null) {
            responseModel = new SearchMoviesResponseModel(-1, null);
        }
        else if(movies.isEmpty())
            responseModel = new SearchMoviesResponseModel(211, null);
        else
            responseModel = new SearchMoviesResponseModel(210, movies);

        ServiceLogger.LOGGER.info("Get responseModel: " + responseModel.getResultCode());
        return responseModel;

    }

    private static ArrayList<MovieModel> queryMovies(SearchMoviesRequestModel requestModel, int privileged){
        String title = requestModel.getTitle();
        String genre = requestModel.getGenre();
        Integer year = requestModel.getYear();
        String director = requestModel.getDirector();
        Boolean includeHidden = requestModel.isHidden();
        int limit = requestModel.getLimit();
        int offset = requestModel.getOffset();
        String orderby = requestModel.getOrderby();
        String direction = requestModel.getDirection();

        ServiceLogger.LOGGER.info("Start Query Movies");

        if(title == null) title = "%%";
        if(year == null) year = 0;
        if(director == null) director = "%%";
        ServiceLogger.LOGGER.info("Line 59");
        ArrayList<MovieModel> movies = new ArrayList<>();
        ServiceLogger.LOGGER.info("Line 61");
        String baseQuery = "SELECT DISTINCT m.id AS id, m.title AS title, m.director AS director, m.year AS year, r.rating as rating, r.numVotes as numVotes, m.hidden AS hidden " +
                "FROM movies m ";
        ServiceLogger.LOGGER.info("Line 64");
        if(genre != null){
            baseQuery += "RIGHT OUTER JOIN (SELECT movieId, name AS genreName FROM genres_in_movies LEFT OUTER JOIN genres ON genreId = id WHERE genres.name LIKE " +
                    "\'" + genre + "\'" +
                    " ) g ON m.id = g.movieId ";
        }
        ServiceLogger.LOGGER.info("Line 70");

        baseQuery += "LEFT OUTER JOIN ratings r ON m.id = r.movieId ";

        baseQuery += "WHERE 1 = 1 " +
                "AND (? = \'%%\' OR title LIKE ?) " +
                "AND (? = 0 OR year = ?) " +
                "AND (? = \'%%\' OR director LIKE ?) ";

        ServiceLogger.LOGGER.info("Line 79");
        ServiceLogger.LOGGER.info("includeHidden: " + includeHidden);
        ServiceLogger.LOGGER.info("privileged = " + privileged);
        ServiceLogger.LOGGER.info("Get result: " + ((privileged == 0) && !includeHidden));
        if((privileged == 0) && !includeHidden) {
            baseQuery += "AND hidden = 0 ";
        }

        ServiceLogger.LOGGER.info("Line 83");
        baseQuery += "ORDER BY ? " + direction + " LIMIT ? OFFSET ?";

//        ServiceLogger.LOGGER.info("BaseQuery: " + baseQuery);


        try{
            PreparedStatement ps = MovieService.getCon().prepareStatement(baseQuery);
            ps.setString(1, title);
            ps.setString(2, title);
            ps.setInt(3, year);
            ps.setInt(4, year);
            ps.setString(5, director);
            ps.setString(6, director);
            ps.setString(7, orderby);
            ps.setInt(8, limit);
            ps.setInt(9, offset);

            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query Succeed");
            while(rs.next()){
                String movieId = rs.getString("id");
                String movieTitle = rs.getString("title");
                String movieDirector = rs.getString("director");
                int movieYear = rs.getInt("year");
                Float rating = rs.getFloat("rating");
                int numVotes = rs.getInt("numVotes");

                MovieModel movie = new MovieModel(movieId, movieTitle, movieDirector,movieYear, rating, numVotes, includeHidden);
                if(privileged == 0)
                    movie.setHidden(null);
                movies.add(movie);

            }

            return movies;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed. RETURN NULL........");
        }
        return null;

    }
}
