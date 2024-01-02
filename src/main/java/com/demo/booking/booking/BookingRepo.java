package com.demo.booking.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepo extends JpaRepository<Booking, Long>, BookingRepoCustom
{
}