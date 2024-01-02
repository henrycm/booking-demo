package com.demo.booking.booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepoCustom
{
    List<Booking> findExistingBookings(Long propertyId, LocalDate from, LocalDate to, Boolean active);
}
