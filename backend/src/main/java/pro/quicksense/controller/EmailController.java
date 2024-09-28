package pro.quicksense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.quicksense.common.ApiResponseBuilder;
import pro.quicksense.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/verify/{verifyType}")
    public ResponseEntity<Object> sendVerificationCode(@PathVariable String verifyType, @RequestParam String email) {
        emailService.sendVerificationCode(verifyType, email);
        return ApiResponseBuilder.success(HttpStatus.OK, "Verification code sent successfully", null);
    }
}