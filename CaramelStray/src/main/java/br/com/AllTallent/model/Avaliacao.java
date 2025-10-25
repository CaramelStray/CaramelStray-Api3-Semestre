package br.com.AllTallent.model;

// --- Imports Essenciais ---
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// Removido ToString para evitar recursão com relacionamentos bidirecionais

import java.time.LocalDate;
import java.util.Set; // Import para Set



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_avaliacao") // Mapeia para a tabela correta
public class Avaliacao { // Mantendo o nome Avaliacao2 por enquanto

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
    private LocalDate dataPrazo;

    // --- Relacionamentos Reintroduzidos ---

    // Muitas Avaliacoes podem ter muitas Perguntas
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
      name = "tb_cad_avaliacao_pergunta",
      joinColumns = @JoinColumn(name = "codigo_avaliacao"),
      inverseJoinColumns = @JoinColumn(name = "codigo_pergunta")
    )
    private Set<Pergunta> perguntas;

    // Uma Avaliacao tem muitas Instâncias (AvaliacaoFuncionario)
    @OneToMany(
        mappedBy = "avaliacao", // Campo 'avaliacao' na classe AvaliacaoFuncionario
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<AvaliacaoFuncionario> instanciasAvaliacao; // Ligação com a outra entidade

    // --- Métodos ---
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDate.now();
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "Rascunho";
        }
    }

    // Opcional: Métodos para adicionar/remover instâncias e perguntas de forma segura (gerenciar bidirecionalidade)
    public void addInstancia(AvaliacaoFuncionario instancia) {
        if (instanciasAvaliacao == null) {
            instanciasAvaliacao = new java.util.HashSet<>();
        }
        instanciasAvaliacao.add(instancia);
        instancia.setAvaliacao(this); // Mantém a consistência bidirecional
    }

    public void removeInstancia(AvaliacaoFuncionario instancia) {
        if (instanciasAvaliacao != null) {
            instanciasAvaliacao.remove(instancia);
            instancia.setAvaliacao(null); // Mantém a consistência bidirecional
        }
    }
     public void addPergunta(Pergunta pergunta) {
         if (perguntas == null) {
             perguntas = new java.util.HashSet<>();
         }
         perguntas.add(pergunta);
         // Para @ManyToMany, o lado "dono" (onde não tem mappedBy) gerencia a tabela de junção.
         // Não precisamos setar o lado inverso aqui explicitamente se Pergunta não tiver Set<Avaliacao2>
     }

     public void removePergunta(Pergunta pergunta) {
         if (perguntas != null) {
             perguntas.remove(pergunta);
         }
     }
}