package br.com.AllTallent.dto;

/**
 * Este é o "formulário" que a sua API recebe para criar ou atualizar um funcionário.
 * Note que para os relacionamentos (Area, Perfil, Gestor), nós recebemos apenas os IDs.
 * Isso é mais simples e seguro.
 */
public record FuncionarioRequestDTO(
    String nomeCompleto,
    String email,
    String cpf,
    String telefone,
    String senhaHash,
    Integer areaId,
    Integer perfilId,
    Integer gestorId,
    String tituloProfissional,
    String localizacao,
    String resumo
) {}