package br.com.AllTallent.dto;

import br.com.AllTallent.model.Competencia;

public record CompetenciaDTO(Integer id, String nome) {
    public CompetenciaDTO(Competencia competencia) {
        this(
        competencia.getCodigo(), 
        competencia.getNome());

    }
}