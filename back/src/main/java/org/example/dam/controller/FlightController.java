package org.example.dam.controller;

import org.example.dam.dto.FlightResponseDTO;
import org.example.dam.model.Flight;
import org.example.dam.model.Flight;
import org.example.dam.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public ResponseEntity<Flight> createFlight(Flight flight) {
        return ResponseEntity.ok(flightService.save(flight));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return flightService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.findAll());
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<Flight> updateFlight(@RequestBody Flight newFlight, @PathVariable Long id) {
        return ResponseEntity.ok(
                flightService.findById(id)
                        .map(flight -> {
                            flight.setFlightNumber(newFlight.getFlightNumber());
                            flight.setOrigin(newFlight.getOrigin());
                            flight.setDestination(newFlight.getDestination());
                            //TODO
                            flight.setDeparture(newFlight.getDeparture());
                            flight.setDuration(newFlight.getDuration());

                            // TODO If dateOfBirth is a LocalDate or Date, uncomment this:
                            // flight.setDateOfBirth(newFlight.getDateOfBirth());
                            return flightService.save(flight);
                        })
                        .orElseGet(() -> {
                            newFlight.setId(id); // optional: set ID manually
                            return flightService.save(newFlight);
                        })
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        if (flightService.findById(id).isPresent()) {
            flightService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
