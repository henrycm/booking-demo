package com.demo.booking.booking;

import com.demo.booking.guest.Guest;
import com.demo.booking.property.Property;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Booking
{
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "start_date")
    private LocalDate from;

    @NotNull
    @Column(name = "end_date")
    private LocalDate to;

    @NotNull
    @ManyToOne
    private Property property;

    @ManyToOne
    private Guest guest;

    private String otherGuestNames;

    @NotNull
    @Column(columnDefinition = "boolean default false")
    private Boolean cancelled;

    @NotNull
    @Column(columnDefinition = "boolean default false")
    private Boolean block;
}
