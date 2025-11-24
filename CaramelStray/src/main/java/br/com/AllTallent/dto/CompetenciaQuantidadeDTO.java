package br.com.AllTallent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompetenciaQuantidadeDTO {
    private String nomeCompetencia;
    private Long quantidade;
}
