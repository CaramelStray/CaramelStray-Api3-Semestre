package br.com.AllTallent.dto; 

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PerguntaRequestDTO(

    @NotBlank(message = "O texto da pergunta não pode ser vazio.")
    @Size(max = 2000, message = "O texto da pergunta excede o limite de caracteres.") 
    String pergunta,

    @NotNull(message = "O código da competência é obrigatório.")
    Integer competenciaCodigo, 

    String tipoPergunta,
    List<OpcaoRequest> opcoes

) {}