package com.akhil.controller;

import com.akhil.domain.PaymentMethod;
import com.akhil.model.PaymentOrder;
import com.akhil.payload.DTO.BookingDTO;
import com.akhil.payload.DTO.UserDTO;
import com.akhil.payload.response.PaymentLinkResponse;
import com.akhil.service.PaymentService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO bookingDTO,
            @RequestParam PaymentMethod paymentMethod
            ) throws StripeException, RazorpayException {

        UserDTO userDTO=new UserDTO();
        userDTO.setFullName("Ashok");
        userDTO.setEmail("ashok@gmail.com");
        userDTO.setId(1L);

        PaymentLinkResponse res=service.createOrder(userDTO,bookingDTO,paymentMethod);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
          @PathVariable Long paymentOrderId
            )  {

        PaymentOrder res=service.getPaymentOrderById(paymentOrderId);
        return ResponseEntity.ok(res);
    }
    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(
          @RequestParam String paymentId,
          @RequestParam String paymentLinkId
            ) throws RazorpayException {

        PaymentOrder paymentOrder=service.getPaymentOrderByPaymentId(paymentLinkId);
        Boolean res=service.proceedPayment(paymentOrder,paymentId,paymentLinkId);
        return ResponseEntity.ok(res);
    }


}
