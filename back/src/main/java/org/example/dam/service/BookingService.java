package org.example.dam.service;

import org.example.dam.model.Booking;
import org.example.dam.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    //TODO update y create?
    public Booking save(Booking user) {
        return bookingRepository.save(user);
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

}
