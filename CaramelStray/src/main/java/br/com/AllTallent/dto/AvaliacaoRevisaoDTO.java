package br.com.AllTallent.dto;

import br.com.AllTallent.model.Avaliacao;
import br.com.AllTallent.model.AvaliacaoFuncionario;
//import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.model.RespostaColaborador; 

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record AvaliacaoRevisaoDTO(
    Long avaliacaoFuncionarioCodigo,
    String nomeFuncionario,
    String tituloAvaliacao,
    String comentarioColaborador,
    String statusAtual,
    List<PerguntaComRespostaDTO> perguntasComRespostas 
) {
    public AvaliacaoRevisaoDTO(AvaliacaoFuncionario instancia, Avaliacao avaliacaoBase) {
        this(
            instancia.getCodigo(),
            (instancia.getFuncionario() != null) ? instancia.getFuncionario().getNomeCompleto() : null,
            avaliacaoBase.getTitulo(),
            instancia.getComentarioColaborador(),
            instancia.getResultadoStatus(),
            (avaliacaoBase.getPerguntas() != null) ?
                avaliacaoBase.getPerguntas().stream()
                    .map(pergunta -> new PerguntaComRespostaDTO(pergunta, (instancia.getRespostas() != null ? List.copyOf(instancia.getRespostas()) : Collections.<RespostaColaborador>emptyList()) ))
                    .collect(Collectors.toList())
                : Collections.emptyList()
        );
    }
}