package br.com.AllTallent.dto;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import br.com.AllTallent.model.Funcionario;

public record FuncionarioPerfilDTO(
    Integer codigo,
    String nomeCompleto,
    String email,
    String telefone,
    OffsetDateTime dataCadastro,
    String tituloProfissional,
    String resumo, // Ordem corrigida e mantida
    String localizacao,
    String nomeArea,
    String nomeGestor,
    List<CompetenciaDTO> competencias,
    List<CertificadoDTO> certificados
) {
    // Construtor "tradutor" simplificado
    public FuncionarioPerfilDTO(Funcionario funcionario) {
        this(
            funcionario.getCodigo(),
            funcionario.getNomeCompleto(),
            funcionario.getEmail(),
            funcionario.getTelefone(),
            funcionario.getDataCadastro(),
            funcionario.getTituloProfissional(),
            // ✅ CORREÇÃO: Pega o resumo direto do funcionário
            funcionario.getResumo(),
            funcionario.getLocalizacao(),
            // Acessa os nomes das entidades relacionadas (pode ser nulo)
            funcionario.getArea() != null ? funcionario.getArea().getNome() : null,
            funcionario.getGestor() != null ? funcionario.getGestor().getNomeCompleto() : null,
            // Transforma o Set<Competencia> em uma List<CompetenciaDTO>
            funcionario.getCompetencias() != null ?
                funcionario.getCompetencias().stream().map(CompetenciaDTO::new).collect(Collectors.toList()) :
                Collections.emptyList(),
            // Transforma o Set<FuncionarioCertificado> em uma List<CertificadoDTO>
            funcionario.getCertificados() != null ?
                funcionario.getCertificados().stream().map(CertificadoDTO::new).collect(Collectors.toList()) :
                Collections.emptyList()
        );
    }
}