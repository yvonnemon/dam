package org.example.dam.service;

import org.example.dam.dto.FlightDTO;
import org.example.dam.model.Airport;
import org.example.dam.model.Flight;
import org.example.dam.model.Plane;
import org.example.dam.repository.AirportRepository;
import org.example.dam.repository.FlightRepository;
import org.example.dam.repository.PlaneRepository;
import org.example.dam.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

    public List<FlightDTO> findAll() {

        List<FlightDTO> flights = new ArrayList<>();
        for (Flight flight : flightRepository.findAll()) {
            flights.add(entityToDto(flight));
        }

        return flights;
    }

    public FlightDTO findById(Long id) {
        return entityToDto(flightRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found")));
    }

    public FlightDTO save(FlightDTO flight) {
//        if(flight.getId() == null) {
//            flight.setFlightNumber("SB" + flight.getDepartureDate().toString());
//
//            Airport departure = airportRepository.findById(flight.getDepartureId())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Airport not found"));
//
//            Airport destination = airportRepository.findById(flight.getDestinationId())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Airport not found"));
//
//            double disntance = Util.getDistanceFromLatLonInKm(departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
//            flight.setDuration(disntance / 750.0); //750 km/h es la velocidad media de crucero de un jet privado
//            Plane plane = planeRepository.findById(flight.getModelId())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plane model not found"));
//            flight.set
//        }
        return entityToDto(flightRepository.save(dtoToEntity(flight)));
    }

    //TODO tiene que returnear algo
    public void delete(Long flight) {
        flightRepository.deleteById(flight);
    }

    private Flight dtoToEntity(FlightDTO dto) {
        Flight flight = new Flight();
        if(dto.getId() == null) {
            flight.setFlightNumber("SB"+dto.getDepartureDate().toString());
           // dto.setSeatsAvailable(entity.getPlane().getCapacity()-entity.getSeatsReserved());
            //flight.setSeatsReserved(dto.get);
        } else {
            flight.setFlightNumber(dto.getFlightNumber());
        }
        //la entidad neceista el aeropuerto y el avion.classes
        Airport departure = airportRepository.findById(dto.getDepartureId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Airport not found"));

        Airport destination = airportRepository.findById(dto.getDestinationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Airport not found"));

        flight.setDepartureAirport(departure);
        flight.setDestinationAirport(destination);

        flight.setDepartureDate(dto.getDepartureDate());

        double disntance = Util.getDistanceFromLatLonInKm(departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
        flight.setDuration( disntance/750.0 ); //750 km/h es la velocidad media de crucero de un jet privado
        Plane plane = planeRepository.findById(dto.getModelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plane model not found"));
        flight.setPlane(plane);

        return flight;
    }

    private FlightDTO entityToDto(Flight entity) {
        FlightDTO dto = new FlightDTO();

        dto.setId(entity.getId());
        dto.setModelId(entity.getPlane().getId());
        dto.setModelName(entity.getPlane().getName());
        dto.setSeatsTotal(entity.getPlane().getCapacity()); //TODO
        dto.setSeatsAvailable(entity.getPlane().getCapacity()-entity.getSeatsReserved());

        dto.setDepartureId(entity.getDepartureAirport().getId());
        dto.setDestinationId(entity.getDestinationAirport().getId());
        dto.setDepartureName(entity.getDepartureAirport().getName());
        dto.setDestinationName(entity.getDestinationAirport().getName());
        dto.setDepartureIata(entity.getDepartureAirport().getIata());
        dto.setDestinationIata(entity.getDestinationAirport().getIata());

        dto.setFlightNumber(entity.getFlightNumber());
        dto.setDepartureDate(entity.getDepartureDate());
        return dto;
    }
}
