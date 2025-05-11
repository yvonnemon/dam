package org.example.dam.repository;

import org.example.dam.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findBookingByBookingNumber(String number);
}
