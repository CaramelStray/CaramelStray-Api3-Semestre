package br.com.AllTallent.dto;

import br.com.AllTallent.model.Funcionario;

/**
 * Este é o "cartão de visita" que a sua API envia de volta como resposta.
 * Ele traduz a entidade Funcionario para um formato limpo e amigável para o cliente,
 * mostrando os nomes das áreas e perfis em vez de apenas os códigos.
 */
public record FuncionarioResponseDTO(
    Integer codigo,
    String nomeCompleto,
    String email,
    String telefone,
    String nomeArea,
    String nomePerfil,
    String nomeGestor
) {
    // Este construtor é o "tradutor" mágico. Ele sabe como converter
    // a entidade Funcionario para este DTO.
    public FuncionarioResponseDTO(Funcionario funcionario) {
        this(
            funcionario.getCodigo(),
            funcionario.getNomeCompleto(),
            funcionario.getEmail(),
            funcionario.getTelefone(),
            // Lógica para evitar erros se a área, perfil ou gestor forem nulos
            funcionario.getArea() != null ? funcionario.getArea().getNome() : null,
            funcionario.getPerfil() != null ? funcionario.getPerfil().getNome() : null,
            funcionario.getGestor() != null ? funcionario.getGestor().getNomeCompleto() : null
        );
    }
}