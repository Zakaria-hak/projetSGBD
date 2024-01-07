package com.example.projetgestiondereservations.controller;

import com.example.projetgestiondereservations.entity.Membre;
import com.example.projetgestiondereservations.entity.Reservation;
import com.example.projetgestiondereservations.repository.MembreRepository;
import com.example.projetgestiondereservations.repository.ReservationRepository;
import com.example.projetgestiondereservations.repository.TerrainRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Controller
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    MembreRepository membreRepository;
    @Autowired
    TerrainRepository terrainRepository;

    private final LocalTime HEURE_DEB_MIN = LocalTime.of(8, 59);
    private final LocalTime HEURE_DEB_MAX = LocalTime.of(20, 1);

    @GetMapping("/reservationPage")
    public String showReservationPage(Model model) {
        model.addAttribute("listReservations", reservationRepository.findAll());
        model.addAttribute("listMembres", membreRepository.findAll());
        model.addAttribute("listeTerrain", terrainRepository.findAll());
        return "reservation";
    }

    @GetMapping("/showAddReservationPage")
    public String showAddReservationPage(Model model) {
        model.addAttribute("listMembres", membreRepository.findAll());
        model.addAttribute("listTerrain", terrainRepository.findAll());
        model.addAttribute("reservation", new Reservation());
        return "save_reservation";
    }

    @PostMapping("/saveReservation")
    public String saveReservation(@ModelAttribute("reservation") @Valid Reservation reservation, Errors errors, @RequestParam(name = "inputHeureFin") String heuFin, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("listMembres", membreRepository.findAll());
            model.addAttribute("listTerrain", terrainRepository.findAll());
            return "save_reservation";
        }
        // vérifier la durée
        if (!veriferDuree(reservation, heuFin, model)) return "save_reservation";
        // Vérifier la validité du membre
        if (!isMembreEnOrdre(reservation.getMembre().getId(), model)) return "save_reservation";
        //ajouter duree
        ajouterDuree(reservation, heuFin);

        if (chevaucheReservationExistante(reservation)) {
            model.addAttribute("messageErrorExiste", "La réservation chevauche une réservation existante.");
            model.addAttribute("listMembres", membreRepository.findAll());
            model.addAttribute("listTerrain", terrainRepository.findAll());
            return "save_reservation";
        }

        // Sauvegarder la réservation
        reservationRepository.save(reservation);
        return "redirect:/reservationPage";
    }


    @GetMapping("/showUpdateReservationPage")
    public String showUpadateReservationPage(Model model, @RequestParam(name = "id") Integer id) {
        Optional<Reservation> optional = reservationRepository.findById(id);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            model.addAttribute("reservation", reservation);
            model.addAttribute("duree", reservation.getHeureFin());
            Optional<Membre> optionalMembre = membreRepository.findById(reservation.getMembre().getId());
            Membre membre;
            if (optionalMembre.isPresent()) {
                membre = optionalMembre.get();
                model.addAttribute("membre", membre);
            } else {
                throw new RuntimeException("le membre n'exsite pas");
            }
            model.addAttribute("reservation", reservation);
            model.addAttribute("listMembres", membreRepository.findAll());
            model.addAttribute("listTerrain", terrainRepository.findAll());
            return "update_reservation";
        } else {
            throw new RuntimeException("la reservation n'exsite pas " + " " + id);
        }
    }

    @PostMapping("/updateReservation")
    public String updateReservation(@RequestParam(name = "inputHeureFin") String heuFin, @ModelAttribute("reservation") @Valid Reservation reservation, Errors errors, Model model) {
        // Vérifier les erreurs
        if (errors.hasErrors()) {
            model.addAttribute("listMembres", membreRepository.findAll());
            model.addAttribute("listTerrain", terrainRepository.findAll());
            return "update_reservation";
        }

        // vérifier la durée
        if (!veriferDuree(reservation, heuFin, model)) return "update_reservation";
        // Vérifier la validité du membre
        if (!isMembreEnOrdre(reservation.getMembre().getId(), model)) return "update_reservation";

        //ajouter duree
        ajouterDuree(reservation, heuFin);

        if (chevaucheReservationExistante(reservation)) {
            model.addAttribute("messageErrorExiste", "La réservation chevauche une réservation existante.");
            model.addAttribute("listMembres", membreRepository.findAll());
            model.addAttribute("listTerrain", terrainRepository.findAll());
            return "update_reservation";
        }

        // Sauvegarder la réservation
        reservationRepository.save(reservation);
        return "redirect:/reservationPage";
    }


    @GetMapping("deleteReservation")
    public String deleteReservation(@RequestParam(name = "id") Integer id) {
        reservationRepository.deleteById(id);
        return "redirect:/reservationPage";
    }


    @GetMapping("/chercherParNumTerrain")
    public String showReservationsParNumTerrain(Model model, @RequestParam(name = "numTerrain", defaultValue = "0") int numTerrain) {
        if (numTerrain != 0) {
            model.addAttribute("listReservations", reservationRepository.chercherParNumTerrain(numTerrain));
        } else {
            model.addAttribute("listReservations", reservationRepository.findAll());
        }
        model.addAttribute("listMembres", membreRepository.findAll());
        model.addAttribute("listeTerrain", terrainRepository.findAll());
        model.addAttribute("cherhcerParNumTerrain", numTerrain);
        return "reservation";
    }


    @GetMapping("/chercherParDate")
    public String showReservationsParDateDeReservation(Model model, @RequestParam(name = "date", defaultValue = "") String dateString) throws ParseException {

        if (!dateString.isEmpty()) {
            System.out.println("date");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, formatter);
            model.addAttribute("listReservations", reservationRepository.findByDate(date));
            return "reservation";
        } else {
            model.addAttribute("listReservations", reservationRepository.findAll());

        }
        model.addAttribute("chercherParDate", dateString);
        model.addAttribute("listMembres", membreRepository.findAll());
        model.addAttribute("listeTerrain", terrainRepository.findAll());
        return "reservation";
    }

    private boolean isMembreEnOrdre(Integer membreId, Model model) {
        Membre membre = null;
        Optional<Membre> optional = membreRepository.findById(membreId);

        if (optional.isPresent()) {
            membre = optional.get();
            if (!membre.getEnOrdre()) {
                model.addAttribute("listMembres", membreRepository.findAll());
                model.addAttribute("listTerrain", terrainRepository.findAll());
                model.addAttribute("message", "Vous n'êtes pas en ordre de paiement");
                return false;
            }
        } else {
            throw new RuntimeException("Le membre n'existe pas : " + membreId);
        }
        return true;
    }


    /*verification de la durée*/
    private boolean veriferDuree(Reservation reservation, String heuFin, Model model) {
        if (heuFin.isEmpty() || !heuFin.matches("^1:00|2:00$")) {
            System.out.println(reservation.getHeureFin());
            model.addAttribute("listMembres", membreRepository.findAll());
            model.addAttribute("listTerrain", terrainRepository.findAll());
            model.addAttribute("messageDuree", "la dureé ne peut pas être null");
            return false;
        }
        return true;
    }

    /*getion de durée de la reservation */
    private void ajouterDuree(Reservation reservation, String dureeString) {
        /* Vérification de l'heure de début (doit être comprise entre 9h et 20h) */
        if (!heureDebEstValide(reservation.getHeureDeb())) {
            throw new RuntimeException("L'heure de début doit être comprise entre 09:00 et 20:00");
        }

        /* Conversion de la durée de type chaîne en heures de type int */
        int heureDebut = reservation.getHeureDeb().getHour();
        int minuteDebut = reservation.getHeureDeb().getMinute();

        int dureeEnHeures = Integer.parseInt(dureeString.substring(0, 1));

        // Ajout de la durée à la variable heureFin
        LocalTime heureFin = LocalTime.of(heureDebut + dureeEnHeures, minuteDebut);
        reservation.setHeureFin(heureFin);
    }

    private boolean heureDebEstValide(LocalTime heureDebut) {
        return heureDebut.isAfter(HEURE_DEB_MIN) && heureDebut.isBefore(HEURE_DEB_MAX);
    }

    private boolean chevaucheReservationExistante(Reservation nouvelleReservation) {
        List<Reservation> reservationsExistantes = reservationRepository.findByTerrainAndDate(nouvelleReservation.getTerrain(), nouvelleReservation.getDate());

        if (!reservationsExistantes.isEmpty()) {
            for (Reservation reservationExistante : reservationsExistantes) {
                if (!(reservationExistante.getId().equals(nouvelleReservation.getId()) ||
                        (reservationExistante.getHeureDeb().isAfter(nouvelleReservation.getHeureFin()) ||
                                reservationExistante.getHeureFin().isBefore(nouvelleReservation.getHeureDeb()) ||
                                reservationExistante.getHeureFin().equals(nouvelleReservation.getHeureDeb())))) {
                    System.out.println("Chevauchement détecté:");
                    return true;
                }
            }
        }
        return false;
    }

}