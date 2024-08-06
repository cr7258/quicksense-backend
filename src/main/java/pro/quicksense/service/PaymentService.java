package pro.quicksense.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pro.quicksense.annotation.AirwallexRequest;
import pro.quicksense.common.AirwallexConstant;
import pro.quicksense.util.TimezoneUtil;

import java.text.ParseException;
import java.util.Date;

import static pro.quicksense.common.AirwallexConstant.*;

@Service
public class PaymentService {

    /**
     * As a client to Airwallex, all the requests should be sent with a token returned from authentication.
     */
    private static String AIRWALLEX_TOKEN;

    /**
     * The timestamp the token would expire at.
     */
    private static String AIRWALLEX_TOKEN_EXPIRES_AT;

    /**
     * Request for Airwallex authentication, Airwallex would return a time-sensitive token,
     * which should be included in all the other requests to Airwallex.
     */
    public void authByAirwallex() throws UnirestException, ParseException {
        Date currentDate = new Date();
        // TODO Check the correctness of the date.
        if (StringUtils.isNotEmpty(AIRWALLEX_TOKEN_EXPIRES_AT)
                && currentDate.before(TimezoneUtil.convertTimeStringToDateObject(AIRWALLEX_TOKEN_EXPIRES_AT))) {
            return;
        }
        HttpResponse<String> response = Unirest.post(AirwallexConstant.API_AUTHENTICATION_LOGIN)
                .header("Content-Type", "application/json")
                // TODO Replace the 'CLIENT_ID_TEST' and 'API_KEY_TEST' with configurations for production.
                .header("x-client-id", AirwallexConstant.CLIENT_ID_TEST)
                .header("x-api-key", AirwallexConstant.API_KEY_TEST)
                .body("{}")
                .asString();
        JSONObject jsonObject = JSON.parseObject(response.getBody());
        AIRWALLEX_TOKEN = (String) jsonObject.get(AirwallexConstant.TOKEN);
        AIRWALLEX_TOKEN_EXPIRES_AT = TimezoneUtil.convertUTC2ICT((String) jsonObject.get(AirwallexConstant.TOKEN_EXPIRED_AT));
    }


