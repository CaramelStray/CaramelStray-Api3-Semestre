package br.com.AllTallent.dto;

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