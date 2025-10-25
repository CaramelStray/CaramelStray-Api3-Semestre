package br.com.AllTallent.dto;

import br.com.AllTallent.model.Avaliacao; // Use o nome correto da sua classe de Avaliação
import java.time.LocalDate;
// Importe outros DTOs se precisar aninhar (ex: PerguntaDTO)

public record AvaliacaoResponseDTO(
    Integer codigo,
    String titulo,
    String status,
    LocalDate dataCriacao,
    LocalDate dataPrazo
    // Adicione outros campos se necessário (ex: lista de perguntas simplificada)
) {
    // Construtor pAara conversão
    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        this(
            avaliacao.getCodigo(),
            avaliacao.getTitulo(),
            avaliacao.getStatus(),
            avaliacao.getDataCriacao(),
            avaliacao.getDataPrazo()
        );
    }
}