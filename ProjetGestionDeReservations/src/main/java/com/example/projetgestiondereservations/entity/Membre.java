package com.example.projetgestiondereservations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Membres")
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom")
    @NotEmpty(message = "ne doit pas être vide")
    @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ]+$" ,message = "ne doit contenir que des lettres")
    private String nom;
    @NotEmpty(message = "ne doit pas être vide")
    @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ]+$" , message = "ne doit contenir que des lettres")
    @Column(name = "prénom")
    private String prenom;
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Past
    private LocalDate dateNaiss;
    @Column(name = "en_ordre_de_paiement")
    @NotNull
    private Boolean enOrdre;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "membre")
    private List<Reservation> reservations;

    public Membre() {
    }

    public Membre(Integer id, String nom, String prenom, LocalDate dateNaiss, Boolean enOrdre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaiss = dateNaiss;
        this.enOrdre = enOrdre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(LocalDate dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public Boolean getEnOrdre() {
        return enOrdre;
    }

    public void setEnOrdre(Boolean enOrdre) {
        this.enOrdre = enOrdre;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
