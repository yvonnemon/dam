package org.example.dam.repository;

import org.example.dam.model.Booking;
import org.example.dam.model.BookingStatus;
import org.example.dam.model.Flight;
import org.example.dam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findBookingByBookingNumber(String number);
    List<Booking> findBookingsByUserOrderByStatusAsc(User user);
    boolean existsByUserAndFlightAndStatus(User user, Flight flight, BookingStatus status);
    List<Booking> findByUserAndStatusNot(User user, BookingStatus status);
}
