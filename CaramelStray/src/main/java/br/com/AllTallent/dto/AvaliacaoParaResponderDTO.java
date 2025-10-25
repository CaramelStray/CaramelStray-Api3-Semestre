package br.com.AllTallent.dto;

import br.com.AllTallent.model.Avaliacao2; // Importar modelos
import br.com.AllTallent.model.AvaliacaoFuncionario;
import br.com.AllTallent.model.Pergunta;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record AvaliacaoParaResponderDTO(
    Long avaliacaoFuncionarioCodigo, // ID da instância específica que o funcionário está respondendo
    String tituloAvaliacao,         // Título da avaliação base
    LocalDate dataPrazo,            // Prazo para responder
    List<PerguntaParaResponderDTO> perguntas // Lista de perguntas com opções
) {
    // Construtor que recebe a instância (AvaliacaoFuncionario) e a avaliação base (Avaliacao2)
    // Assumimos que as perguntas foram carregadas (inicializadas) na Avaliacao2 antes de chamar este construtor
    public AvaliacaoParaResponderDTO(AvaliacaoFuncionario instancia, Avaliacao2 avaliacaoBase) {
        this(
            instancia.getCodigo(),
            avaliacaoBase.getTitulo(),
            avaliacaoBase.getDataPrazo(),
            // Mapeia as perguntas da avaliação base para o DTO específico
            (avaliacaoBase.getPerguntas() != null) ?
                avaliacaoBase.getPerguntas().stream()
                    // É crucial que as opções de cada pergunta também estejam carregadas (inicializadas) aqui!
                    .map(PerguntaParaResponderDTO::new) 
                    .collect(Collectors.toList())
                : Collections.emptyList()
        );
    }
}