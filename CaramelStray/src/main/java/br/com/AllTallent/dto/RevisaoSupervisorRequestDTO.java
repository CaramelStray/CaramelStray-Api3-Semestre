package br.com.AllTallent.dto;

import jakarta.validation.constraints.NotBlank;
public record RevisaoSupervisorRequestDTO(

    @NotBlank(message = "O comentário de supervisão (interno) é obrigatório.")
    String comentarioSupervisao,

    String comentarioParaColaborador,

    @NotBlank(message = "O status final da avaliação é obrigatório.")
    String resultadoStatus
) {}