package com.demo.booking.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingUpdateRequest
{
    @NotNull(message = "Start date is required")
    private LocalDate from;
    @NotNull(message = "End date is required")
    private LocalDate to;

    private String otherGuestNames;
}
