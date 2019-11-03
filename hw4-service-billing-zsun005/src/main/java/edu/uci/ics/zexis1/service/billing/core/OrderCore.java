package edu.uci.ics.zexis1.service.billing.core;


import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import edu.uci.ics.zexis1.service.billing.BillingService;
import edu.uci.ics.zexis1.service.billing.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.billing.models.*;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderCore {
    final static String ClinetID = "AXgrKv_055vX9sCsMNxIGKCj7SdxzAUuAsVGA880AyF8E_tw_CBfwErlW2Rdo58n5xoqACnKua_is2sX";
    final static String Secret = "EGHuYLQcldbj2mZtsgu7UQ1hZtH4eRnyLqjQmmYxe5VYZ_0hm3bT4xCoSmml6432lUvIDJ_8FglEAKMa";
    public static OrderResponseModel placeOrder(String email) {
        OrderResponseModel responseModel;
        if (!CustomerCore.checkCustomerExist(email)) {
            responseModel = new OrderResponseModel(332);
            return responseModel;
        }
        Date currentDate = new Date(System.currentTimeMillis());

        String query = "SELECT * FROM carts WHERE email = ?";
        try {
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Try Query" + ps.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList<OrderItemsModel> items = new ArrayList<>();
            while (rs.next()) {
                String movieId = rs.getString("movieId");
                int quantity = rs.getInt("quantity");
                if (movieId == null)
                    break;
                OrderItemsModel item = new OrderItemsModel(email, movieId, quantity, currentDate);
                items.add(item);
            }
            if (items.size() == 0) {
                responseModel = new OrderResponseModel(341);
                return responseModel;
            }
            float total = calculateTotal(items);
            String redirectUrl = getRedirectURL(total);
            if(redirectUrl == null){
                responseModel = new OrderResponseModel(342);
                return responseModel;
            }

            String token = redirectUrl.substring(redirectUrl.indexOf("token") + 6, redirectUrl.length());

            for (OrderItemsModel item : items) {
                insertIntoSales_Transcations(item, token);
            }

            responseModel = new OrderResponseModel(3400, redirectUrl, token);
            return responseModel;

        } catch (SQLException e) {
            ServiceLogger.LOGGER.info("Query Failed");
            return null;
        }
    }
    public static OrderResponseModel completePayment(OrderCompleteRequestModel requestModel){
        OrderResponseModel responseModel;
        String token = requestModel.getToken();
        if(!findTransaction(token)){
            responseModel = new OrderResponseModel(3421);
            return responseModel;
        }
        String PayerID = requestModel.getPayerID();
        String paymentID = requestModel.getPaymentId();

        Payment payment = new Payment();
        payment.setId(paymentID);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(PayerID);
        try{
            APIContext context = new APIContext(ClinetID, Secret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            if(createdPayment != null){
                String newTransactionId = createdPayment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
                updateTransactionId(token, newTransactionId);
                responseModel = new OrderResponseModel(3420);
                return responseModel;
            }
            else{
                responseModel = new OrderResponseModel(3422);
                return responseModel;
            }
        } catch (PayPalRESTException e){
            ServiceLogger.LOGGER.warning("Payment Failed");
            responseModel = new OrderResponseModel(-1);
            return responseModel;
        }



    }
    public static void updateTransactionId(String token, String transactionId){
        String query = "UPDATE transactions SET transactionId = ? WHERE token = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, transactionId);
            ps.setString(2, token);
            ServiceLogger.LOGGER.info("Try Query " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Succeed");
        } catch (SQLException e){
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query Failed");
        }
    }
    public static boolean findTransaction(String token){
        String query = "SELECT COUNT(*) AS num FROM transactions WHERE token = ?";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, token);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int num = rs.getInt("num");
                if(num == 0)
                    return false;
                else
                    return true;
            }
            return false;
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
            System.exit(1);
            return false;
        }
    }
    public static String getRedirectURL(float t){
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        String total = decimalFormat.format(t);

        Amount amount = new Amount();
        amount.setTotal(total);
        amount.setCurrency("USD");
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);


        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("https://example.com/cancel");


        redirectUrls.setReturnUrl("http://0.0.0.0:6543/api/billing/order/complete");
