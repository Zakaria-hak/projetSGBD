package com.example.projetgestiondereservations.repository;

import com.example.projetgestiondereservations.entity.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MembreRepository extends JpaRepository<Membre,Integer> {
}
