package org.example.dam.repository;

import org.example.dam.model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneRepository  extends JpaRepository<Plane, Long> {
}
