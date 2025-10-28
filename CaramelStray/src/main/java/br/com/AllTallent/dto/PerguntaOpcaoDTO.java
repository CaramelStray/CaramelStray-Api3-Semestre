package br.com.AllTallent.dto;

import br.com.AllTallent.model.PerguntaOpcao; // Importar o modelo

public record PerguntaOpcaoDTO(
    Long codigo,         // ID da opção (importante para salvar a resposta)
    String descricaoOpcao // Texto da opção a ser exibido
) {
    // Construtor para facilitar a conversão
    public PerguntaOpcaoDTO(PerguntaOpcao entidade) {
        this(entidade.getCodigo(), entidade.getDescricaoOpcao());
    }
}