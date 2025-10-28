package br.com.AllTallent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min; // Para nota mínima
import jakarta.validation.constraints.Max; // Para nota máxima

public record RevisaoSupervisorRequestDTO(
    // O ID da AvaliacaoFuncionario virá pela URL (ex: PUT /api/avaliacoes/instancias/{id}/revisar)

    @NotBlank(message = "O comentário de supervisão (interno) é obrigatório.")
    String comentarioSupervisao, // Feedback interno/privado

    String comentarioParaColaborador, // Feedback público (opcional?)

    @NotNull(message = "A nota é obrigatória.")
    @Min(value = 1, message = "Nota mínima é 1.") // Ajuste a escala conforme necessário
    @Max(value = 3, message = "Nota máxima é 3.") // Ajuste a escala conforme necessário
    Integer nota,

    @NotBlank(message = "O status final da avaliação é obrigatório.")
    String resultadoStatus // Ex: APROVADO, REPROVADO_COM_PLANO, etc.
) {}