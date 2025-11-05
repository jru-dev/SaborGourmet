package com.example.saborgourmet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mesa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesa;

    @NotNull(message = "El número de mesa es obligatorio")
    private Integer numero;

    private Integer capacidad;

    @Column(length = 20)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente clienteAsignado;

    private LocalDateTime horaAsignacion;
}