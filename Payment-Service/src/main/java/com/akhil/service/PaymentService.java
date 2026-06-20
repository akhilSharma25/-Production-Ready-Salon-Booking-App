package com.akhil.service;

import com.akhil.domain.PaymentMethod;
import com.akhil.model.PaymentOrder;
import com.akhil.payload.DTO.BookingDTO;
import com.akhil.payload.DTO.UserDTO;
import com.akhil.payload.response.PaymentLinkResponse;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentLinkResponse createOrder(UserDTO userDto, BookingDTO bookingDto, PaymentMethod  paymentMethod) throws RazorpayException, StripeException;

    PaymentOrder getPaymentOrderById(Long id);

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

    PaymentLink createRazorpayPaymentLink(UserDTO userDTO,Long amount,Long orderId) throws RazorpayException;
    String createStripePaymentLink(UserDTO userDTO,Long amount,Long orderId) throws StripeException;

    Boolean proceedPayment(PaymentOrder paymentOrder,String paymentId,String paymentLinkId) throws RazorpayException;
}
