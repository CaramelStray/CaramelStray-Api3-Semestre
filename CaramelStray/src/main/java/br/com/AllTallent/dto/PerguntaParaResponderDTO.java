package br.com.AllTallent.dto;

import br.com.AllTallent.model.Pergunta; // Importar o modelo
import java.util.Collections; // Para Collections.emptyList()
import java.util.List;
import java.util.stream.Collectors;

public record PerguntaParaResponderDTO(
    Long codigo,
    String pergunta,
    String tipoPergunta,
    String competenciaNome,
    List<PerguntaOpcaoDTO> opcoes
) {
    
    public PerguntaParaResponderDTO(Pergunta entidade) {
        this(
            entidade.getCodigo(),
            entidade.getPergunta(),
            entidade.getTipoPergunta(),
            (entidade.getCompetencia() != null) ? entidade.getCompetencia().getNome() : "Sem Categoria",
            
            (entidade.getOpcoes() != null) ?
                entidade.getOpcoes().stream()
                    .map(PerguntaOpcaoDTO::new)
                    .collect(Collectors.toList())
                : Collections.emptyList()
        );
    }
}