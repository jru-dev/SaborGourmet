package com.example.saborgourmet.repository;

import com.example.saborgourmet.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    Mesa findByNumero(Integer numero);

    List<Mesa> findByEstado(String estado);
}