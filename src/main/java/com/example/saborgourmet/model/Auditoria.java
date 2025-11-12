package com.example.saborgourmet.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tabla")
    private String tabla;

    @Column(name = "id_registro")
    private Long idRegistro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "accion")
    private String accion;

    public Auditoria(String tabla, Long idRegistro, Date fecha, String usuario, String accion) {
        this.tabla = tabla;
        this.idRegistro = idRegistro;
        this.fecha = fecha;
        this.usuario = usuario;
        this.accion = accion;
    }
}