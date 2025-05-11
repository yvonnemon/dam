package org.example.dam.dto;

import org.example.dam.model.BookingStatus;
import org.example.dam.model.Flight;
import org.example.dam.model.User;

import java.time.LocalDate;
import java.util.Date;

public class BookingDTO {

    private Long id;

    private String bookingNumber;

    private LocalDate datePurchase;

    private Double price;

    private BookingStatus status;

    private User user;

    private Flight flight;
    private Long flightId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return bookingNumber;
    }

    public void setNumber(String number) {
        this.bookingNumber = number;
    }

    public LocalDate getDatePurchase() {
        return datePurchase;
    }

    public void setDatePurchase(LocalDate datePurchase) {
        this.datePurchase = datePurchase;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
