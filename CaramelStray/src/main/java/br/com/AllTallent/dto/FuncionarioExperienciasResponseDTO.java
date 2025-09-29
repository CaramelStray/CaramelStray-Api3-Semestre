
package br.com.AllTallent.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import br.com.AllTallent.model.Funcionario;

public record FuncionarioExperienciasResponseDTO(
    Integer codigoFuncionario,
    String nomeCompleto,
    List<ExperienciaDTO> experiencias
) {
    public FuncionarioExperienciasResponseDTO(Funcionario funcionario) {
        this(
            funcionario.getCodigo(),
            funcionario.getNomeCompleto(),
           
            funcionario.getExperiencias() != null
                ? funcionario.getExperiencias().stream()
                    .map(ExperienciaDTO::new)
                    .collect(Collectors.toList())
                : Collections.emptyList() 
        );
    }
}