package com.akhil.service;

import com.akhil.DTO.BookingRequest;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.ServiceDto;
import com.akhil.DTO.UserDTO;
import com.akhil.domain.BookingStatus;
import com.akhil.model.Booking;
import com.akhil.model.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(BookingRequest bookingRequest, UserDTO userDTO, SaloonDTO saloonDTO, Set<ServiceDto> serviceDto);
    List<Booking> getBookingByCustomer(Long customerId);
    List<Booking> getBookingBySalon(Long salonId);
    Booking getBookingById(Long id);
    Booking updateBooking(Long bookingId, BookingStatus status);
    List<Booking> getBookingsByDate(LocalDate date,Long salonId);
    SalonReport getSalonReport(Long salonId);

}
