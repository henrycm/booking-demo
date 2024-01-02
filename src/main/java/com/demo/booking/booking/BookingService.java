package com.demo.booking.booking;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface BookingService
{
    Booking book(Long propertyId, Long guestId, LocalDate from, LocalDate to, String otherGuestNames );
    void cancel( Long bookingId );

    void rebook( Long bookingId );

    void update( Long bookingId, LocalDate from, LocalDate to, String otherGuestNames );

    void delete( Long bookingId );

    Booking get( Long bookingId );
}
