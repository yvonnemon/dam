package org.example.dam.model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class Flight {

    @Id
    @Column()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private Integer seatsReserved = 0;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "destination_airport_id")
    private Airport destinationAirport;

    @Column(nullable = false)
    private LocalDate departureDate;

    @Column(nullable = false)
    private Double duration;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

/*
*
*
*   GETTERS Y SETTERS
*
*
* */
    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Integer getSeatsReserved() {
        return seatsReserved;
    }

    public void setSeatsReserved(Integer seatsReserved) {
        this.seatsReserved = seatsReserved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departure) {
        this.departureDate = departure;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
    }
}
