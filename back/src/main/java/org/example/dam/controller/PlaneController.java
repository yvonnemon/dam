package org.example.dam.controller;

import org.example.dam.model.Plane;
import org.example.dam.service.PlaneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
public class PlaneController {

    private final PlaneService planeService;

    public PlaneController(PlaneService planeService) {
        this.planeService = planeService;
    }

    @PostMapping
    public ResponseEntity<Plane> createUser(@RequestBody Plane plane) {
        return ResponseEntity.ok(planeService.save(plane));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plane> getUserById(@PathVariable Long id) {
        return planeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/planes")
    public ResponseEntity<List<Plane>> getAllUsers() {
        return ResponseEntity.ok(planeService.findAll());
    }

    @PutMapping("/planes/{id}")
    public ResponseEntity<Plane> updateUser(@RequestBody Plane newPlane, @PathVariable Long id) {
        return ResponseEntity.ok(
                planeService.findById(id)
                        .map(plane -> {
                            plane.setName(newPlane.getName());
                            plane.setCapacity(newPlane.getCapacity());
                            plane.setModel(newPlane.getModel());
                            plane.setMaxRange(newPlane.getMaxRange());

                            // TODO If dateOfBirth is a LocalDate or Date, uncomment this:
                            // plane.setDateOfBirth(newUser.getDateOfBirth());
                            return planeService.save(plane);
                        })
                        .orElseGet(() -> {
                            newPlane.setId(id); // optional: set ID manually
                            return planeService.save(newPlane);
                        })
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (planeService.findById(id).isPresent()) {
            planeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
