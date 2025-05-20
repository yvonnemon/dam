package org.example.dam.service;

import org.example.dam.dto.FlightDTO;
import org.example.dam.model.Airport;
import org.example.dam.model.Flight;
import org.example.dam.model.Plane;
import org.example.dam.repository.AirportRepository;
import org.example.dam.repository.FlightRepository;
import org.example.dam.repository.PlaneRepository;
import org.example.dam.util.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
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

    public Page<FlightDTO> getFilteredFlights(String airport, String date, Pageable pageable) {
        Page<Flight> page = flightRepository.findFilteredFlights(airport, date, pageable);
        return page.map(this::entityToDto);
    }

    public FlightDTO findById(Long id) {
        return entityToDto(flightRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found")));
    }

    public FlightDTO save(FlightDTO flight) {
        return entityToDto(flightRepository.save(dtoToEntity(flight)));
    }


    public void delete(Long flight) {
        flightRepository.deleteById(flight);
    }

   private Flight dtoToEntity(FlightDTO dto) {
       try {
           Flight flight = new Flight();
           if (dto.getId() == null) {
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy"); //Segun google, cada aerolinea genera sus codigos como les da la gana
               flight.setFlightNumber("SB" +dto.getDepartureDate().format(formatter));
           } else {
               flight.setFlightNumber(dto.getFlightNumber());
           }

           Airport departure = airportRepository.findById(dto.getDepartureId())
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Airport not found"));
           Airport destination = airportRepository.findById(dto.getDestinationId())
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Airport not found"));
           Plane plane = planeRepository.findById(dto.getModelId())
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plane model not found"));

           flight.setDepartureAirport(departure);
           flight.setDestinationAirport(destination);
           flight.setDepartureDate(dto.getDepartureDate());

           double distance = Util.getDistanceFromLatLonInKm(
                   departure.getLatitude(), departure.getLongitude(),
                   destination.getLatitude(), destination.getLongitude()
           );
           flight.setDuration(distance / 750.0);
           flight.setPlane(plane);

           return flight;

       } catch (ResponseStatusException ex) {
           throw ex;
       } catch (Exception ex) {
           throw new RuntimeException("Failed to convert FlightDTO to Flight entity: " + ex.getMessage(), ex);
       }
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
