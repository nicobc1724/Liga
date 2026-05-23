package com.ligacolombiana.app.repository;

import com.ligacolombiana.app.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
}