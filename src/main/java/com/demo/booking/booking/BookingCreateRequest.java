package com.demo.booking.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingCreateRequest
{
    @NotNull(message = "Start date is required")
    private LocalDate from;
    @NotNull(message = "End date is required")
    private LocalDate to;
    @NotNull(message = "Property is required")
    private Long property;
    @NotNull(message = "Guest is required")
    private Long guest;

    private String otherGuestNames;
}
