package edu.uci.ics.zexis1.service.billing.core;

import edu.uci.ics.zexis1.service.billing.BillingService;
import edu.uci.ics.zexis1.service.billing.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.billing.models.CreditCardRequestModel;
import edu.uci.ics.zexis1.service.billing.models.CreditCardResponseModel;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCard {
    public static CreditCardResponseModel insertCreditCard(CreditCardRequestModel requestModel, Date date){
        String id = requestModel.getId();
        CreditCardResponseModel responseModel;
        if(checkCreditIDExist(id)){
            responseModel = new CreditCardResponseModel(325);
            return responseModel;
        }
        String firstName = requestModel.getFirstName();
        String lastName = requestModel.getLastName();
        Date expiration = date;

        String query = "INSERT INTO creditcards (id, firstName, lastName, expiration) VALUES(?, ?, ?, ?)";

        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setDate(4,expiration);

            ServiceLogger.LOGGER.info("Try:" + ps.toString());
            ps.execute();
            responseModel = new CreditCardResponseModel(3200);
            return responseModel;
        } catch (SQLException e){
            e.printStackTrace();
        }
//        System.exit(1);
        return null;

    }

    public static CreditCardResponseModel updateCreditCard(CreditCardRequestModel requestModel, Date date) {
        String id = requestModel.getId();
        CreditCardResponseModel responseModel;

        if (!checkCreditIDExist(id)) {
            responseModel = new CreditCardResponseModel(324);
            return responseModel;
        }

        String firstName = requestModel.getFirstName();
        String lastName = requestModel.getLastName();

        String query = "UPDATE creditcards SET firstName = ?, lastName = ?, expiration = ? WHERE id = ?";
        try {
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setDate(3, date);
            ps.setString(4, id);

            ServiceLogger.LOGGER.info("Trying Query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query Succeed");
            responseModel = new CreditCardResponseModel(3210);
            return responseModel;
        } catch (SQLException e) {
            e.printStackTrace();
            responseModel = new CreditCardResponseModel(-1);
            return responseModel;
        }
    }
    public static CreditCardResponseModel deleteCreditCard(String id) {
        CreditCardResponseModel responseModel;
        if(!checkCreditIDExist(id)){
            responseModel = new CreditCardResponseModel(324);
            return responseModel;
        }
        String query = "DELETE FROM creditcards WHERE id = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Trying query" + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query succeed");
            responseModel = new CreditCardResponseModel(3220);
            return responseModel;
        } catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("QUERY FAILED");
            return null;
        }
    }
    public static CreditCardResponseModel retrieveCreditcard(String id){
        CreditCardResponseModel responseModel = null;
        if(!checkCreditIDExist(id)){
            responseModel = new CreditCardResponseModel(324);
            return responseModel;
        }
        String query = "SELECT * FROM creditcards WHERE id = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Trying query" + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                String cid = rs.getString("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Date date = rs.getDate("expiration");

                CreditCardRequestModel creditcard = new CreditCardRequestModel(cid, firstName, lastName, date.toString());
                responseModel = new CreditCardResponseModel(3230, creditcard);
                return responseModel;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
//            System.exit(1);
            return responseModel;
        }


    }
    public static boolean checkCreditIDExist(String id){
        String query = "SELECT COUNT(*) AS num FROM creditcards WHERE id = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ServiceLogger.LOGGER.info("Trying query" + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                if(rs.getInt("num") == 0)
                    return false;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
        }
//        System.exit(1);
        return true;
    }

}
