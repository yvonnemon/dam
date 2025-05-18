package org.example.dam.service;

import org.example.dam.dto.FlightDTO;
import org.example.dam.model.Airport;
import org.example.dam.model.Flight;

import org.example.dam.model.Plane;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CronService {

    private final FlightService flightRepository;
    private final AirportService airportService;
    private final PlaneService planeService;

    public CronService(FlightService flightRepository, AirportService airportService, PlaneService planeService) {
        this.flightRepository = flightRepository;

        this.airportService = airportService;
        this.planeService = planeService;
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void createFlights(){
        createMultipleFlights(5);
    }

    private void createMultipleFlights(int count) {
        System.out.println("Creating " + count + " flights");
        List<Airport> airports = airportService.findAll();
        List<Plane> planes = planeService.findAll();

        if (airports.size() < 2 || planes.size() < 2) {
            throw new IllegalStateException("Not enough airports or planes to create flights.");
        }

        for (int i = 0; i < count; i++) {
            Airport from, to;
            Plane p;

            // Get two different airports
            do {
                from = airports.get((int) (Math.random() * airports.size()));
                to = airports.get((int) (Math.random() * airports.size()));
                p = planes.get((int) (Math.random() * planes.size()));
            } while (from.equals(to));

            FlightDTO flight = new FlightDTO();
            flight.setDepartureId(from.getId());
            flight.setDestinationId(to.getId());
            flight.setModelId(p.getId());
            flight.setDepartureDate(LocalDateTime.now().plusDays(i + 1).withHour(10).withMinute(30));


            flightRepository.save(flight);

        }
    }
}
