package br.com.AllTallent.dto;

import br.com.AllTallent.model.Avaliacao2;
import br.com.AllTallent.model.AvaliacaoFuncionario;
//import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.model.RespostaColaborador; // Importar RespostaColaborador

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record AvaliacaoRevisaoDTO(
    Long avaliacaoFuncionarioCodigo, // ID da instância
    String nomeFuncionario,         // Nome do avaliado
    String tituloAvaliacao,         // Título da avaliação base
    String comentarioColaborador,   // Comentário (se houver) do colaborador
    String statusAtual,             // Status atual da instância (ex: AGUARDANDO_REVISAO)
    List<PerguntaComRespostaDTO> perguntasComRespostas // Lista de perguntas e respostas
) {
    // Construtor que recebe a instância e a avaliação base
    // Assumimos que perguntas (com opções) e respostas foram carregadas
    public AvaliacaoRevisaoDTO(AvaliacaoFuncionario instancia, Avaliacao2 avaliacaoBase) {
        this(
            instancia.getCodigo(),
            (instancia.getFuncionario() != null) ? instancia.getFuncionario().getNomeCompleto() : null,
            avaliacaoBase.getTitulo(),
            instancia.getComentarioColaborador(),
            instancia.getResultadoStatus(),
            // Mapeia cada pergunta da avaliação base para PerguntaComRespostaDTO,
            // passando a lista completa de respostas da instância para encontrar a correspondente.
            (avaliacaoBase.getPerguntas() != null) ?
                avaliacaoBase.getPerguntas().stream()
                    .map(pergunta -> new PerguntaComRespostaDTO(pergunta, (instancia.getRespostas() != null ? List.copyOf(instancia.getRespostas()) : Collections.<RespostaColaborador>emptyList()) ))
                    .collect(Collectors.toList())
                : Collections.emptyList()
        );
    }
}