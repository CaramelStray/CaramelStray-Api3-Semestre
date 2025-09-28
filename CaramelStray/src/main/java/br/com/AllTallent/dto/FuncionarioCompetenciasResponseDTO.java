package br.com.AllTallent.dto;

//import br.com.AllTallent.model.Competencia;
import br.com.AllTallent.model.Funcionario;
import java.util.List;
import java.util.stream.Collectors;

public record FuncionarioCompetenciasResponseDTO(
    List<CompetenciaDTO> competencias
) {
    public FuncionarioCompetenciasResponseDTO(Funcionario funcionario) {
        this(
            funcionario.getCompetencias() != null ?
                funcionario.getCompetencias().stream().map(CompetenciaDTO::new).collect(Collectors.toList()) :
                List.of()
        );
    }
}
