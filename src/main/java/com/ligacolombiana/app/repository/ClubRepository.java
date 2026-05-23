package com.ligacolombiana.app.repository;

import com.ligacolombiana.app.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
}