package com.demo.booking.blocks;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BlockCreateRequest
{
    @NotNull(message = "Start date is required")
    private LocalDate from;
    @NotNull(message = "End date is required")
    private LocalDate to;
    @NotNull(message = "Property is required")
    private Long property;
}
