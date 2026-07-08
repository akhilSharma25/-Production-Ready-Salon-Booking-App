package com.akhil.controller;

import com.akhil.domain.PaymentMethod;
import com.akhil.model.PaymentOrder;
import com.akhil.payload.DTO.BookingDTO;
import com.akhil.payload.DTO.UserDTO;
import com.akhil.payload.response.PaymentLinkResponse;
import com.akhil.service.PaymentService;
import com.akhil.service.client.PaymentFeignClient;
import com.akhil.service.client.SaloonFeignClient;
import com.akhil.service.client.ServiceOfferingFeignClient;
import com.akhil.service.client.UserFeignClient;
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

    @Autowired
    private SaloonFeignClient saloonFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ServiceOfferingFeignClient serviceOfferingFeignClient;

    @Autowired
    private PaymentFeignClient paymentFeignClient;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO bookingDTO,
            @RequestParam PaymentMethod paymentMethod
            ,
            @RequestHeader("Authorization")String jwt
            ) throws StripeException, RazorpayException {

        System.out.println("Payment Controller Hit");

        UserDTO userDTO=userFeignClient.getUserProfile(jwt).getBody();
        System.out.println(userDTO);
        System.out.println("User ID = " + userDTO.getId());
        PaymentLinkResponse res=service.createOrder(userDTO,bookingDTO,paymentMethod);
        System.out.println("Done");
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
