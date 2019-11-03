package edu.uci.ics.zexis1.service.movies.core;

import edu.uci.ics.zexis1.service.movies.MovieService;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.SearchStarRequestModel;
import edu.uci.ics.zexis1.service.movies.models.StarModel;
import edu.uci.ics.zexis1.service.movies.models.StarResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchStarCore {
    public static StarResponseModel SearchStars(SearchStarRequestModel requestModel){
        StarResponseModel responseModel;
        String movieTitle = requestModel.getMovieTitle();
        int birthYear = requestModel.getBirthYear();
        String name = requestModel.getName();
        int offset = requestModel.getOffset();
        int limit = requestModel.getLimit();
        String orderby = requestModel.getOrderby();
        String direction = requestModel.getDirection();

        // TODO: Sortby: "name", Orderby: "desc"
        ArrayList<String> starIDs = new ArrayList<>();
        if(movieTitle != null)
            starIDs = findStarsFromMovie(movieTitle);

        String query =
                "SELECT stars.id AS id, stars.name AS name, stars.birthYear AS birthYear " +
                        "FROM stars " +
                        "WHERE 1 = 1 " +
                        "AND (? = name OR name LIKE ?) " +
                        "AND (? = 0 OR birthYear = ?) ";
        if(starIDs != null && starIDs.size() > 0)
        {
            query += "AND id IN (";
            for(int i = 0; i < starIDs.size(); i++)
                query += "?, ";
            query = query.substring(0, query.length() - 2);
            query += ") ";
        }

        query += "ORDER BY " + orderby + " " + direction + " " + "LIMIT " + String.valueOf(limit) + " OFFSET " + String.valueOf(offset);

        try
        {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, name);
            ps.setInt(3, birthYear);
            ps.setInt(4, birthYear);
            for(int i = 5; i < starIDs.size() + 5; i++)
                ps.setString(i, starIDs.get(i - 5));

            ServiceLogger.LOGGER.info("Trying Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeed!! Try reading data.");

            int size = 0;
            if(rs != null)
            {
                rs.last();
                size = rs.getRow();
                rs.beforeFirst();
            }

            int index = 0;

            ArrayList<StarModel> stars = new ArrayList<>();
            while(rs.next())
            {
                if(size == 0)
                {
                    ServiceLogger.LOGGER.warning("Size is 0, break the loop");
                    break;
                }
                String starId = rs.getString("id");
                String starName = rs.getString("name");
                Integer bYear = rs.getInt("birthYear");
                bYear = (bYear == 0) ? null : bYear;


                StarModel star = new StarModel(starId, starName, bYear);
                stars.add(star);

            }

            if(stars.size() == 0)
                responseModel = new StarResponseModel(213, stars);
            else
                responseModel = new StarResponseModel(212, stars);

            return responseModel;


        }
        catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query fails");
        }


        return null;
    }

    public static ArrayList<String> findStarsFromMovie(String movieTitle)
    {
        ArrayList<String> starIDs = new ArrayList<>();
        String query
                = "SELECT stars_in_movies.starId AS starId FROM movies LEFT JOIN stars_in_movies ON movies.id = stars_in_movies.movieId " +
                "WHERE movies.title LIKE ?";
        try
        {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieTitle);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query Succeed");



            int index = 0;
            while (rs.next())
            {
                starIDs.add(rs.getString("starId"));
            }

            return starIDs;


        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Failing Query");
        }
        return starIDs;
    }
}
