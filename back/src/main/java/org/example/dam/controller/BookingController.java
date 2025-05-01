package org.example.dam.controller;

import org.example.dam.model.Booking;
import org.example.dam.model.Booking;
import org.example.dam.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Booking> createUser(@RequestBody Booking user) {
        return ResponseEntity.ok(bookingService.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getUserById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/users")
    public ResponseEntity<List<Booking>> getAllUsers() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Booking> updateUser(@RequestBody Booking newBoking, @PathVariable Long id) {
        return ResponseEntity.ok(
                bookingService.findById(id)
                        .map(booking -> {
                            booking.setNumber(newBoking.getNumber());
                            booking.setDatePurchase(newBoking.getDatePurchase());
                            booking.setPrice(newBoking.getPrice());
                            booking.setStatus(newBoking.getStatus());

                            //TODO relations
                            booking.setUser(newBoking.getUser());
                            booking.setFlight(newBoking.getFlight());

                            // TODO If dateOfBirth is a LocalDate or Date, uncomment this:
                            // user.setDateOfBirth(newBoking.getDateOfBirth());
                            return bookingService.save(booking);
                        })
                        .orElseGet(() -> {
                            newBoking.setId(id); // optional: set ID manually
                            return bookingService.save(newBoking);
                        })
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (bookingService.findById(id).isPresent()) {
            bookingService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
