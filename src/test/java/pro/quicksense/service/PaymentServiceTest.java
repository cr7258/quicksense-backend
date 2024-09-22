package pro.quicksense.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.quicksense.entity.payment.Payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @Test
    public void testCreatePaymentLink() throws Throwable {
        Payment payment = new Payment("001", "CNY");
        JSONObject paymentLink = paymentService.createPaymentLink(payment);
        assertTrue(paymentLink.containsKey("url"));
    }

    @Test
    public void testListPaymentLinks() throws UnirestException {
        JSONObject paymentLinks = paymentService.listPaymentLinks(null, null, null, null);
        JSONArray items = (JSONArray) paymentLinks.get("items");
        assertTrue(items.size() > 0);
    }

    @Test
    public void testRetrievePaymentLink() throws UnirestException {
        JSONObject paymentLinks = paymentService.listPaymentLinks(null, null, null, null);
        JSONArray items = (JSONArray) paymentLinks.get("items");
        JSONObject item0 = (JSONObject) items.get(0);
        String idOfItem0 = (String) item0.get("id");
        JSONObject paymentLinkRetrieved = paymentService.retrievePaymentLink(idOfItem0);
        assertEquals(idOfItem0, paymentLinkRetrieved.get("id"));
    }
}
