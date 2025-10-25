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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
// --- Fim dos Imports ---

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Removido @EqualsAndHashCode(of = "codigo") para evitar problemas com HashSet antes de salvar
@Entity
@Table(name = "tb_cad_funcionario_avalicacao")
public class FuncionarioAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cad_funcionario_avalicacao_codigo_seq")
    @SequenceGenerator(name = "tb_cad_funcionario_avalicacao_codigo_seq", sequenceName = "tb_cad_funcionario_avalicacao_codigo_seq", allocationSize = 1)
    private Long codigo; // bigint

    // Ligação com o Funcionário que está sendo avaliado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_funcionario_avalidado", nullable = false) // Não pode ser nulo
    private Funcionario funcionarioAvaliado;

    // Ligação com a Avaliacao que está sendo realizada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_avalicacao", nullable = false) // Não pode ser nulo
    private Avaliacao avaliacao;

    @Column(name = "comentario_colaborador", columnDefinition = "TEXT")
    private String comentarioColaborador;

    @Column(name = "comentario_supervisao", columnDefinition = "TEXT")
    private String comentarioSupervisao;

    @Column(name = "resultado_status", length = 30) // Definir tamanho se possível
    private String resultadoStatus; // Ex: PENDENTE, APROVADO, REPROVADO

    private Integer nota; // Nota final (se aplicável)

    // --- Relacionamentos ---

    // Uma FuncionarioAvaliacao (instância) tem muitas Respostas
    @OneToMany(
        mappedBy = "funcionarioAvaliacao", // Campo na classe RespostaColaborador
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<RespostaColaborador> respostas; // As respostas dadas nesta instância

    // --- Construtor Auxiliar ---
    public FuncionarioAvaliacao(Funcionario funcionario, Avaliacao avaliacao) {
        this.funcionarioAvaliado = funcionario;
        this.avaliacao = avaliacao;
        this.resultadoStatus = "PENDENTE"; // Status inicial padrão
    }

    // Nota: Se remover @EqualsAndHashCode(of = "codigo"),
    // o Lombok pode gerar um padrão. Se precisar de lógica customizada
    // para equals/hashCode, implemente manualmente. Por enquanto, remover é mais seguro.
}