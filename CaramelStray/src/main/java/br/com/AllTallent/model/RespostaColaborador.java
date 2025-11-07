package br.com.AllTallent.model; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_resposta_colaborador")
public class RespostaColaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cad_resposta_colaborador_codigo_seq")
    @SequenceGenerator(name = "tb_cad_resposta_colaborador_codigo_seq", sequenceName = "tb_cad_resposta_colaborador_codigo_seq", allocationSize = 1)
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_funcionario_avaliacao", nullable = false)
    private AvaliacaoFuncionario avaliacaoFuncionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pergunta", nullable = false)
    private Pergunta pergunta;

    @Column(name = "resposta_texto", columnDefinition = "TEXT")
    private String respostaTexto; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pergunta_opcao_selecionada") 
    private PerguntaOpcao opcaoSelecionada;
}