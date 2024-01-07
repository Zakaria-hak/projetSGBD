package com.example.projetgestiondereservations.repository;

import com.example.projetgestiondereservations.entity.Reservation;
import com.example.projetgestiondereservations.entity.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    @Query("SELECT e from Reservation e where e.terrain.numTerrain =?1")
    public List<Reservation> chercherParNumTerrain(int numTerrain);

    @Query("select e from Reservation e where e.date=?1")
    public List<Reservation>findByDate(LocalDate date);

    List<Reservation> findByTerrainAndDate(Terrain terrain, LocalDate date);
}
