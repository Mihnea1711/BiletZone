package com.example.database.persistence.interfaces;

import com.example.database.models.HallMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHallMapRepository extends JpaRepository<HallMap, Long> {
}