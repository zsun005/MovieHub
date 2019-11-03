package edu.uci.ics.zexis1.service.billing.core;

import edu.uci.ics.zexis1.service.billing.BillingService;
import edu.uci.ics.zexis1.service.billing.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.billing.models.CustomerRequestModel;
import edu.uci.ics.zexis1.service.billing.models.CustomerResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerCore {
    public static CustomerResponseModel customerInsert(CustomerRequestModel requestModel){
        String email = requestModel.getEmail();
        CustomerResponseModel responseModel = null;
        if(checkCustomerExist(email)){
            responseModel = new CustomerResponseModel(333);
            return responseModel;
        }
        String firstName = requestModel.getFirstName();
        String lastName = requestModel.getLastName();
        String ccId = requestModel.getCcId();
        String address = requestModel.getAddress();
        if(!Validation.validCreditCardlength(ccId))
        {
            responseModel = new CustomerResponseModel(321);
            return responseModel;
        }
        if(!Validation.validCreditCardValue(ccId))
        {
            responseModel = new CustomerResponseModel(322);
            return responseModel;
        }
        if(!CreditCard.checkCreditIDExist(ccId)){
            responseModel = new CustomerResponseModel(331);
            return responseModel;
        }
        String query = "INSERT INTO customers (email, firstName, lastName, ccId, address) VALUES(?, ?, ?, ?, ?)";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, ccId);
            ps.setString(5, address);
            ServiceLogger.LOGGER.info("Trying Query " + ps.toString());
            ps.execute();
            responseModel = new CustomerResponseModel(3300);
            return responseModel;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("QUERY FAILED");
        }

//        System.exit(1);
        return null;

    }
    public static CustomerResponseModel customerUpdate(CustomerRequestModel requestModel){
        String email = requestModel.getEmail();
        CustomerResponseModel responseModel = null;
        if(!checkCustomerExist(email)){
            responseModel = new CustomerResponseModel(332);
            return responseModel;
        }
        String firstName = requestModel.getFirstName();
        String lastName = requestModel.getLastName();
        String ccId = requestModel.getCcId();
        if(!Validation.validCreditCardlength(ccId))
        {
            responseModel = new CustomerResponseModel(321);
            return responseModel;
        }
        if(!Validation.validCreditCardValue(ccId)){
            responseModel = new CustomerResponseModel(322);
            return responseModel;
        }
        String address = requestModel.getAddress();
        if(!CreditCard.checkCreditIDExist(ccId)){
            responseModel = new CustomerResponseModel(331);
            return responseModel;
        }

        String query = "UPDATE customers SET firstName = ?, lastName = ?, ccId = ?, address = ? WHERE email = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, ccId);
            ps.setString(4, address);
            ps.setString(5, email);
            ServiceLogger.LOGGER.info("Try query: " + ps.toString());
            ps.execute();
            responseModel = new CustomerResponseModel(3310);
            return responseModel;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
//            System.exit(1);
            return null;
        }
    }
    public static CustomerResponseModel retrieveCustomer(String email){
        CustomerResponseModel responseModel = new CustomerResponseModel(-1);
        if(!checkCustomerExist(email)){
            responseModel = new CustomerResponseModel(332);
            return responseModel;
        }
        String query = "SELECT * FROM customers WHERE email = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String ccId = rs.getString("ccId");
                String address = rs.getString("address");
                CustomerRequestModel customer = new CustomerRequestModel(email, firstName, lastName, ccId, address);
                responseModel = new CustomerResponseModel(3320, customer);

            }
            return responseModel;

        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
//            System.exit(1);
            return null;
        }

    }

    public static boolean checkCustomerExist(String email){
        String query = "SELECT COUNT(*) AS num FROM customers WHERE email = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getInt("num") == 0)
                    return false;
                return true;
            }
            ServiceLogger.LOGGER.info("Didn't get result");

            return true;
        } catch (SQLException e){
            e.printStackTrace();
        }
//        System.exit(1);
        return true;

    }
}
