package com.akhil.service;

import com.akhil.domain.PaymentMethod;
import com.akhil.model.PaymentOrder;
import com.akhil.payload.DTO.BookingDTO;
import com.akhil.payload.DTO.UserDTO;
import com.akhil.payload.response.PaymentLinkResponse;

public interface PaymentService {

    PaymentLinkResponse createOrder(UserDTO userDto, BookingDTO bookingDto, PaymentMethod  paymentMethod);

    PaymentOrder getPaymentOrderById(Long id);

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

}
