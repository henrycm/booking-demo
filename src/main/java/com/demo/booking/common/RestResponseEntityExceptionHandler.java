package com.demo.booking.common;

import com.demo.booking.booking.BookingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(value = { BookingException.class })
    protected ResponseEntity<Object> handle( BookingException ex, WebRequest request )
    {
        return handleExceptionInternal( ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request );
    }
}