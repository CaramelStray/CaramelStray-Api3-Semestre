package br.com.AllTallent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record RevisaoSupervisorRequestDTO(

    @NotBlank(message = "O comentário de supervisão (interno) é obrigatório.")
    String comentarioSupervisao,

    String comentarioParaColaborador,

    @NotNull(message = "A nota é obrigatória.")
    @Min(value = 1, message = "Nota mínima é 1.")
    @Max(value = 3, message = "Nota máxima é 3.")
    Integer nota,

    @NotBlank(message = "O status final da avaliação é obrigatório.")
    String resultadoStatus
) {}