package br.com.AllTallent.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_avaliacao")
public class Avaliacao2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;
    
    @Column(name = "titulo", length = 255, nullable = false)
    private String titulo;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @Column(name = "data_prazo")
    private LocalDate dataPrazo;



}
