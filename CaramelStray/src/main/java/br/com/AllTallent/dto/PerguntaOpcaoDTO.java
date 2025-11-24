package br.com.AllTallent.dto;

import br.com.AllTallent.model.PerguntaOpcao; 

public record PerguntaOpcaoDTO(
    Long codigo,         
    String descricaoOpcao 
) {
    
    public PerguntaOpcaoDTO(PerguntaOpcao entidade) {
        this(entidade.getCodigo(), entidade.getDescricaoOpcao());
    }
}