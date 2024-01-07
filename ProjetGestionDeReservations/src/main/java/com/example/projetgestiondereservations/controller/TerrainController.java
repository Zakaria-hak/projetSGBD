package com.example.projetgestiondereservations.controller;

import com.example.projetgestiondereservations.entity.Reservation;
import com.example.projetgestiondereservations.entity.Terrain;
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

import java.util.List;
import java.util.Optional;

@Controller
public class TerrainController {

    @Autowired
    TerrainRepository terrainRepository;
    @Autowired
    ReservationRepository reservationRepository;

    @GetMapping("/terrainPage")
    public String showPageTerrain(Model model) {
        model.addAttribute("listTerrains", terrainRepository.findAll());
        return "terrain";
    }

    @GetMapping("/showAddTerrainPage")
    public String showPageAddTerrain(Model model) {
        model.addAttribute("terrain", new Terrain());
        return "save_terrain";
    }

    @PostMapping("/saveTerrain")
    public String saveTerrain(@ModelAttribute("terrain") @Valid Terrain terrain, Errors errors) {
        if (errors.hasErrors()) {
            return "save_terrain";
        }
        terrainRepository.save(terrain);
        return "redirect:/terrainPage";
    }

    @GetMapping("/showUpdateTerrainPage")
    public String showUpdateTerrainPage(Model model, @RequestParam(name = "id") Integer id) {
        Optional<Terrain> optional = terrainRepository.findById(id);
        Terrain terrain ;
        if (optional.isPresent()) {
            terrain = optional.get();
        } else {
            throw new RuntimeException("le terrain n'exsite pas");
        }
        model.addAttribute("terrain", terrain);
        return "update_terrain";
    }

    @PostMapping("/updateTerrain")
    public String updateTerrain(@ModelAttribute("terrain") @Valid Terrain terrain, Errors errors) {
        if (errors.hasErrors()) {
            return "update_terrain";
        }
        terrainRepository.save(terrain);
        return "redirect:/terrainPage";
    }

    @GetMapping("/deleteTerrain")
    public String deleteTerrain(@RequestParam(name = "id") Integer id) {
        List<Reservation> all = reservationRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getTerrain() != null && all.get(i).getTerrain().getId() == id) {
                throw new RuntimeException("le terrain ne peut pas être supprimé , des reservations sont liés à ce terrain");
            }
        }
        terrainRepository.deleteById(id);
        return "redirect:/terrainPage";
    }
}
