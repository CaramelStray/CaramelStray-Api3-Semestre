package br.com.AllTallent.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "tb_cad_funcionario_avaliacao")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class AvaliacaoFuncionario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo; // BIGINT é mapeado para Long no Java


    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento Muitos para Um (muitos resultados para uma Avaliacao)
    @JoinColumn(name = "codigo_avaliacao", referencedColumnName = "codigo")
    @ToString.Exclude // Alternativa a colocar no @ToString da classe, garante que não chame o toString() da Avaliacao
    private Avaliacao2 avaliacao;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_funcionario_avalidado", referencedColumnName = "codigo")
    private Funcionario funcionario;


    @Column(name = "comentario_colaborador", columnDefinition = "TEXT")
    private String comentarioColaborador;


    @Column(name = "comentario_supervisao", columnDefinition = "TEXT")
    private String comentarioSupervisao;


    @Column(name = "resultado_status", length = 30)
    private String resultadoStatus;


    @Column(name = "nota")
    private Integer nota;
}

