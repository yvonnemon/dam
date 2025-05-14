package org.example.dam.controller;

import org.example.dam.dto.BookingDTO;
import org.example.dam.model.Booking;
import org.example.dam.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<BookingDTO> asd = bookingService.findByUserAndStatusNot();
        int a = asd.size();
        return ResponseEntity.ok(asd);
    }

    @GetMapping("/")
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

}
