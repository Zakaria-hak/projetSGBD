package com.example.projetgestiondereservations.repository;

import com.example.projetgestiondereservations.entity.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerrainRepository extends JpaRepository<Terrain,Integer> {
}
