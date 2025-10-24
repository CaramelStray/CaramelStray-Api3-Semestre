package br.com.AllTallent.dto; // Ou o pacote onde seus DTOs estão

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PerguntaRequestDTO(

    @NotBlank(message = "O texto da pergunta não pode ser vazio.")
    @Size(max = 2000, message = "O texto da pergunta excede o limite de caracteres.") // Ajuste o max se necessário
    String pergunta,

    @NotNull(message = "O código da competência é obrigatório.")
    Integer competenciaCodigo, // Recebemos apenas o ID da competência

    // Campos que não estão na entidade Pergunta, mas são úteis para a aplicação
    String tipoPergunta,  // Ex: "texto", "multipla escolha", "escala/nota"
    List<String> opcoes   // Lista de opções, se tipoPergunta for "multipla escolha"

    // Nota: Não precisamos receber o 'codigo' da pergunta aqui, pois será gerado pelo banco.
) {}