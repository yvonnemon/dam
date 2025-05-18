package org.example.dam.repository;

import org.example.dam.model.Flight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("SELECT f FROM Flight f " +
            "WHERE (:airport IS NULL OR LOWER(f.departureAirport.name) LIKE LOWER(CONCAT('%', :airport, '%')) " +
            "   OR LOWER(f.destinationAirport.name) LIKE LOWER(CONCAT('%', :airport, '%'))) " +
            "AND (:date IS NULL OR CAST(f.departureDate AS string) LIKE CONCAT(:date, '%'))")
    Page<Flight> findFilteredFlights(
            @Param("airport") String airport,
            @Param("date") String date,
            Pageable pageable);

}
