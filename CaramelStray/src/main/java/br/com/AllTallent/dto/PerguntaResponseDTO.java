package br.com.AllTallent.dto; // Ou o pacote onde seus DTOs estão

import br.com.AllTallent.model.Pergunta;

// DTO para retornar a pergunta criada (incluindo o ID)
public record PerguntaResponseDTO(
    Long codigo,
    String pergunta,
    Integer competenciaCodigo,
    String competenciaNome
    // Nota: tipoPergunta e opcoes podem ou não ser incluídos aqui,
    // dependendo se você precisa deles de volta no frontend imediatamente.
    // Para simplificar, não incluí por enquanto.
) {
    // Construtor para facilitar a conversão da Entidade para DTO
    public PerguntaResponseDTO(Pergunta entidade) {
        this(
            entidade.getCodigo(),
            entidade.getPergunta(),
            entidade.getCompetencia() != null ? entidade.getCompetencia().getCodigo() : null,
            entidade.getCompetencia() != null ? entidade.getCompetencia().getNome() : null
        );
    }
}