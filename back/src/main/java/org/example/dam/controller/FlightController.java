package org.example.dam.controller;

import org.example.dam.dto.FlightResponseDTO;
import org.example.dam.dto.FlightDTO;
import org.example.dam.dto.FlightDTO;
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
    public ResponseEntity<FlightDTO> createFlight(FlightDTO flight) {
        return ResponseEntity.ok(flightService.save(flight));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id) {
        return flightService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/flights")
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        return ResponseEntity.ok(flightService.findAll());
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@RequestBody FlightDTO newFlight, @PathVariable Long id) {
        return ResponseEntity.ok(
                flightService.findById(id)
                        .map(flight -> {
                            flight.setFlightNumber(newFlight.getFlightNumber());
                            flight.setDepartureAirport(newFlight.getDepartureAirport());
                            flight.setDestinationAirport(newFlight.getDestinationAirport());
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
