package org.example.dam.service;

import org.example.dam.model.Plane;
import org.example.dam.model.Plane;
import org.example.dam.repository.PlaneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaneService {

    private final PlaneRepository planeRepository;

    public PlaneService(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    public List<Plane> findAll() {
        return planeRepository.findAll();
    }

    public Optional<Plane> findById(Long id) {
        return planeRepository.findById(id);
    }

    //TODO update y create?
    public Plane save(Plane user) {
        return planeRepository.save(user);
    }

    public void deleteById(Long id) {
        planeRepository.deleteById(id);
    }
}
