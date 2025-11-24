package br.com.AllTallent.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cad_avaliacao_codigo_seq")
    @SequenceGenerator(name = "tb_cad_avaliacao_codigo_seq", sequenceName = "tb_cad_avaliacao_codigo_seq", allocationSize = 1)
    private Integer codigo;

    @Column(name = "titulo", length = 255, nullable = false)
    private String titulo;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "data_criacao", updatable = false)
    private LocalDate dataCriacao;

    @Column(name = "data_prazo")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPrazo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_criador")
    private Funcionario criador;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
      name = "tb_cad_avaliacao_pergunta",
      joinColumns = @JoinColumn(name = "codigo_avaliacao"),
      inverseJoinColumns = @JoinColumn(name = "codigo_pergunta")
    )
    private Set<Pergunta> perguntas;

    
    @OneToMany(
        mappedBy = "avaliacao", 
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<AvaliacaoFuncionario> instanciasAvaliacao;

    // --- Métodos ---
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDate.now();
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "Rascunho";
        }
    }

    public void addInstancia(AvaliacaoFuncionario instancia) {
        if (instanciasAvaliacao == null) {
            instanciasAvaliacao = new java.util.HashSet<>();
        }
        instanciasAvaliacao.add(instancia);
        instancia.setAvaliacao(this); 
    }

    public void removeInstancia(AvaliacaoFuncionario instancia) {
        if (instanciasAvaliacao != null) {
            instanciasAvaliacao.remove(instancia);
            instancia.setAvaliacao(null); 
        }
    }
     public void addPergunta(Pergunta pergunta) {
         if (perguntas == null) {
             perguntas = new java.util.HashSet<>();
         }
         perguntas.add(pergunta);
     }

     public void removePergunta(Pergunta pergunta) {
         if (perguntas != null) {
             perguntas.remove(pergunta);
         }
     }
}