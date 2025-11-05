package com.example.saborgourmet.service;

import com.example.saborgourmet.model.Mesa;
import com.example.saborgourmet.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    public Mesa guardar(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public Optional<Mesa> obtenerPorId(Long id) {
        return mesaRepository.findById(id);
    }

    public void eliminar(Long id) {
        mesaRepository.deleteById(id);
    }
}