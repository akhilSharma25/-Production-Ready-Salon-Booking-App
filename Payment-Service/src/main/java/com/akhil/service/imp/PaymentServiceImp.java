package com.akhil.service.imp;

import com.akhil.domain.PaymentMethod;
import com.akhil.domain.PaymentOrderStatus;
import com.akhil.model.PaymentOrder;
import com.akhil.payload.DTO.BookingDTO;
import com.akhil.payload.DTO.UserDTO;
import com.akhil.payload.response.PaymentLinkResponse;
import com.akhil.repo.PaymentRepo;
import com.akhil.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImp implements PaymentService {

    @Autowired
    private PaymentRepo repo;

    @Value("${stripe.api.secret}")
    private String stripeSecretKey;
    @Value("${razorpay.api.secret}")
    private String razorpaySecretKey;

    @Value("razorpay.api.key")
    private String razorpayApiKey;
    @Value("stripe.api.key")
    private String stripeApiKey;

    @Override
    public PaymentLinkResponse createOrder(UserDTO userDto, BookingDTO bookingDto, PaymentMethod paymentMethod) throws RazorpayException, StripeException {
        Long amount=(long)bookingDto.getTotalPrice();
        PaymentOrder order=new PaymentOrder();
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);
        order.setBookingId(bookingDto.getId());

        order.setSalonId(bookingDto.getSalonId());

        PaymentOrder savedOrder=repo.save(order);

        PaymentLinkResponse paymentLinkResponse=new PaymentLinkResponse();
        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink payment=createRazorpayPaymentLink(userDto, savedOrder.getAmount(), savedOrder.getId());

            String paymentUrl=payment.get("short_url");
            String paymentUrlId=payment.get("id");
            paymentLinkResponse.setPayment_link_url(paymentUrl);

            paymentLinkResponse.setGetPayment_link_id(paymentUrlId);
            savedOrder.setPaymentLinkId(paymentUrlId);

            repo.save(savedOrder);

        }else{
            String paymentUrl=createStripePaymentLink(userDto, savedOrder.getAmount(), savedOrder.getId());

            paymentLinkResponse.setPayment_link_url(paymentUrl);


            repo.save(savedOrder);
        }


        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) {
        PaymentOrder paymentOrder=repo.findById(id).orElseThrow(()->new RuntimeException("payment order not found"));
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return repo.findByPaymentLinkId(paymentId);
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(UserDTO userDTO, Long amount, Long orderId) throws RazorpayException {

        Long amountt=amount*100;
        RazorpayClient razorpayClient=new RazorpayClient(razorpayApiKey,razorpaySecretKey);

        JSONObject paymentLinkRequest=new JSONObject();
        paymentLinkRequest.put("amount",amountt);
        paymentLinkRequest.put("currency","INR");

        JSONObject customer=new JSONObject();
        customer.put("name",userDTO.getFullName());
        customer.put("email",userDTO.getEmail());

        paymentLinkRequest.put("customer",customer);
        paymentLinkRequest.put("reminder_enable",true);
        JSONObject notify=new JSONObject();
        notify.put("email",true);

        paymentLinkRequest.put("notify",notify);

        paymentLinkRequest.put("callback_url","http://localhost:3000/payment-success/"+orderId);
        paymentLinkRequest.put("callback_method","get");

        PaymentLink paymentLink=razorpayClient.paymentLink.create(paymentLinkRequest);


        return paymentLink;
    }

    @Override
    public String createStripePaymentLink(UserDTO userDTO, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey=stripeApiKey;
        SessionCreateParams params= SessionCreateParams.builder().addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD).setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl("http://localhost:3000/payment-success/"+orderId).setCancelUrl("http://localhost:3000/payment-cancel").addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency("usd").setUnitAmount(amount*100).setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("Salon appointment booking").build()).build()).build()).build();

        Session session=Session.create(params);

        return session.getUrl();
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {

        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient=new RazorpayClient(razorpayApiKey,razorpaySecretKey);
                Payment payment=razorpayClient.payments.fetch(paymentId);
                Integer amount=payment.get("amount");
                String status=payment.get("status");

                if(status.equals("captured")){
                    //produce kafka event
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    repo.save(paymentOrder);
                    return  true;
                }
                else{
                    return  false;
                }
            }else{
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                repo.save(paymentOrder);
                return  true;
            }
        }
        return false;
    }
}
