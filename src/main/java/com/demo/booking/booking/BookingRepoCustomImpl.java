package com.demo.booking.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookingRepoCustomImpl implements BookingRepoCustom
{
    private EntityManager em;

    public BookingRepoCustomImpl( EntityManager em )
    {
        this.em = em;
    }

    @Override
    public List<Booking> findExistingBookings( Long propertyId, LocalDate from, LocalDate to, Boolean cancelled )
    {
        Map<String, Object> params = new HashMap<>();
        String query = "SELECT b FROM Booking b JOIN FETCH b.property JOIN FETCH b.guest" +
            " WHERE b.property.id = :propertyId AND (b.from BETWEEN :from AND :to OR b.to BETWEEN :from AND :to)";

        params.put( "propertyId", propertyId );
        params.put( "from", from );
        params.put( "to", to );

        if ( cancelled != null ) {
            query += " AND b.cancelled = :cancelled";
            params.put( "cancelled", cancelled );
        }

        TypedQuery<Booking> bookings = em.createQuery( query, Booking.class );
        params.forEach( ( k, v ) -> bookings.setParameter( k, v ) );

        return bookings.getResultList();
    }
}
