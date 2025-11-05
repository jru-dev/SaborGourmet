package com.example.saborgourmet.repository;

import com.example.saborgourmet.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    Mesa findByNumero(Integer numero);
}