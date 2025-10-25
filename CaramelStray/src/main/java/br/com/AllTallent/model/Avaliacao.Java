/*
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode; // Adicionado para @EqualsAndHashCode

import java.time.LocalDate;
import java.util.Set;
// --- Fim dos Imports ---

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo") // Boa prática, usa apenas o ID para igualdade
@Entity
@Table(name = "tb_cad_avaliacao")
public class Avaliacao {

    @Id
    // Usar a SEQUENCE definida no dump SQL
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cad_avaliacao_codigo_seq")
    @SequenceGenerator(name = "tb_cad_avaliacao_codigo_seq", sequenceName = "tb_cad_avaliacao_codigo_seq", allocationSize = 1)
    private Integer codigo; // Chave primária (integer no SQL)

    @Column(nullable = false, length = 255) // Adicionado nullable = false
    private String titulo;

    @Column(length = 50)
    private String status; // Ex: Rascunho, Publicada, Concluída

    @Column(name = "data_criacao", updatable = false) // updatable = false: não atualiza após criação
    private LocalDate dataCriacao;

    @Column(name = "data_prazo")
    private LocalDate dataPrazo; // Prazo para conclusão da avaliação

    // --- Relacionamentos ---

    // Muitas Avaliacoes podem ter muitas Perguntas (através da tabela de junção)
    @ManyToMany(fetch = FetchType.LAZY) // LAZY é geralmente melhor para performance
    @JoinTable(
      name = "tb_cad_avaliacao_pergunta", // Nome da tabela de junção
      joinColumns = @JoinColumn(name = "codigo_avaliacao"), // Coluna FK nesta entidade (Avaliacao)
      inverseJoinColumns = @JoinColumn(name = "codigo_pergunta") // Coluna FK na outra entidade (Pergunta)
    )
    private Set<Pergunta> perguntas; // As perguntas que compõem esta avaliação

    // Uma Avaliacao pode ter várias 'instâncias' (uma para cada funcionário fazendo ela)
    // Ligação com a entidade que representa tb_cad_funcionario_avalicacao
    @OneToMany(
        mappedBy = "avaliacao", // Campo na classe FuncionarioAvaliacao que referencia Avaliacao
        cascade = CascadeType.ALL, // Operações (salvar/deletar) em Avaliacao afetam FuncionarioAvaliacao
        orphanRemoval = true, // Remove instâncias órfãs (se remover da lista, deleta do banco)
        fetch = FetchType.LAZY // Carrega as instâncias apenas quando necessário
    )
    private Set<FuncionarioAvaliacao> instanciasAvaliacao; // As execuções desta avaliação pelos funcionários

    // --- Métodos de Ciclo de Vida JPA ---

    @PrePersist // Método chamado antes de salvar uma nova Avaliacao
    protected void onCreate() {
        this.dataCriacao = LocalDate.now();
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "Rascunho"; // Define um status padrão
        }
    }
}*/