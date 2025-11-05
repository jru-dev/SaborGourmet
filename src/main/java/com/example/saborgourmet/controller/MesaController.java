package com.example.saborgourmet.controller;

import com.example.saborgourmet.model.Mesa;
import com.example.saborgourmet.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mesas", mesaService.listarTodas());
        return "mesas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("mesa", new Mesa());
        return "mesas/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Mesa mesa, BindingResult result) {
        if (result.hasErrors()) {
            return "mesas/nuevo";
        }
        mesa.setEstado("disponible");
        mesaService.guardar(mesa);
        return "redirect:/mesas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Mesa mesa = mesaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("mesa", mesa);
        return "mesas/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute Mesa mesa, BindingResult result) {
        if (result.hasErrors()) {
            return "mesas/editar";
        }
        mesa.setIdMesa(id);
        mesaService.guardar(mesa);
        return "redirect:/mesas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        mesaService.eliminar(id);
        return "redirect:/mesas";
    }
}