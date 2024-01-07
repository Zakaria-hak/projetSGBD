package com.example.projetgestiondereservations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "terrains", uniqueConstraints = @UniqueConstraint(columnNames = {"numTerrain"}))
public class Terrain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "ça ne doit pas être null ")
    @Column(name = "numTerrain", nullable = false)
    private Integer numTerrain;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "terrain")
    private List<Reservation> reservations = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumTerrain() {
        return numTerrain;
    }

    public void setNumTerrain(Integer numTerrain) {
        this.numTerrain = numTerrain;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Terrain() {
    }

    public Terrain(Integer numTerrain, List<Reservation> reservations) {
        this.numTerrain = numTerrain;
        this.reservations = reservations;
    }
}