    /**
     * Create a payment link, which is hosted by Airwallex,
     * the customer could be redirected to this link address and finish the payment.
     */
    @AirwallexRequest
    public JSONObject createPaymentLink() throws UnirestException {
        HttpResponse<String> response = Unirest.post(API_PAYMENT_LINK_CREATE)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + AIRWALLEX_TOKEN)
                .body(this.assignParametersForPaymentLink().toString())
                .asString();
        return JSON.parseObject(response.getBody());
    }

    /**
     * Get list of payments from a specified condition,
     * payments 30 days in arrears of today or the to created_at date will be returned.
     */
    @AirwallexRequest
    public JSONArray listPayments() throws UnirestException {
        // TODO Add parameters
        HttpResponse<String> response = Unirest.get(API_LIST_PAYMENTS)
                .header("Authorization", "Bearer " + AIRWALLEX_TOKEN)
                .asString();
        return JSON.parseArray(response.getBody());
    }

    /**
     * Create a payment
     */
    @AirwallexRequest
    public JSONObject createPayment(String requestID) throws UnirestException {
        HttpResponse<String> response = Unirest.post(AirwallexConstant.API_CREATE_PAYMENT)
                .header("Authorization", "Bearer " + AIRWALLEX_TOKEN)
                .header("Content-Type", "application/json")
                .body(this.assignParametersForCreatingPayment(requestID).toString())
                .asString();
        return JSON.parseObject(response.getBody());
    }

    /**
     * query the payment status
     */
    @AirwallexRequest
    public JSONObject checkPaymentStatus(String paymentId) throws UnirestException {
        HttpResponse<String> response = Unirest.get(AirwallexConstant.API_AIRWALLEX_ROOT + "/payments/" + paymentId)
                .header("Authorization", "Bearer " + AIRWALLEX_TOKEN)
                .asString();
        return JSON.parseObject(response.getBody());
    }

    private JSONObject assignParametersForPaymentLink() {
        // TODO Modify the arguments, which should be passed from the client side.
        JSONObject jsonBody = new JSONObject();

        JSONObject collectableShopperInfo = new JSONObject();
        collectableShopperInfo.put("message", true);
        collectableShopperInfo.put("phone_number", false);
        collectableShopperInfo.put("reference", false);
        collectableShopperInfo.put("shipping_address", false);

        JSONObject metadata = new JSONObject();
        metadata.put("customer_id", "12345678");

        jsonBody.put("amount", 0.1);
        jsonBody.put("collectable_shopper_info", collectableShopperInfo);
        jsonBody.put("currency", "CNY");
        jsonBody.put("description", "Quicksense 年费会员");
        jsonBody.put("expires_at", "2025-11-04T16:00:00Z");

        jsonBody.put("metadata", metadata);
        jsonBody.put("reference", "1529");
        jsonBody.put("reusable", false);
        jsonBody.put("title", "Quicksense 年费会员");

        return jsonBody;
    }

    private JSONObject assignParametersForCreatingPayment(String requestID) {
        // TODO Modify the arguments, which should be passed from the client side.
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("beneficiary", this.assignBeneficiary());
        // jsonBody.add("payer", this.conductPayer());
        jsonBody.put("payment_amount", "10000");
        jsonBody.put("payment_currency", "HKD");
        jsonBody.put("payment_method", "LOCAL");
        jsonBody.put("reason", "Travel");
        jsonBody.put("reference", "PMT1936398");
        jsonBody.put("request_id", requestID);
        jsonBody.put("source_currency", "USD");
        return jsonBody;
    }

    private JSONObject assignBeneficiary() {
        // TODO Modify the arguments, which should be passed from the client side.
        JSONObject additionalInfo = new JSONObject();
        additionalInfo.put("business_area", "Travel");

        JSONObject address = new JSONObject();
        address.put("city", "Hong Kong");
        address.put("country_code", "HK");
        address.put("postcode", "999077");
        address.put("state", "Hong Kong");
        address.put("street_address", "38 Chengtu Rd");

        JSONObject bankDetails = new JSONObject();
        bankDetails.put("account_currency", "HKD");
        bankDetails.put("account_name", "John Walker");
        bankDetails.put("account_number", "786005728434");
        bankDetails.put("account_routing_type1", "bank_code");
        bankDetails.put("account_routing_value1", "024");
        bankDetails.put("bank_country_code", "HK");
        bankDetails.put("bank_name", "Hang Seng Bank Limited");

        JSONObject beneficiary = new JSONObject();
        beneficiary.put("additional_info", additionalInfo);
        beneficiary.put("address", address);
        beneficiary.put("bank_details", bankDetails);
        beneficiary.put("date_of_birth", "1976-08-26");
        beneficiary.put("entity_type", "PERSONAL");
        beneficiary.put("first_name", "John");
        beneficiary.put("last_name", "Walker");

        return beneficiary;
    }

    @Deprecated
    private JSONObject conductPayer() {
        // TODO Modify the arguments, which should be passed from the client side.
        JSONObject additionalInfo = new JSONObject();
        additionalInfo.put("business_registration_number", "EU300503");

        JSONObject address = new JSONObject();
        address.put("city", "Melbourne");
        address.put("country_code", "AU");
        address.put("postcode", "3000");
        address.put("state", "VIC");
        address.put("street_address", "15 Williams Street");

        JSONObject payer = new JSONObject();
        payer.put("additional_info", additionalInfo);
        payer.put("address", address);
        payer.put("company_name", "Complete Concrete Pty Ltd");
        payer.put("entity_type", "COMPANY");
        return payer;
    }

    /**
     * Query all the available balances in our account.
     */
    private JsonNode getBalanceInAirwallex() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(AirwallexConstant.API_GET_BALANCES)
                .header("Authorization", "Bearer " + AIRWALLEX_TOKEN)
                .asJson();
        return response.getBody();
    }

    /**
     * Return a guaranteed rate for the currency pair you are looking to transact in.
     */
    @AirwallexRequest
    private void getQuote() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(AirwallexConstant.API_CREATE_QUOTE)
                .header("Authorization", "Bearer " + AIRWALLEX_TOKEN)
                .header("Content-Type", "application/json")
                .body(this.conductRequestBodyForCreatingQuote().toString())
                .asJson();
    }

    private JSONObject conductRequestBodyForCreatingQuote() {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("buy_amount", 10000);
        jsonBody.put("buy_currency", "AUD");
        jsonBody.put("conversion_date", TimezoneUtil.getCurrentDateByYYYYMMDD());
        jsonBody.put("sell_amount", 100);
        jsonBody.put("sell_currency", "USD");
        jsonBody.put("validity", "HR_24");
        return jsonBody;
    }
}
