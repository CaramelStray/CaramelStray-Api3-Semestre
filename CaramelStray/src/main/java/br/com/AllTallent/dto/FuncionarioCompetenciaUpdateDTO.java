package br.com.AllTallent.dto;

import java.util.Set;

public record FuncionarioCompetenciaUpdateDTO(
    Set<Integer> codigosCompetencia 
) {}