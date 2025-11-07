package br.com.AllTallent.dto; 

import jakarta.validation.constraints.NotNull;

public record RespostaColaboradorRequestDTO(

    @NotNull(message = "Código da instância da avaliação do funcionário é obrigatório.")
    Long funcionarioAvaliacaoCodigo,

    @NotNull(message = "Código da pergunta é obrigatório.")
    Long perguntaCodigo,

    String respostaTexto, 
    Long opcaoSelecionadaCodigo 

) {}