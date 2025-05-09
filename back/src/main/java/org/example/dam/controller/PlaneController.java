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
    public ResponseEntity<Plane> createPlane(@RequestBody Plane plane) {
        return ResponseEntity.ok(planeService.save(plane));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plane> getPlaneById(@PathVariable Long id) {
        return planeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/")
    public ResponseEntity<List<Plane>> getAllPlanes() {
        return ResponseEntity.ok(planeService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plane> updatePlane(@RequestBody Plane newPlane, @PathVariable Long id) {
        return ResponseEntity.ok(
                planeService.findById(id)
                        .map(plane -> {
                            plane.setName(newPlane.getName());
                            plane.setCapacity(newPlane.getCapacity());
                            plane.setModel(newPlane.getModel());
                            plane.setMaxRange(newPlane.getMaxRange());

                            return planeService.save(plane);
                        })
                        .orElseGet(() -> {
                            newPlane.setId(id); // optional: set ID manually
                            return planeService.save(newPlane);
                        })
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlane(@PathVariable Long id) {
        if (planeService.findById(id).isPresent()) {
            planeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
