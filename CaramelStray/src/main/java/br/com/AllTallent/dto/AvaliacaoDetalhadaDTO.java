package br.com.AllTallent.dto;

import br.com.AllTallent.model.Avaliacao2; 
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
    List<PerguntaResponseDTO> perguntas,             
    List<AvaliacaoFuncionarioResponseDTO> instancias 
) {
    
    public AvaliacaoDetalhadaDTO(Avaliacao2 avaliacao) {
        this(
            avaliacao.getCodigo(),
            avaliacao.getTitulo(),
            avaliacao.getStatus(),
            avaliacao.getDataCriacao(),
            avaliacao.getDataPrazo(),

            // Mapeia o Set<Pergunta> para uma List<PerguntaResponseDTO>
            // PerguntaResponseDTO é mais simples que PerguntaParaResponderDTO aqui
            (avaliacao.getPerguntas() != null) ?
                avaliacao.getPerguntas().stream()
                    .map(PerguntaResponseDTO::new) // Converte cada Pergunta para PerguntaResponseDTO
                    .collect(Collectors.toList())
                : Collections.emptyList(), // Retorna lista vazia se não houver perguntas

            // Mapeia o Set<AvaliacaoFuncionario> para uma List<AvaliacaoFuncionarioResponseDTO>
            (avaliacao.getInstanciasAvaliacao() != null) ?
                avaliacao.getInstanciasAvaliacao().stream()
                    .map(AvaliacaoFuncionarioResponseDTO::new) // Converte cada instancia para seu DTO
                    .collect(Collectors.toList())
                : Collections.emptyList() // Retorna lista vazia se não houver instâncias
        );
    }
}