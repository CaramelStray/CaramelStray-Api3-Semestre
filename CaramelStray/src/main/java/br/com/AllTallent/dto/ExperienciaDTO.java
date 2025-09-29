// Crie este novo arquivo em: br/com/AllTallent/dto/ExperienciaDTO.java

package br.com.AllTallent.dto;

import br.com.AllTallent.model.Experiencia;
import java.time.LocalDate;

public record ExperienciaDTO(
    Integer codigo,
    String cargo,
    String empresa,
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim
) {
    // Construtor que "traduz" a entidade para o DTO
    public ExperienciaDTO(Experiencia experiencia) {
        this(
            experiencia.getCodigo(),
            experiencia.getCargo(),
            experiencia.getEmpresa(),
            experiencia.getDescricao(),
            experiencia.getDataInicio(),
            experiencia.getDataFim()
        );
    }
}