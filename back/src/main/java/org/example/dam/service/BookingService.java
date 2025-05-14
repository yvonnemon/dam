package org.example.dam.service;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.dam.dto.BookingDTO;
import org.example.dam.model.Booking;
import org.example.dam.model.BookingStatus;

import org.example.dam.model.Flight;
import org.example.dam.model.User;
import org.example.dam.repository.BookingRepository;
import org.example.dam.repository.FlightRepository;
import org.example.dam.repository.UserRepository;
import org.example.dam.util.EmailsSender;
import org.example.dam.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    @Autowired
    private EmailsSender emailSender;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;

    }

    public List<BookingDTO> findAll() {

        List<BookingDTO> bookings = new ArrayList<>();
        for (Booking booking : bookingRepository.findAll()) {
            bookings.add(entityToDto(booking));
        }

        return bookings;
    }

    public List<BookingDTO> findAllFromUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<BookingDTO> bookings = new ArrayList<>();
        for (Booking booking : bookingRepository.findBookingsByUser(user)) {
            BookingDTO bookingDTO = entityToDto(booking);
            bookingDTO.setUser(null);
            bookings.add(bookingDTO);
        }

        return bookings;
    }

    public List<BookingDTO> findByUserAndStatusNot() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<BookingDTO> bookings = new ArrayList<>();
        for (Booking booking : bookingRepository.findByUserAndStatusNot(user, BookingStatus.CANCELLED)) {
            BookingDTO bookingDTO = entityToDto(booking);
            bookingDTO.setUser(null);
            bookings.add(bookingDTO);
        }

        return bookings;
    }



    public BookingDTO findById(Long id) {
        return entityToDto(bookingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found")));
    }

    @Transactional
    public BookingDTO save(BookingDTO booking) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        booking.setUser(user);

        if (booking.getId() == null) {
            booking.setStatus(BookingStatus.BOOKED);
            Flight flight = flightRepository.findById(booking.getFlightId())
                    .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

            Booking bookingEntity = dtoToEntity(booking);

            try {
                bookingEntity = bookingRepository.save(bookingEntity); // Try to insert
                sendBookingConfirmation("yvosobrero2@hotmail.com", booking.getUser().getFirstName(), bookingEntity.getBookingNumber()); //TODO hardcodeado el email para que me lleguen
                flight.setSeatsReserved(flight.getSeatsReserved() + 1);
                flightRepository.save(flight);

            } catch (DataIntegrityViolationException ex) {
                // Booking insert failed, flight should not be updated
                throw new RuntimeException("Booking failed: " + ex.getMessage(), ex);
            }


            return entityToDto(bookingEntity);

        } else {
            booking.setStatus(BookingStatus.CANCELLED);
            return entityToDto(bookingRepository.save(dtoToEntity(booking)));
        }
    }

    public void deleteById(Long id) {
        Booking b = bookingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        b.setStatus(BookingStatus.CANCELLED);
        // quitar el asiento reservado
        Flight f = b.getFlight();
        f.setSeatsReserved(f.getSeatsReserved() - 1);
        flightRepository.save(f);
        bookingRepository.save(b);
        //bookingRepository.deleteById(id);
    }

    public byte[] generatePdfFromHtml() throws IOException {
       // ("templates/email_template.html");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            doc.add(new Paragraph("✈️ Your Flight Ticket"));
            doc.add(new Paragraph("Booking Number: " + "bookingnumber"));
            doc.add(new Paragraph("Flight Details: " + "flightDetails"));

            doc.add(new Paragraph("Thank you for booking with us!"));

            doc.close();
            return baos.toByteArray();
        }
    }

    private Booking dtoToEntity(BookingDTO dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        Flight flight = flightRepository.findById(dto.getFlightId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));
        booking.setFlight(flight);
        if (dto.getId() == null) { //si no tiene id es nuevo

            booking.setBookingNumber(generateUniqueBookingCode());
            booking.setPrice(flight.getDuration() * 5000); //5000 dineros por hora
        } else {
            booking.setBookingNumber(dto.getNumber());
            booking.setPrice(dto.getPrice());
        }

        booking.setDatePurchase(dto.getDatePurchase());
        //booking.setPrice(dto.getPrice());
        booking.setStatus(dto.getStatus());
        booking.setUser(dto.getUser());
        //no deberia dar nunca la excepcion

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

    private void sendBookingConfirmation(String userEmail, String userName, String bookingDetails) {
        try {
            String subject = "Booking Confirmation";

            String dataAsBody = bookingDetails;

            emailSender.sendEmailWithAttachment(subject, dataAsBody, userEmail, userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
