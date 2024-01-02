package com.demo.booking.booking;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/booking")
public class BookingController
{
    private BookingService service;

    public BookingController( BookingService service )
    {
        this.service = service;
    }

    @PostMapping("/")
    public Long create( @Valid @RequestBody BookingCreateRequest payload )
    {
        return service.book( payload.getProperty(), payload.getGuest(), payload.getFrom(), payload.getTo(), payload.getOtherGuestNames() ).getId();
    }

    @GetMapping("/{id}")
    public Booking get( @PathVariable Long id )
    {
        return service.get( id );
    }

    @PutMapping("/{id}")
    public void update( @PathVariable Long id, @Valid @RequestBody BookingUpdateRequest payload )
    {
        service.update( id, payload.getFrom(), payload.getTo(), payload.getOtherGuestNames() );
    }

    @PostMapping("/{id}/cancel")
    public void cancel( @PathVariable Long id )
    {
        service.cancel( id );
    }

    @PostMapping("/{id}/rebook")
    public void rebook( @PathVariable Long id )
    {
        service.rebook( id );
    }

    @DeleteMapping("/{id}/delete")
    public void delete( @PathVariable Long id )
    {
        service.delete( id );
    }
}
