package br.com.AllTallent.dto;

import br.com.AllTallent.model.Competencia;

public record CompetenciaDTO(Integer id, String nome, String categoria) {
    public CompetenciaDTO(Competencia competencia) {
        this(
            competencia.getCodigo(), 
            competencia.getNome(),
            competencia.getCategoria() // O parêntese de fechamento vem AQUI, depois do último argumento
        );
    }
}