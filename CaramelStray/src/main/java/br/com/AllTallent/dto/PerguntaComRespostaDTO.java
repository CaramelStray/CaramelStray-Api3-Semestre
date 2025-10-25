package br.com.AllTallent.dto;

import br.com.AllTallent.model.Pergunta;
import br.com.AllTallent.model.RespostaColaborador; // Importar RespostaColaborador

import java.util.Collections;
import java.util.List;
//import java.util.Optional; // Para Optional
import java.util.stream.Collectors;

public record PerguntaComRespostaDTO(
    Long perguntaCodigo,
    String perguntaTexto,
    String tipoPergunta,
    List<PerguntaOpcaoDTO> opcoes,       // Todas as opções da pergunta
    String respostaTexto,            // Resposta do colaborador (se texto/escala)
    Long opcaoSelecionadaCodigo   // ID da opção selecionada (se múltipla escolha)
) {
    // Construtor que recebe a Pergunta e a RespostaColaborador correspondente (pode ser null se não respondida)
    public PerguntaComRespostaDTO(Pergunta pergunta, RespostaColaborador resposta) {
        this(
            pergunta.getCodigo(),
            pergunta.getPergunta(),
            pergunta.getTipoPergunta(),
            // Mapeia as opções da pergunta
            (pergunta.getOpcoes() != null) ?
                pergunta.getOpcoes().stream().map(PerguntaOpcaoDTO::new).collect(Collectors.toList())
                : Collections.emptyList(),
            // Pega a resposta de texto, se houver
            (resposta != null) ? resposta.getRespostaTexto() : null,
            // Pega o código da opção selecionada, se houver
            (resposta != null && resposta.getOpcaoSelecionada() != null) ? resposta.getOpcaoSelecionada().getCodigo() : null
        );
    }

     // Construtor alternativo que recebe a Pergunta e uma LISTA de todas as respostas da instância.
     // Ele encontra a resposta correta para esta pergunta.
     public PerguntaComRespostaDTO(Pergunta pergunta, List<RespostaColaborador> todasRespostas) {
         this(
             pergunta.getCodigo(),
             pergunta.getPergunta(),
             pergunta.getTipoPergunta(),
             (pergunta.getOpcoes() != null) ?
                 pergunta.getOpcoes().stream().map(PerguntaOpcaoDTO::new).collect(Collectors.toList())
                 : Collections.emptyList(),
             // Encontra a resposta para esta pergunta na lista
             todasRespostas.stream()
                 .filter(r -> r.getPergunta() != null && r.getPergunta().getCodigo().equals(pergunta.getCodigo()))
                 .findFirst()
                 .map(RespostaColaborador::getRespostaTexto)
                 .orElse(null),
             todasRespostas.stream()
                 .filter(r -> r.getPergunta() != null && r.getPergunta().getCodigo().equals(pergunta.getCodigo()) && r.getOpcaoSelecionada() != null)
                 .findFirst()
                 .map(r -> r.getOpcaoSelecionada().getCodigo())
                 .orElse(null)
         );
     }
}