package br.com.AllTallent.dto;

import br.com.AllTallent.model.Competencia;

public record CompetenciaDTO(String nome) {
    public CompetenciaDTO(Competencia competencia) {
        this(competencia.getNome());
    }
}