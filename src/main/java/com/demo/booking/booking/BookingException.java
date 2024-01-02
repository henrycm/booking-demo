package com.demo.booking.booking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingException extends RuntimeException
{
    public BookingException( String message )
    {
        this.message = message;
    }

    private String message;
}
