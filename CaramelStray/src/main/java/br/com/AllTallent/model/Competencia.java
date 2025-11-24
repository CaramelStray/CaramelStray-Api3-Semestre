package br.com.AllTallent.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "nome")
@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_competencia")
public class Competencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    private String nome;

    private String categoria;

    @ManyToMany(mappedBy = "competencias")
    private Set<Funcionario> funcionarios;
}