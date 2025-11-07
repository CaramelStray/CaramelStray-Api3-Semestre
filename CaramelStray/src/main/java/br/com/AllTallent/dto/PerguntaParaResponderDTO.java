package br.com.AllTallent.dto;

import br.com.AllTallent.model.Pergunta; // Importar o modelo
import java.util.Collections; // Para Collections.emptyList()
import java.util.List;
import java.util.stream.Collectors;

public record PerguntaParaResponderDTO(
    Long codigo,                 // ID da pergunta
    String pergunta,             // Texto da pergunta
    String tipoPergunta,
    String competenciaNome,         // "texto", "multipla escolha", "escala/nota"
    List<PerguntaOpcaoDTO> opcoes // Lista de opções (vazia se não for múltipla escolha)
) {
    // Construtor para facilitar a conversão
    public PerguntaParaResponderDTO(Pergunta entidade) {
        this(
            entidade.getCodigo(),
            entidade.getPergunta(),
            entidade.getTipoPergunta(),
            (entidade.getCompetencia() != null) ? entidade.getCompetencia().getNome() : "Sem Categoria",
            // Mapeia as opções da entidade para DTOs, se existirem
            (entidade.getOpcoes() != null) ?
                entidade.getOpcoes().stream()
                    .map(PerguntaOpcaoDTO::new) // Converte cada PerguntaOpcao para PerguntaOpcaoDTO
                    .collect(Collectors.toList())
                : Collections.emptyList() // Retorna lista vazia se não houver opções
        );
    }
}