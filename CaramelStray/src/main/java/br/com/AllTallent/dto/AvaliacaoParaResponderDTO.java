package br.com.AllTallent.dto;

import br.com.AllTallent.model.Avaliacao; 
import br.com.AllTallent.model.AvaliacaoFuncionario;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record AvaliacaoParaResponderDTO(
    Long avaliacaoFuncionarioCodigo, 
    String tituloAvaliacao,         
    LocalDate dataPrazo,            
    List<PerguntaParaResponderDTO> perguntas 
) {
    
    public AvaliacaoParaResponderDTO(AvaliacaoFuncionario instancia, Avaliacao avaliacaoBase) {
        this(
            instancia.getCodigo(),
            avaliacaoBase.getTitulo(),
            avaliacaoBase.getDataPrazo(),
            (avaliacaoBase.getPerguntas() != null) ?
                avaliacaoBase.getPerguntas().stream()
                    .map(PerguntaParaResponderDTO::new) 
                    .collect(Collectors.toList())
                : Collections.emptyList()
        );
    }
} 