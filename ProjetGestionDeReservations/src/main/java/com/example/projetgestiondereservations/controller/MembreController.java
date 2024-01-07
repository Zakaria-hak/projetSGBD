package com.example.projetgestiondereservations.controller;

import com.example.projetgestiondereservations.entity.Membre;
import com.example.projetgestiondereservations.entity.Reservation;
import com.example.projetgestiondereservations.repository.MembreRepository;
import com.example.projetgestiondereservations.repository.ReservationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MembreController {

    @Autowired
    MembreRepository membreRepository;
    @Autowired
    ReservationRepository reservationRepository;

    @GetMapping("/membrePage")
    public String showPageMembre(Model model) {
        model.addAttribute("listMembres", membreRepository.findAll());
        return "membre";
    }

    @GetMapping("/showAddMembrePage")
    public String showAddMembrePage(Model model) {
        model.addAttribute("membre", new Membre());
        return "save_membre";
    }

    @PostMapping("/saveMembre")
    public String saveMembre(Model model, @Valid Membre membre , Errors errors) {
        if (errors.hasErrors()){
            return "save_membre";
        }
        model.addAttribute("membre", membre);
        membreRepository.save(membre);
        return "redirect:/membrePage";
    }

    @GetMapping("/showUpdateMembrePage")
    public String showUpdateMembrePage(Model model, @RequestParam(name = "id") Integer id) {
        Optional<Membre> optional = membreRepository.findById(id);

        Membre membre = null;
        if (optional.isPresent()) {
            membre = optional.get();
        } else {
            throw new RuntimeException(" l'id fourni " + id + " n'est pas trouvé");
        }
        model.addAttribute("membre", membre);
        return "update_membre";
    }

    @PostMapping("/updateMembre")
    public String updateMembre(@ModelAttribute("membre") @Valid Membre membre ,Errors errors ){
        if (errors.hasErrors()){
            return "update_membre";
        }
        membreRepository.save(membre);
        return "redirect:/membrePage";
    }

    @GetMapping("/showDeleteMembrePage")
        public String showDeletePage(Model model ,@RequestParam(name = "id") Integer id){
        Optional<Membre> optional = membreRepository.findById(id);
            Membre membre ;
        if (optional.isPresent()){
                membre = optional.get();
            } else {
            throw new RuntimeException(" l'id fourni " + id + " n'est pas trouvé");
        }
        model.addAttribute("membre",membre);
        return "delete_membre";
    }
    @PostMapping("/deleteMembre")
    public String deleteMembre(@RequestParam(name = "id" )Integer id){
        List<Reservation> allReservation = reservationRepository.findAll();
        for (Reservation reservation : allReservation){
            if (reservation.getMembre() != null && reservation.getMembre().getId().equals(id)){
                throw new RuntimeException("le membre ne peut pas être supprimé , des reservations sont liées à ce membre");
            }
        }
        membreRepository.deleteById(id);
        return "redirect:/membrePage";
    }
}
