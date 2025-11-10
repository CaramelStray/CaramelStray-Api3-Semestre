package br.com.AllTallent.dto;

import br.com.AllTallent.model.Avaliacao;
import java.time.LocalDate;
import java.util.Collections; 
import java.util.List;
import java.util.stream.Collectors;


public record AvaliacaoDetalhadaDTO(
    Integer codigo,
    String titulo,
    String status,
    LocalDate dataCriacao,
    LocalDate dataPrazo,
    String nomeCriador,
    List<PerguntaResponseDTO> perguntas,             
    List<AvaliacaoFuncionarioResponseDTO> instancias 
) {
    
    public AvaliacaoDetalhadaDTO(Avaliacao avaliacao) {
        this(
            avaliacao.getCodigo(),
            avaliacao.getTitulo(),
            avaliacao.getStatus(),
            avaliacao.getDataCriacao(),
            avaliacao.getDataPrazo(),
            (avaliacao.getCriador() != null) ? avaliacao.getCriador().getNomeCompleto() : "Sistema",
            (avaliacao.getPerguntas() != null) ?
                avaliacao.getPerguntas().stream()
                    .map(PerguntaResponseDTO::new) 
                    .collect(Collectors.toList())
                : Collections.emptyList(), 

            
            (avaliacao.getInstanciasAvaliacao() != null) ?
                avaliacao.getInstanciasAvaliacao().stream()
                    .map(AvaliacaoFuncionarioResponseDTO::new) 
                    .collect(Collectors.toList())
                : Collections.emptyList() 
        );
    }
}