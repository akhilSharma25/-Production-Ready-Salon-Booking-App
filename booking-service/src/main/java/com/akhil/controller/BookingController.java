package com.akhil.controller;

import com.akhil.DTO.*;
import com.akhil.domain.BookingStatus;
import com.akhil.domain.PaymentMethod;
import com.akhil.mapper.BookingMapper;
import com.akhil.model.Booking;
import com.akhil.model.SalonReport;
import com.akhil.service.BookingService;
import com.akhil.service.client.PaymentFeignClient;
import com.akhil.service.client.SaloonFeignClient;
import com.akhil.service.client.ServiceOfferingFeignClient;
import com.akhil.service.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService service;

    @Autowired
    private SaloonFeignClient saloonFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ServiceOfferingFeignClient serviceOfferingFeignClient;

    @Autowired
    private PaymentFeignClient paymentFeignClient;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createBooking(@RequestParam Long salonId, @RequestParam PaymentMethod paymentMethod, @RequestBody BookingRequest bookingRequest, @RequestHeader("Authorization") String jwt){


        UserDTO userDTO=userFeignClient.getUserProfile(jwt).getBody();

        SaloonDTO saloonDTO=saloonFeignClient.getSalonById(salonId).getBody();
        Set<ServiceDto> serviceDtoSet=serviceOfferingFeignClient.getServicesById(bookingRequest.getServiceIds()).getBody();


        Booking booking=service.createBooking(bookingRequest,userDTO,saloonDTO,serviceDtoSet);
        if(serviceDtoSet==null){
            throw new RuntimeException("Services not found");
        }

        BookingDTO bookingDTO=BookingMapper.bookingDTO(booking);
        PaymentLinkResponse paymentLinkResponse=    paymentFeignClient.createPaymentLink(bookingDTO,paymentMethod,jwt).getBody();
//        System.out.println(        paymentFeignClient.createPaymentLink(bookingDTO,paymentMethod,jwt));
        return ResponseEntity.ok(paymentLinkResponse);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(@RequestHeader("Authorization") String jwt){
        UserDTO userDTO=userFeignClient.getUserProfile(jwt).getBody();


        if(userDTO==null || userDTO.getId()==null){
            throw new RuntimeException("User not found from jwt...");
        }
        List<Booking> bookings=service.getBookingByCustomer(userDTO.getId());


        return  ResponseEntity.ok(getBookingDto(bookings));
    }


    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon(@RequestHeader("Authorization") String jwt){

         SaloonDTO saloonDTO=saloonFeignClient.getSalonByOwnerId(jwt).getBody();
        List<Booking> bookings=service.getBookingBySalon(saloonDTO.getId());


        return  ResponseEntity.ok(getBookingDto(bookings));
    }



    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId){


        Booking booking=service.getBookingById(bookingId);


        return  ResponseEntity.ok(BookingMapper.bookingDTO(booking));
    }

  @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long bookingId, @RequestParam BookingStatus status){



        Booking booking=service.updateBooking(bookingId,status);


        return  ResponseEntity.ok(BookingMapper.bookingDTO(booking));
    }


    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity< List<BookingSlotDTO>> getBookedSlot(@PathVariable Long salonId,  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

        java.util.List<Booking> bookings=service.getBookingsByDate(date,salonId);

        List<BookingSlotDTO> slotDTOS=bookings.stream().
                map(booking -> {
                    BookingSlotDTO slotDTO=new BookingSlotDTO();
                    slotDTO.setEndTime(booking.getEndTime());
                    slotDTO.setStartTime(booking.getStartTime());
                    return slotDTO;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(slotDTOS);

    }

    @GetMapping("/report")
    public ResponseEntity< SalonReport> getSalonReport(@RequestHeader("Authorization") String jwt){

        SaloonDTO saloonDTO=saloonFeignClient.getSalonByOwnerId(jwt).getBody();

        com.akhil.model.SalonReport salonReport =  service.getSalonReport(saloonDTO.getId());
      return ResponseEntity.ok(salonReport);

    }

    private  Set<BookingDTO> getBookingDto(    List<Booking> bookings){

      return bookings.stream().map(booking -> BookingMapper.bookingDTO(booking) ).collect(Collectors.toSet());
    }
}
