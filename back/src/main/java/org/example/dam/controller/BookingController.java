package org.example.dam.controller;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.example.dam.dto.BookingDTO;
import org.example.dam.model.Booking;
import org.example.dam.service.BookingService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO booking) {
        return ResponseEntity.ok(bookingService.save(booking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<BookingDTO>> getAllFromUser() {
        List<BookingDTO> asd = bookingService.findAllFromUser();
        int a = asd.size();
        return ResponseEntity.ok(asd);
    }

    @GetMapping("/user-booked")
    public ResponseEntity<List<BookingDTO>> findByUserAndStatusNot() {
        List<BookingDTO> asd = bookingService.findByUserAndStatus();
        int a = asd.size();
        return ResponseEntity.ok(asd);
    }

    @GetMapping("/admin") //get all only admin
    public ResponseEntity<List<BookingDTO>> getAllBookings() {

        return ResponseEntity.ok(bookingService.findAll());
    }

    @PutMapping("/booking/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@RequestBody BookingDTO newBoking, @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.save(newBoking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        if (bookingService.findById(id) != null) {
            bookingService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/ticket")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) throws IOException {
        // Generate PDF content
        BookingDTO booking = bookingService.findById(id);
        Map<String, String> flightDetails = new HashMap<>();
        flightDetails.put("Departure", booking.getFlight().getDepartureAirport().getName());
        flightDetails.put("Arrival", booking.getFlight().getDestinationAirport().getName());
        flightDetails.put("Date", booking.getFlight().getDepartureDate().toString());
        byte[] html = bookingService.generateFlightTicketPdf(booking.getNumber(), flightDetails); // You can template this however you want

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "booking.pdf");
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        System.out.println(html);
        return new ResponseEntity<>(html, headers, HttpStatus.OK);

    }


}
