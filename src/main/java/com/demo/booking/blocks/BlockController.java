package com.demo.booking.blocks;

import com.demo.booking.booking.BookingService;
import com.demo.booking.booking.BookingUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/blocks")
public class BlockController
{
    private BookingService service;

    public BlockController( BookingService service )
    {
        this.service = service;
    }

    @PostMapping("/")
    public Long create( @Valid @RequestBody BlockCreateRequest payload )
    {
        return service.book( payload.getProperty(), null, payload.getFrom(), payload.getTo(), null ).getId();
    }

    @PutMapping("/{id}")
    public void update( @PathVariable Long id, @Valid @RequestBody BlockUpdateRequest payload )
    {
        service.update( id, payload.getFrom(), payload.getTo(), null );
    }

    @DeleteMapping("/{id}/delete")
    public void delete( @PathVariable Long id )
    {
        service.delete( id );
    }
}
