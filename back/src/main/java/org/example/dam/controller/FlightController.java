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
    public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightDTO flight) {
        return ResponseEntity.ok(flightService.save(flight));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.findById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        return ResponseEntity.ok(flightService.findAll());
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@RequestBody FlightDTO newFlight, @PathVariable Long id) {
        return ResponseEntity.ok(flightService.save(newFlight));
    }

    @DeleteMapping("/{id}")
    //TODO
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        if (flightService.findById(id) != null) {
            flightService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
