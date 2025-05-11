package org.example.dam.service;

import org.example.dam.dto.BookingDTO;
import org.example.dam.model.Booking;
import org.example.dam.repository.BookingRepository;
import org.example.dam.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<BookingDTO> findAll() {

        List<BookingDTO> bookings = new ArrayList<>();
        for (Booking booking : bookingRepository.findAll()) {
            bookings.add(entityToDto(booking));
        }

        return bookings;
    }

    public BookingDTO findById(Long id) {
        return entityToDto( bookingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found")));
    }

    public BookingDTO save(BookingDTO user) {
        return entityToDto(bookingRepository.save(dtoToEntity(user)));
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    private Booking dtoToEntity(BookingDTO dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        if(dto.getId() == null) { //si no tiene id es nuevo

            booking.setBookingNumber(generateUniqueBookingCode());
            booking.setPrice( dto.getFlight().getDuration() * 5000); //5000 dineros por hora
        } else {
            booking.setBookingNumber(dto.getNumber());
            booking.setPrice(dto.getPrice());
        }

        booking.setDatePurchase(dto.getDatePurchase());
        //booking.setPrice(dto.getPrice());
        booking.setStatus(dto.getStatus());
        booking.setUser(dto.getUser());
        booking.setFlight(dto.getFlight());
        return booking;
    }

    private BookingDTO entityToDto(Booking booking) {

        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
      //  String code = Util.getSaltString();
       // dtoRepository.findBookingDTOByNumber(code);

        dto.setNumber(booking.getBookingNumber());
        dto.setDatePurchase(booking.getDatePurchase());
        dto.setPrice(booking.getPrice());
        dto.setStatus(booking.getStatus());
        dto.setUser(booking.getUser());
        dto.setFlight(booking.getFlight());

        return dto;
    }

    private String generateUniqueBookingCode() {
        String code = Util.getSaltString();
        if (exists(code) == null) {
            return code;
        } else {
            return generateUniqueBookingCode(); // recursion
        }
    }
    private Booking exists(String bookingNumber) {
        return bookingRepository.findBookingByBookingNumber(bookingNumber);
    }
}
