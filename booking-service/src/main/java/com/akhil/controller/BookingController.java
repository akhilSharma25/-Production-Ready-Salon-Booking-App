package com.akhil.controller;

import com.akhil.DTO.*;
import com.akhil.domain.BookingStatus;
import com.akhil.mapper.BookingMapper;
import com.akhil.model.Booking;
import com.akhil.model.SalonReport;
import com.akhil.service.BookingService;
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

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestParam Long salonId, @RequestBody BookingRequest bookingRequest){


        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);

        SaloonDTO saloonDTO=new SaloonDTO();
        saloonDTO.setId(salonId);
        saloonDTO.setOpenTime(LocalTime.of(9, 0));   // Subah 09:00 baje khulega
        saloonDTO.setCloseTime(LocalTime.of(21, 0));

        Set<ServiceDto> serviceDtoSet=new HashSet<>();
        ServiceDto serviceDto1=new ServiceDto();
        serviceDto1.setId(1L);
        serviceDto1.setPrice(399);
        serviceDto1.setDuration(45);
        serviceDto1.setName("Hair cut for men");
        serviceDtoSet.add(serviceDto1);


        Booking booking=service.createBooking(bookingRequest,userDTO,saloonDTO,serviceDtoSet);
        return ResponseEntity.ok(booking);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(){
        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);

        List<Booking> bookings=service.getBookingByCustomer(1L);


        return  ResponseEntity.ok(getBookingDto(bookings));
    }


    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon(){


        List<Booking> bookings=service.getBookingBySalon(101L);


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
    public ResponseEntity< SalonReport> getSalonReport(){

      com.akhil.model.SalonReport salonReport =  service.getSalonReport(101L);
      return ResponseEntity.ok(salonReport);

    }

    private  Set<BookingDTO> getBookingDto(    List<Booking> bookings){

      return bookings.stream().map(booking -> BookingMapper.bookingDTO(booking) ).collect(Collectors.toSet());
    }
}
