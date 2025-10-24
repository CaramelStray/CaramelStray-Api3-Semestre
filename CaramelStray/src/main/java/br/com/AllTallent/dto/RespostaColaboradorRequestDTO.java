package br.com.AllTallent.dto; // Ou o pacote de DTOs

import jakarta.validation.constraints.NotNull;

public record RespostaColaboradorRequestDTO(

    @NotNull(message = "Código da instância da avaliação do funcionário é obrigatório.")
    Long funcionarioAvaliacaoCodigo,

    @NotNull(message = "Código da pergunta é obrigatório.")
    Long perguntaCodigo,

    // Apenas um dos dois abaixo deve ser preenchido, dependendo do tipo da pergunta
    String respostaTexto, // Para tipo 'texto' ou 'escala/nota' (guardar a nota como string?)
    Long opcaoSelecionadaCodigo // Para tipo 'multipla escolha'

    // Nota: Poderia ter validação customizada para garantir que apenas um dos dois (texto/opcao) seja enviado.
) {}