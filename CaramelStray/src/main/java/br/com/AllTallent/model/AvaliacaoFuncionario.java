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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set; 


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_cad_funcionario_avalicacao") 
public class AvaliacaoFuncionario { 

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cad_funcionario_avalicacao_codigo_seq")
    @SequenceGenerator(name = "tb_cad_funcionario_avalicacao_codigo_seq", sequenceName = "tb_cad_funcionario_avalicacao_codigo_seq", allocationSize = 1)
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_funcionario_avalidado", nullable = false)
    private Funcionario funcionario; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_avalicacao", nullable = false) 
    private Avaliacao avaliacao; 

    @Column(name = "comentario_colaborador", columnDefinition = "TEXT")
    private String comentarioColaborador;

    @Column(name = "comentario_supervisao", columnDefinition = "TEXT")
    private String comentarioSupervisao;

    @Column(name = "resultado_status", length = 30)
    private String resultadoStatus;

    @Column(name = "nota")
    private Integer nota;


    @OneToMany(
        mappedBy = "avaliacaoFuncionario",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<RespostaColaborador> respostas;

    public AvaliacaoFuncionario(Funcionario funcionario, Avaliacao avaliacao) {
        this.funcionario = funcionario;
        this.avaliacao = avaliacao;
        this.resultadoStatus = "PENDENTE";
    }

     public void addResposta(RespostaColaborador resposta) {
         if (respostas == null) {
             respostas = new java.util.HashSet<>();
         }
         respostas.add(resposta);
         resposta.setAvaliacaoFuncionario(this);
     }

     public void removeResposta(RespostaColaborador resposta) {
         if (respostas != null) {
             respostas.remove(resposta);
             resposta.setAvaliacaoFuncionario(null);
         }
     }
}