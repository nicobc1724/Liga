package com.ligacolombiana.app.repository;

import com.ligacolombiana.app.entity.Competicion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompeticionRepository extends JpaRepository<Competicion, Long> {
}