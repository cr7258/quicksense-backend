package pro.quicksense.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.quicksense.common.ApiResponseBuilder;
import pro.quicksense.entity.payment.Payment;
import pro.quicksense.service.PaymentService;


@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/link/create")
    public ResponseEntity<Object> createPaymentLink(@RequestBody Payment payment) throws UnirestException {
        return ApiResponseBuilder.success(
                HttpStatus.OK,
                "Create payment link successfully",
                paymentService.createPaymentLink(payment));
    }

    @GetMapping("/link/retrieve")
    public ResponseEntity<Object> retrievePaymentLink(@RequestParam(required = false) String paymentLinkId) throws UnirestException {
        return ApiResponseBuilder.success(
                HttpStatus.OK,
                "Retrieve payment link successfully",
                paymentService.retrievePaymentLink(paymentLinkId));
    }

    @GetMapping("/link/list")
    public ResponseEntity<Object> listPaymentLinks(@RequestParam(required = false) String fromCreatedAt,
                                                   @RequestParam(required = false) String toCreatedAt,
                                                   @RequestParam(required = false) Boolean status,
                                                   @RequestParam(required = false) Boolean isActive) throws UnirestException {
        return ApiResponseBuilder.success(
                HttpStatus.OK,
                "List payment links successfully",
                paymentService.listPaymentLinks(fromCreatedAt, toCreatedAt, status, isActive));
    }
}
