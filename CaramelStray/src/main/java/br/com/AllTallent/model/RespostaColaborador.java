package br.com.AllTallent.model; // Ou o pacote de modelos

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
    private Long codigo; // bigint

    // Ligação com a instância da avaliação do funcionário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_funcionario_avaliacao", nullable = false)
    private FuncionarioAvaliacao funcionarioAvaliacao;

    // Ligação com a pergunta que está sendo respondida
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pergunta", nullable = false)
    private Pergunta pergunta;

    @Column(name = "resposta_texto", columnDefinition = "TEXT")
    private String respostaTexto; // Usado para perguntas tipo 'texto' ou 'escala/nota'

    // Ligação com a opção selecionada (apenas para múltipla escolha)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pergunta_opcao_selecionada") // Permite nulo
    private PerguntaOpcao opcaoSelecionada;
}