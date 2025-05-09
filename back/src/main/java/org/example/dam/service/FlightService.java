package org.example.dam.service;

import org.example.dam.dto.FlightDTO;
import org.example.dam.model.Airport;
import org.example.dam.model.Flight;
import org.example.dam.model.Plane;
import org.example.dam.repository.AirportRepository;
import org.example.dam.repository.FlightRepository;
import org.example.dam.repository.PlaneRepository;
import org.example.dam.util.Util;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final PlaneRepository planeRepository;

    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository, PlaneRepository planeRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.planeRepository = planeRepository;
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    public Flight save(FlightDTO flight) {
        Flight newFlight = new Flight();


        Airport departure = airportRepository.findById(flight.getDepartureId())
                .orElseThrow(() -> new RuntimeException("Invalid departure airport"));

        Airport destination = airportRepository.findById(flight.getDestinationId())
                .orElseThrow(() -> new RuntimeException("Invalid destination airport"));

        newFlight.setDepartureAirport(departure);
        newFlight.setDestinationAirport(destination);
        newFlight.setDeparture(flight.getDepartureDate());
        double disntance = Util.getDistanceFromLatLonInKm(departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
        newFlight.setDuration( disntance/750.0 ); //750 km/h es la velocidad media de crucero de un jet privado
        Plane plane = planeRepository.findById(flight.getModelId())
                .orElseThrow(() -> new RuntimeException("Invalid destination airport"));
        newFlight.setPlane(plane);

        return flightRepository.save(newFlight);
    }

    //TODO tiene que returnear algo
    public void delete(Long flight) {
        flightRepository.deleteById(flight);
    }
}
