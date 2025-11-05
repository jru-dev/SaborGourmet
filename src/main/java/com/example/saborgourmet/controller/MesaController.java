package com.example.saborgourmet.controller;

import com.example.saborgourmet.model.Mesa;
import com.example.saborgourmet.service.ClienteService;
import com.example.saborgourmet.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    private final MesaService mesaService;
    private final ClienteService clienteService;

    public MesaController(MesaService mesaService, ClienteService clienteService) {
        this.mesaService = mesaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mesas", mesaService.listarTodas());
        return "mesas/listar";
    }

    @GetMapping("/disponibles")
    public String listarDisponibles(Model model) {
        model.addAttribute("mesas", mesaService.listarDisponibles());
        return "mesas/disponibles";
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

    // RF2: Asignar mesa a un cliente
    @GetMapping("/asignar/{id}")
    public String mostrarFormularioAsignar(@PathVariable Long id, Model model) {
        Mesa mesa = mesaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));
        model.addAttribute("mesa", mesa);
        model.addAttribute("clientes", clienteService.listarTodos());
        return "mesas/asignar";
    }

    @PostMapping("/asignar/{idMesa}")
    public String asignarMesa(@PathVariable Long idMesa,
                              @RequestParam Long idCliente,
                              RedirectAttributes redirectAttributes) {
        try {
            mesaService.asignarMesa(idMesa, idCliente);
            redirectAttributes.addFlashAttribute("mensaje", "Mesa asignada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mesas";
    }

    // RF2: Liberar mesa
    @GetMapping("/liberar/{id}")
    public String liberarMesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mesaService.liberarMesa(id);
            redirectAttributes.addFlashAttribute("mensaje", "Mesa liberada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mesas";
    }
}