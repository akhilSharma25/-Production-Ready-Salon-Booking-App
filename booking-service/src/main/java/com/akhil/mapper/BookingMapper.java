package com.akhil.mapper;

import com.akhil.DTO.BookingDTO;
import com.akhil.model.Booking;

public class BookingMapper {

    public  static BookingDTO bookingDTO(Booking booking){
        BookingDTO bookingDTO=new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setTotalPrice(booking.getTotalPrice());
        bookingDTO.setServiceIds(booking.getServiceIds());

        return bookingDTO;

    }
}
