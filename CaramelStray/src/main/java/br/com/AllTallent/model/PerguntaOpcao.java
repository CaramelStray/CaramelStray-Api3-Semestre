package br.com.AllTallent.model; // Ou o pacote onde seus modelos estão

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_pergunta_opcao")
public class PerguntaOpcao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cad_pergunta_opcao_codigo_seq")
    @SequenceGenerator(name = "tb_cad_pergunta_opcao_codigo_seq", sequenceName = "tb_cad_pergunta_opcao_codigo_seq", allocationSize = 1)
    private Long codigo; // bigint

    // Relacionamento inverso: Muitas Opcoes pertencem a UMA Pergunta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pergunta", nullable = false) // FK para tb_cad_pergunta
    private Pergunta pergunta;

    @Column(name = "descricao_opcao", nullable = false, columnDefinition = "TEXT")
    private String descricaoOpcao;

    // Não precisamos mapear is_correta explicitamente aqui se a lógica
    // de qual opção foi selecionada fica na tabela de RespostaColaborador.
    // Se precisar da informação de qual *deveria* ser a correta (para testes/quizzes),
    // descomente a linha abaixo:
    // @Column(name = "is_correta", nullable = false)
    // private boolean isCorreta = false; // Valor padrão false
}