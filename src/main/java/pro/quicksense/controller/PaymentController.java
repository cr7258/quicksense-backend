package pro.quicksense.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.quicksense.common.ApiResponseBuilder;
import pro.quicksense.service.PaymentService;


@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/link/create")
    public ResponseEntity<Object> createPaymentLink() throws UnirestException {
        return ApiResponseBuilder.success(
                HttpStatus.OK,
                "Create payment link successfully",
                paymentService.createPaymentLink());
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listPayments() throws UnirestException {
        return ApiResponseBuilder.success(
                HttpStatus.OK,
                "List payments successfully",
                paymentService.listPayments());
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPayment(@RequestParam("requestID") String requestID) throws UnirestException {
        return ApiResponseBuilder.success(
                HttpStatus.OK,
                "Create payment successfully",
                paymentService.createPayment(requestID));
    }

    @GetMapping("/check")
    public ResponseEntity<Object> checkPaymentStatus() throws UnirestException {
        return ApiResponseBuilder.success(
                HttpStatus.OK,
                "Check payment status successfully",
                paymentService.checkPaymentStatus("test_payment_id"));
    }
}