//        redirectUrls.setReturnUrl(uri.toString());
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;

        try{
            String redirectUrl = "";
            APIContext context = new APIContext(ClinetID, Secret, "sandbox");
            createdPayment = payment.create(context);
            if(createdPayment != null){
                List<Links> links = createdPayment.getLinks();
                for(Links link : links){
                    if(link.getRel().equals("approval_url")){
                        redirectUrl = link.getHref();
                        break;
                    }
                }
            }
            return redirectUrl;
        } catch (PayPalRESTException e){
            System.out.println("Error happened during payment creation!");
        }
        return null;
    }
    public static float calculateTotal(ArrayList<OrderItemsModel> items){
        float total = 0;
        for(OrderItemsModel item : items){
            String movieId = item.getMovieId();
            int quantity = item.getQuantity();

            String query = "SELECT * FROM movie_prices WHERE movieId = ?";
            try{
                PreparedStatement ps = BillingService.getCon().prepareStatement(query);
                ps.setString(1, movieId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    float unit_price = rs.getFloat("unit_price");
                    float discount = rs.getFloat("discount");

                    total += (unit_price * discount * quantity);
                }

            } catch (SQLException e){
                e.printStackTrace();
                break;
            }
        }

        ServiceLogger.LOGGER.info("Get Total Charge: " + total);
        return total;



    }

    public static void insertIntoSales_Transcations(OrderItemsModel item, String token){
        String email = item.getEmail();
        String movieId = item.getMovieId();
        int quantity = item.getQuantity();
        Date saleDate = item.getSaleDate();

        String query = "{ call insert_sales_transactions(?, ?, ?, ?, ?)}";
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, movieId);
            ps.setInt(3, quantity);
            ps.setDate(4, saleDate);
            ps.setString(5, token);
            ServiceLogger.LOGGER.info("Try Query" + ps.toString());
            ps.execute();
            deleteShoppingCartAfterOrder(email, movieId);
            ServiceLogger.LOGGER.info("Query Succeed");
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
        }
    }
    public static void deleteShoppingCartAfterOrder(String email, String movieId){
        ShoppingCart.deleteShoppingCart(email, movieId);
    }

    public static ArrayList<String> getTransactionIDs(String email){
        String query = "SELECT DISTINCT transactionId FROM sales LEFT JOIN transactions ON id = sId WHERE email = ?";
        ArrayList<String> transactionIds = new ArrayList<>();
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String transactionId = rs.getString("transactionId");
                transactionIds.add(transactionId);
            }
            return transactionIds;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return transactionIds;
    }

    public static OrderRetrieve retrieveOrder(String email){
        OrderRetrieve responseModel = new OrderRetrieve(-1);
        if(!CustomerCore.checkCustomerExist(email)){
            responseModel = new OrderRetrieve(332);
            return responseModel;
        }
        ArrayList<String> transactionIds = getTransactionIDs(email);
        ArrayList<TransactionModel> transactions = new ArrayList<>();

        for(String transactionId: transactionIds){
            if(transactionId == null)
                continue;
            String query = "SELECT * FROM sales LEFT JOIN transactions ON sales.id = transactions.sId " +
                    "LEFT JOIN movie_prices ON sales.movieId = movie_prices.movieId " +
                    "WHERE email = ? AND transactionId = ?";
            try {
                PreparedStatement ps = BillingService.getCon().prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, transactionId);
                ServiceLogger.LOGGER.info("Try Query" + ps.toString());

                // TODO: Use TransactionId find information
                APIContext apiContext = new APIContext(ClinetID, Secret, "sandbox");
                Sale sale = Sale.get(apiContext, transactionId);
                Amount amount = sale.getAmount();
                Currency currency = sale.getTransactionFee();

                String state = sale.getState();

                String transaction_fee_value = currency.getValue();
                String transaction_fee_currancy = currency.getCurrency();
                TransactionFeeModel transaction_fee = new TransactionFeeModel(transaction_fee_value, transaction_fee_currancy);


                String amountTotal = amount.getTotal();
                String amountCurrency = amount.getCurrency();

                AmountModel amountModel = new AmountModel(amountTotal, amountCurrency);

                String create_time = sale.getCreateTime();
                String update_time = sale.getUpdateTime();


                ResultSet rs = ps.executeQuery();
                ArrayList<OrderItemsModel> items = new ArrayList<>();

                while(rs.next()){
                    String movieId = rs.getString("movieId");
                    int quantity = rs.getInt("quantity");
                    Date saleDate = rs.getDate("saleDate");
                    float unit_price = rs.getFloat("unit_price");
                    float discount = rs.getFloat("discount");
                    OrderItemsModel item = new OrderItemsModel(email, movieId, quantity, unit_price, discount,saleDate);
                    items.add(item);
                }
                TransactionModel transaction = new TransactionModel(transactionId, state, amountModel, transaction_fee, create_time, update_time, items);
                transactions.add(transaction);
            } catch (SQLException e){
                e.printStackTrace();
            } catch (PayPalRESTException e){

            }

        }
            responseModel = new OrderRetrieve(3410, transactions);
            return responseModel;




    }
}
