package br.com.AllTallent.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExperienciaRequestDTO(
    @NotBlank @Size(max = 150)
    String cargo,
    
    @NotBlank @Size(max = 150)
    String empresa,
    
    @NotNull
    LocalDate dataInicio,
    
    LocalDate dataFim, // Opcional
    
    @Size(max = 500)
    String descricao
) {}