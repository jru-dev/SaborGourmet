package com.example.saborgourmet.service;

import com.example.saborgourmet.model.Cliente;
import com.example.saborgourmet.model.Mesa;
import com.example.saborgourmet.repository.ClienteRepository;
import com.example.saborgourmet.repository.MesaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ClienteRepository clienteRepository;

    public MesaService(MesaRepository mesaRepository, ClienteRepository clienteRepository) {
        this.mesaRepository = mesaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    public List<Mesa> listarDisponibles() {
        return mesaRepository.findByEstado("disponible");
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

    @Transactional
    public Mesa asignarMesa(Long idMesa, Long idCliente) {
        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        if (!"disponible".equals(mesa.getEstado())) {
            throw new IllegalStateException("La mesa no está disponible");
        }

        mesa.setEstado("ocupada");
        mesa.setClienteAsignado(cliente);
        mesa.setHoraAsignacion(LocalDateTime.now());

        return mesaRepository.save(mesa);
    }

    @Transactional
    public Mesa liberarMesa(Long idMesa) {
        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        mesa.setEstado("disponible");
        mesa.setClienteAsignado(null);
        mesa.setHoraAsignacion(null);

        return mesaRepository.save(mesa);
    }
}