package com.demo.booking.blocks;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BlockUpdateRequest
{
    @NotNull(message = "Start date is required")
    private LocalDate from;
    @NotNull(message = "End date is required")
    private LocalDate to;
}
