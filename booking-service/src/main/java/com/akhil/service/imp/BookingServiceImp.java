package com.akhil.service.imp;

import com.akhil.DTO.BookingRequest;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.ServiceDto;
import com.akhil.DTO.UserDTO;
import com.akhil.domain.BookingStatus;
import com.akhil.model.Booking;
import com.akhil.model.SalonReport;
import com.akhil.repo.BookingRepo;
import com.akhil.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImp implements BookingService {

    @Autowired
    private BookingRepo repo;

    @Override
    public Booking createBooking(BookingRequest bookingRequest, UserDTO userDTO, SaloonDTO saloonDTO, Set<ServiceDto> serviceDto) {
        int totalDuration=serviceDto.stream().mapToInt(ServiceDto::getDuration).sum();
        LocalDateTime bookingStartTime=bookingRequest.getStartTime();
        LocalDateTime bookingEndTime=bookingStartTime.plusMinutes(totalDuration);

        Boolean isSlotAvailable=isTimeSlotAvailable(saloonDTO,bookingStartTime,bookingEndTime);

        int totalPrice=serviceDto.stream().mapToInt(ServiceDto::getPrice).sum();
        Set<Long>idsList=serviceDto.stream().map(ServiceDto::getId).collect(Collectors.toSet());

        Booking newBooking=new Booking();
        newBooking.setCustomerId(userDTO.getId());
        newBooking.setSalonId(saloonDTO.getId());
        newBooking.setServiceIds(idsList);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setTotalPrice(totalPrice);

        return repo.save(newBooking);
    }

    public Boolean isTimeSlotAvailable(SaloonDTO saloonDTO,LocalDateTime bookingStartTime,
                                       LocalDateTime bookingEndTime){

        if (saloonDTO == null || saloonDTO.getOpenTime() == null || saloonDTO.getCloseTime() == null) {
            throw new IllegalArgumentException("Salon operational hours are missing or invalid.");
        }
        LocalDateTime salonOpenTime=saloonDTO.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime=saloonDTO.getCloseTime().atDate(bookingStartTime.toLocalDate());

        List<Booking> existingBookings=getBookingBySalon(saloonDTO.getId());
        if(bookingStartTime.isBefore(salonOpenTime) || bookingStartTime.isAfter(salonCloseTime)){
            throw  new RuntimeException("Booking time must be within salon's working hours");
        }

        for(Booking booking:existingBookings){
            LocalDateTime existingBookingStartTime=booking.getStartTime();
            LocalDateTime existingBookingEndTime=booking.getEndTime();

            if(bookingStartTime.isBefore(existingBookingEndTime) && bookingEndTime.isAfter(existingBookingStartTime)){
                throw  new RuntimeException("Slot not available. choose different time");

            }
            if(bookingStartTime.isEqual(existingBookingStartTime) || bookingEndTime.isEqual(existingBookingEndTime)){
                throw  new RuntimeException("Slot not available. choose different time");

            }


        }
        return true;
    }

    @Override
    public List<Booking> getBookingByCustomer(Long customerId) {
        return repo.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingBySalon(Long salonId) {
        return repo.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long id) {
        Booking booking=repo.findById(id).orElse(null);
        if(booking==null){
            throw new RuntimeException("Booking not found");
        }

        return  booking;
    }

    @Override
    public Booking updateBooking(Long bookingId, BookingStatus status) {
        Booking booking=getBookingById(bookingId);

        booking.setStatus(status);
        return repo.save(booking);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {

        List<Booking>allBooking=getBookingBySalon(salonId);
        if(date==null){
            return allBooking;

        }

    return     allBooking.stream().filter(booking->isSameData(booking.getStartTime(),date) ||
                isSameData(booking.getEndTime(),date)).collect(Collectors.toList());
    }

    private boolean isSameData(LocalDateTime dateTime, LocalDate date) {

        return   dateTime.toLocalDate().isEqual(date);
    }


    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking>allBooking=getBookingBySalon(salonId);
        Double totalEarnings=allBooking.stream().mapToDouble(Booking::getTotalPrice).sum();
        Integer totalBooking=allBooking.size();
        List<Booking> cancelledBookings=allBooking.stream().filter(booking -> booking.getStatus().equals(BookingStatus.CANCEL)).collect(Collectors.toList());
        Double totalRefund=cancelledBookings.stream().mapToDouble(Booking::getTotalPrice).sum();
        SalonReport report=new SalonReport();
        report.setSalonId(salonId);
//        report.setSalonName();
        report.setCancelBookings(cancelledBookings.size());
        report.setTotalBookings(totalBooking);
        report.setTotalEarnings(totalEarnings);
        report.setTotalRefund(totalRefund);

        return report;
    }
}
