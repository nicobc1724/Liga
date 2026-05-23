package com.ligacolombiana.app.repository;

import com.ligacolombiana.app.entity.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
}