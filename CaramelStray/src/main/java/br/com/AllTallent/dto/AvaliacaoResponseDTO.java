package br.com.AllTallent.dto;

import br.com.AllTallent.model.Avaliacao; 
import java.time.LocalDate;


public record AvaliacaoResponseDTO(
    Integer codigo,
    String titulo,
    String status,
    LocalDate dataCriacao,
    LocalDate dataPrazo,
    String nomeCriador
    
) {
    
    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        this(
            avaliacao.getCodigo(),
            avaliacao.getTitulo(),
            avaliacao.getStatus(),
            avaliacao.getDataCriacao(),
            avaliacao.getDataPrazo(),
            (avaliacao.getCriador() != null) ? avaliacao.getCriador().getNomeCompleto() : "Sistema"
        );
    }
}