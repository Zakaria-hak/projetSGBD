package com.example.projetgestiondereservations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table
public class Reservation {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //l'utilisation de LocalDate au lieu de Date , pour faire fonctionner la validation @NottNull et @FutureOrPresent
    @Column
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull
    @Column
    private LocalTime heureDeb;
    @Column
    private LocalTime heureFin;


    @ManyToOne
    @NotNull(message = "ne doit pas être null")
    @JoinColumn(name = "membre_id")
    private Membre membre;


    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "ne doit pas être null")
    @JoinColumn(name = "terrain_id")
    private Terrain terrain;

    public Reservation() {
    }

    public Reservation(Integer id, LocalDate date, LocalTime heureDeb, LocalTime heureFin, Membre membre, Terrain terrain) {
        this.id = id;
        this.date = date;
        this.heureDeb = heureDeb;
        this.heureFin = heureFin;
        this.membre = membre;
        this.terrain = terrain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDeb() {
        return heureDeb;
    }

    public void setHeureDeb(LocalTime temps_deb) {
        this.heureDeb = temps_deb;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime temps_fin) {
        this.heureFin = temps_fin;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
}
