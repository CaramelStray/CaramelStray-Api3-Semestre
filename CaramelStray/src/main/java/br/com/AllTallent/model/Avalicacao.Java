package br.com.AllTallent.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_cad_avaliacao") // A tabela que você criou no passo A
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    private String titulo;
    private String status; 
    
    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @ManyToMany
    @JoinTable(
      name = "tb_cad_funcionario_avalicacao", // Bate com o dump
      joinColumns = @JoinColumn(name = "codigo_avalicacao"),
      inverseJoinColumns = @JoinColumn(name = "codigo_funcionario_avalidado")
    )
    private Set<Funcionario> funcionariosAvaliados;

    @ManyToMany
    @JoinTable(
      name = "tb_cad_avaliacao_pergunta", // Bate com o dump
      joinColumns = @JoinColumn(name = "codigo_avaliacao"),
      inverseJoinColumns = @JoinColumn(name = "codigo_pergunta")
    )
    private Set<Pergunta> perguntas;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDate.now();
        if (status == null) {
            status = "Rascunho";
        }
    }
}