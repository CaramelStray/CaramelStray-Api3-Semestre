package br.com.AllTallent.dto;

import java.time.LocalDate;
import java.util.List;

public record AvaliacaoRequestDTO(
    String titulo,
    LocalDate dataPrazo,
    List<Integer> codigosFuncionarios,
    List<Long> codigosPerguntas 
) {
}