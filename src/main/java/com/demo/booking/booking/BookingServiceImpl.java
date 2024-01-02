package com.demo.booking.booking;

import com.demo.booking.guest.Guest;
import com.demo.booking.property.Property;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService
{
    private BookingRepo repo;

    public BookingServiceImpl( BookingRepo repo )
    {
        this.repo = repo;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking book( Long propertyId, Long guestId, LocalDate from, LocalDate to, String otherGuestNames )
    {
        List<Booking> existingBookings = repo.findExistingBookings( propertyId, from, to, false );
        if ( existingBookings.isEmpty() ) {
            Booking booking = new Booking();
            booking.setCancelled( false );
            if ( guestId == null ) {
                booking.setBlock( true );
            } else {
                booking.setBlock( false );
                booking.setGuest( new Guest( guestId ) );
                booking.setOtherGuestNames( otherGuestNames );
            }
            booking.setProperty( new Property( propertyId ) );
            booking.setFrom( from );
            booking.setTo( to );

            return repo.save( booking );
        } else {
            handleAlreadyBooked( existingBookings );
        }

        return null;
    }

    @Override
    public void cancel( Long bookingId )
    {
        Booking booking = get( bookingId );
        if ( booking.getBlock() ) {
            throw new BookingException( "Booking is a block and cannot be cancelled." );
        }
        booking.setCancelled( true );

        repo.save( booking );
    }

    @Override
    public void rebook( Long bookingId )
    {
        Booking booking = get( bookingId );
        if ( booking.getBlock() ) {
            throw new BookingException( "Booking cannot be rebooked" );
        }
        List<Booking> existingBookings = repo.findExistingBookings( booking.getProperty().getId(), booking.getFrom(), booking.getTo(), null )
            .stream().filter( b -> !b.getId().equals( booking.getId() ) ).collect( Collectors.toList() );

        if ( existingBookings.isEmpty() ) {
            booking.setCancelled( false );
        } else {
            handleAlreadyBooked( existingBookings );
        }

        repo.save( booking );
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void update( Long bookingId, LocalDate from, LocalDate to, String otherGuestNames )
    {
        Booking booking = get( bookingId );
        List<Booking> existingBookings = repo.findExistingBookings( booking.getProperty().getId(), from, to, false )
            .stream().filter( b -> !b.getId().equals( booking.getId() ) ).collect( Collectors.toList() );

        if ( existingBookings.isEmpty() ) {
            booking.setFrom( from );
            booking.setTo( to );
            booking.setOtherGuestNames( otherGuestNames );

            repo.save( booking );
        } else {
            handleAlreadyBooked( existingBookings );
        }
    }

    @Override
    public void delete( Long bookingId )
    {
        repo.deleteById( bookingId );
    }

    @Override
    public Booking get( Long bookingId )
    {
        return repo.findById( bookingId ).orElseThrow( () -> new BookingException( "Booking not found" ) );
    }

    private void handleAlreadyBooked( List<Booking> existingBookings ) throws BookingException
    {
        String dates = existingBookings.stream().map( b -> "%s - %s".formatted( b.getFrom(), b.getTo() ) ).collect( Collectors.joining( ", " ) );
        throw new BookingException( "Property already booked: " + dates );
    }
}
