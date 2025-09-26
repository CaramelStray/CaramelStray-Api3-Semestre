package br.com.AllTallent.model;

import jakarta.persistence.*; // Import simplificado
import lombok.*;             // Import simplificado

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo") // Padrão recomendado
@Entity
@Table(name = "tb_cad_area")
public class Area {

    // --- CORREÇÃO AQUI ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    private String nome;
    private String descricao;
}