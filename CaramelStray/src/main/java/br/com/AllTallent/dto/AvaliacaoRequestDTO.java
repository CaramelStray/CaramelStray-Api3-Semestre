package br.com.AllTallent.dto;

import java.util.List;

public record AvaliacaoRequestDTO(
    String titulo,
    List<Integer> codigosFuncionarios,
    List<Long> codigosPerguntas 
) {
}