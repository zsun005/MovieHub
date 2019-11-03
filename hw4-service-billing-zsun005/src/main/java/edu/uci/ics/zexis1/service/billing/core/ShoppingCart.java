package edu.uci.ics.zexis1.service.billing.core;

import edu.uci.ics.zexis1.service.billing.BillingService;
import edu.uci.ics.zexis1.service.billing.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.billing.models.ShoppingCartRequestModel;
import edu.uci.ics.zexis1.service.billing.models.ShoppingCartResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShoppingCart {

    // Insert email, movieId, quantity into shopping carts table
    public static ShoppingCartResponseModel insertIntoCarts(String email, String movieId, Integer quantity){
        Boolean isDuplicate = findDuplicateRecordInCarts(email, movieId);
        ShoppingCartResponseModel responseModel = null;
        if(isDuplicate)
            return new ShoppingCartResponseModel(311);
        String query = "INSERT INTO carts (email, movieId, quantity) VALUES (?, ?, ?)";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, movieId);
            ps.setInt(3, quantity);


            ServiceLogger.LOGGER.info("Trying Query: " + ps.toString());

            ps.execute();

            ServiceLogger.LOGGER.info("Succeed Insert New Items");

            return new ShoppingCartResponseModel(3100);

        } catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
//            System.exit(0);
            return null;

        }
    }
    public static ShoppingCartResponseModel updateShoppingCart(String email, String movieId, Integer quantity)
    {
        ShoppingCartResponseModel responseModel = null;
        if(!findDuplicateRecordInCarts(email, movieId))
        {
            responseModel = new ShoppingCartResponseModel(312);
            return responseModel;
        }
        String query = "UPDATE carts SET quantity = ? WHERE email = ? AND movieId = ?";
        try
        {
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setInt(1, quantity);
            ps.setString(2, email);
            ps.setString(3, movieId);

            ServiceLogger.LOGGER.info("Query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query Succeed");
            responseModel = new ShoppingCartResponseModel(3110);
            return responseModel;
        } catch (SQLException e)
        {
            e.printStackTrace();
            responseModel = new ShoppingCartResponseModel(-1);
            return responseModel;
        }
    }
    public static ShoppingCartResponseModel deleteShoppingCart(String email, String movieId){
        ShoppingCartResponseModel responseModel;
        if(!findDuplicateRecordInCarts(email, movieId))
        {
            responseModel = new ShoppingCartResponseModel(312);
            return responseModel;
        }

        String query = "DELETE FROM carts WHERE email = ? AND movieId = ?";
        try
        {
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, movieId);

            ServiceLogger.LOGGER.info("Query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query Succeed");
            responseModel = new ShoppingCartResponseModel(3120);
            return responseModel;
        } catch (SQLException e)
        {
            e.printStackTrace();
            responseModel = new ShoppingCartResponseModel(-1);
            return responseModel;
        }
    }

    public static ShoppingCartResponseModel retrieveCart(String email)
    {
        ShoppingCartResponseModel responseModel = null;
        ArrayList<ShoppingCartRequestModel> items = new ArrayList<>();

        String query = "SELECT * FROM carts WHERE email = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                String record_movieId = rs.getString("movieId");
                String record_email = rs.getString("email");
                int record_quantity = rs.getInt("quantity");
                ShoppingCartRequestModel item = new ShoppingCartRequestModel(record_email, record_movieId, record_quantity);
                items.add(item);
            }
            if(items.size() == 0)
                responseModel = new ShoppingCartResponseModel(312);
            else
                responseModel = new ShoppingCartResponseModel(3130, items);

            return responseModel;
        }catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed return Internal Server Error");
            responseModel = new ShoppingCartResponseModel(-1);
            return responseModel;
        }
    }

    public static ShoppingCartResponseModel clearShoppingCart(String email){
        String query = "DELETE FROM carts WHERE email = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying Query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeed");
            ShoppingCartResponseModel responseModel = new ShoppingCartResponseModel(3140);
            return responseModel;
        } catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
            ShoppingCartResponseModel responseModel = new ShoppingCartResponseModel(-1);
            return responseModel;
        }
    }

    public static Boolean findDuplicateRecordInCarts(String email, String movieId){
        String query = "SELECT COUNT(*) AS num FROM carts WHERE email = ? AND movieId = ? ";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, movieId);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                if (rs.getInt("num") == 0)
                    return false;
            }
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
//            System.exit(0); // TODO: Does this work? Want to terminate the program immediately. TO DELETE LATER AFTER TESTING.
            return null;
        }
    }
}
