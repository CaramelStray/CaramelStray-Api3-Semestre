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
    List<PerguntaOpcaoDTO> opcoes,
    String respostaTexto,
    Long opcaoSelecionadaCodigo
) {
    public PerguntaComRespostaDTO(Pergunta pergunta, RespostaColaborador resposta) {
        this(
            pergunta.getCodigo(),
            pergunta.getPergunta(),
            pergunta.getTipoPergunta(),
            (pergunta.getOpcoes() != null) ?
                pergunta.getOpcoes().stream().map(PerguntaOpcaoDTO::new).collect(Collectors.toList())
                : Collections.emptyList(),
            (resposta != null) ? resposta.getRespostaTexto() : null,
            (resposta != null && resposta.getOpcaoSelecionada() != null) ? resposta.getOpcaoSelecionada().getCodigo() : null
        );
    }

     public PerguntaComRespostaDTO(Pergunta pergunta, List<RespostaColaborador> todasRespostas) {
         this(
             pergunta.getCodigo(),
             pergunta.getPergunta(),
             pergunta.getTipoPergunta(),
             (pergunta.getOpcoes() != null) ?
                 pergunta.getOpcoes().stream().map(PerguntaOpcaoDTO::new).collect(Collectors.toList())
                 : Collections.emptyList(),
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