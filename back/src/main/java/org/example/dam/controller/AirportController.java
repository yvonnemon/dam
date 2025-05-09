package org.example.dam.controller;

import org.example.dam.model.Airport;
import org.example.dam.model.Flight;
import org.example.dam.service.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Airport>> getAirports() {
        return ResponseEntity.ok(airportService.findAll());
    }
}
