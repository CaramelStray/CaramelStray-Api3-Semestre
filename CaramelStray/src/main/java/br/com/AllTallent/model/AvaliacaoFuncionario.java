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
// Removido EqualsAndHashCode para evitar problemas com Set antes do save
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// Removido ToString para evitar recursão

import java.util.Set; // Import para Set


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// @EqualsAndHashCode(of = "codigo") // Removido ou comentado
@Entity
@Table(name = "tb_cad_funcionario_avalicacao") // Nome correto da tabela no SQL (com 'c')
public class AvaliacaoFuncionario { // Mantendo o nome AvaliacaoFuncionario

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cad_funcionario_avalicacao_codigo_seq")
    @SequenceGenerator(name = "tb_cad_funcionario_avalicacao_codigo_seq", sequenceName = "tb_cad_funcionario_avalicacao_codigo_seq", allocationSize = 1)
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_funcionario_avalidado", nullable = false)
    private Funcionario funcionario; // Mudado de funcionarioAvaliado para funcionario (como no seu DTO)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_avalicacao", nullable = false) // Nome da coluna no banco
    private Avaliacao2 avaliacao; // Nome da classe Java correspondente

    @Column(name = "comentario_colaborador", columnDefinition = "TEXT")
    private String comentarioColaborador;

    @Column(name = "comentario_supervisao", columnDefinition = "TEXT")
    private String comentarioSupervisao;

    @Column(name = "resultado_status", length = 30)
    private String resultadoStatus;

    @Column(name = "nota")
    private Integer nota;


    @OneToMany(
        mappedBy = "avaliacaoFuncionario", // Campo na classe RespostaColaborador
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<RespostaColaborador> respostas;

    // --- Construtor ---
    // (Pode remover se @AllArgsConstructor e @NoArgsConstructor forem suficientes)
    public AvaliacaoFuncionario(Funcionario funcionario, Avaliacao2 avaliacao) {
        this.funcionario = funcionario;
        this.avaliacao = avaliacao;
        this.resultadoStatus = "PENDENTE";
    }

     // Opcional: Métodos para adicionar/remover respostas de forma segura
     public void addResposta(RespostaColaborador resposta) {
         if (respostas == null) {
             respostas = new java.util.HashSet<>();
         }
         respostas.add(resposta);
         resposta.setAvaliacaoFuncionario(this); // Mantém a consistência
     }

     public void removeResposta(RespostaColaborador resposta) {
         if (respostas != null) {
             respostas.remove(resposta);
             resposta.setAvaliacaoFuncionario(null); // Mantém a consistência
         }
     }
}