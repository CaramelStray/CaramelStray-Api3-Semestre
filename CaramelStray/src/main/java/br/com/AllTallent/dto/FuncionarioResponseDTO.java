package br.com.AllTallent.dto;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import br.com.AllTallent.model.Funcionario;


public record FuncionarioResponseDTO(
    Integer codigo,
    String nomeCompleto,
    String email,
    String telefone,
    String nomeArea,
    String nomePerfil,
    String nomeGestor,
    Integer areaId,
    Integer perfilId,
    Integer gestorId,
    String tituloProfissional,
    String localizacao,
    String resumo,
    OffsetDateTime dataCadastro,
    List<CertificadoDTO> certificados,
    List<CompetenciaDTO> competencias,
    List<ExperienciaDTO> experiencias
) {
    
    public FuncionarioResponseDTO(Funcionario funcionario) {
        this(
            funcionario.getCodigo(),
            funcionario.getNomeCompleto(),
            funcionario.getEmail(),
            funcionario.getTelefone(),
            
            
            funcionario.getArea() != null ? funcionario.getArea().getNome() : null,
            funcionario.getPerfil() != null ? funcionario.getPerfil().getNome() : null,
            funcionario.getGestor() != null ? funcionario.getGestor().getNomeCompleto() : null,
            funcionario.getArea() != null ? funcionario.getArea().getCodigo() : null,
            funcionario.getPerfil() != null ? funcionario.getPerfil().getCodigo() : null,
            funcionario.getGestor() != null ? funcionario.getGestor().getCodigo() : null,
            funcionario.getTituloProfissional() ,
            funcionario.getLocalizacao(),
            funcionario.getResumo(),
            funcionario.getDataCadastro(),
            funcionario.getCertificados() != null
                ? funcionario.getCertificados().stream()
                    .map(CertificadoDTO::new)
                    .collect(Collectors.toList())
                : Collections.emptyList(), 
            funcionario.getCompetencias() != null
                ? funcionario.getCompetencias().stream()
                    .map(CompetenciaDTO::new)
                    .collect(Collectors.toList())
                : Collections.emptyList(),
            funcionario.getExperiencias() != null
                ? funcionario.getExperiencias().stream()
                    .map(ExperienciaDTO::new)
                    .collect(Collectors.toList())
                : Collections.emptyList()

        );
    }
}