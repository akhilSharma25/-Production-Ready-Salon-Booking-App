package com.akhil.service.client;

import com.akhil.domain.PaymentMethod;
import com.akhil.payload.DTO.BookingDTO;
import com.akhil.payload.response.PaymentLinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {
    @PostMapping("api/payments/create")
    public ResponseEntity<com.akhil.payload.response.PaymentLinkResponse> createPaymentLink(
            @RequestBody com.akhil.payload.DTO.BookingDTO bookingDTO,
            @RequestParam PaymentMethod paymentMethod
    ) ;

    }
