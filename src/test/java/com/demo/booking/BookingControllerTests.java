package com.demo.booking;

import com.demo.booking.booking.Booking;
import com.demo.booking.booking.BookingRepo;
import com.demo.booking.booking.BookingCreateRequest;
import com.demo.booking.booking.BookingUpdateRequest;
import com.demo.booking.guest.Guest;
import com.demo.booking.property.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class BookingControllerTests
{
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepo repo;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup()
    {
        restTemplate.setUriTemplateHandler( new DefaultUriBuilderFactory( "http://localhost:" + port ) );
    }

    @AfterEach
    void clean()
    {
        repo.findAll().forEach( b -> repo.delete( b ) );
    }

    @Test
    void testCreateBooking()
    {
        ResponseEntity<String> booking = restTemplate.postForEntity( "/booking/", getDefault(), String.class );

        assertEquals( HttpStatus.OK, booking.getStatusCode() );
        assertNotNull( booking.getBody() );
    }

    @Test
    void testSameDates()
    {
        restTemplate.postForEntity( "/booking/", getDefault(), String.class );
        ResponseEntity<String> booking = restTemplate.postForEntity( "/booking/", getDefault(), String.class );

        assertEquals( HttpStatus.BAD_REQUEST, booking.getStatusCode() );
    }

    @Test
    void testOverlapping()
    {
        ResponseEntity<String> booking = restTemplate.postForEntity( "/booking/", getDefault(), String.class );
        assertEquals( HttpStatus.OK, booking.getStatusCode() );
        assertNotNull( booking.getBody() );

        booking = restTemplate.postForEntity( "/booking/", getDefault( "2024-01-04", "2024-01-06" ), String.class );

        assertEquals( HttpStatus.BAD_REQUEST, booking.getStatusCode() );
    }

    @Test
    void testRetrieve()
    {
        Booking booking = createDefault();

        ResponseEntity<Booking> bookingData = restTemplate.getForEntity( "/booking/" + booking.getId(), Booking.class );
        assertEquals( HttpStatus.OK, bookingData.getStatusCode() );

        assertNotNull( bookingData.getBody().getGuest().getName() );
        assertNotNull( bookingData.getBody().getProperty().getName() );
        assertEquals( booking.getFrom(), bookingData.getBody().getFrom() );
        assertEquals( booking.getTo(), bookingData.getBody().getTo() );
    }

    @Test
    void testCancel()
    {
        Booking booking = createDefault();

        ResponseEntity bookingResponse = restTemplate.postForEntity( "/booking/" + booking.getId() + "/cancel", null, Booking.class );
        assertEquals( HttpStatus.OK, bookingResponse.getStatusCode() );

        assertTrue( repo.findById( booking.getId() ).get().getCancelled() );
    }

    @Test
    void testRebook()
    {
        Booking booking = createDefault();

        ResponseEntity bookingResponse = restTemplate.postForEntity( "/booking/" + booking.getId() + "/rebook", null, Booking.class );
        assertEquals( HttpStatus.OK, bookingResponse.getStatusCode() );

        assertFalse( repo.findById( booking.getId() ).get().getCancelled() );
    }

    @Test
    void testDelete()
    {
        Booking booking = createDefault();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange( "/booking/" + booking.getId() + "/delete", DELETE, null, Void.class );
        assertEquals( HttpStatus.OK, deleteResponse.getStatusCode() );

        assertTrue( repo.findById( booking.getId() ).isEmpty() );
    }

    @Test
    void testUpdateDates()
    {
        Booking booking = createDefault();

        BookingUpdateRequest request = new BookingUpdateRequest();
        request.setFrom( LocalDate.parse( "2024-01-03" ) );
        request.setTo( LocalDate.parse( "2023-01-07" ) );

        ResponseEntity<Void> bookingResponse = restTemplate.exchange( "/booking/" + booking.getId(), PUT, new HttpEntity<>( request ), Void.class );
        assertEquals( HttpStatus.OK, bookingResponse.getStatusCode() );

        booking = repo.findById( booking.getId() ).get();
        assertEquals( request.getFrom(), booking.getFrom() );
        assertEquals( request.getTo(), booking.getTo() );
    }

    Booking createDefault()
    {
        BookingCreateRequest request = getDefault();
        Booking booking = new Booking();

        booking.setGuest( new Guest( request.getGuest() ) );
        booking.setProperty( new Property( request.getProperty() ) );
        booking.setFrom( request.getFrom() );
        booking.setTo( request.getTo() );
        booking.setCancelled( false );
        booking.setBlock( false );
        booking.setOtherGuestNames( "John, Carl" );

        return repo.save( booking );
    }

    private BookingCreateRequest getDefault()
    {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setGuest( 1L );
        request.setProperty( 1L );
        request.setFrom( LocalDate.parse( "2024-01-01" ) );
        request.setTo( LocalDate.parse( "2024-01-05" ) );

        return request;
    }

    private BookingCreateRequest getDefault( String from, String to )
    {
        BookingCreateRequest request = getDefault();
        request.setFrom( LocalDate.parse( from ) );
        request.setTo( LocalDate.parse( to ) );

        return request;
    }
}
