package br.com.AllTallent.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroRequestDTO {

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    private String telefone;

    @NotBlank(message = "ID do Crachá é obrigatório")
    private String idCracha; // Novo campo

    private LocalDate dataAdmissao; // Novo campo (Envie como "YYYY-MM-DD")

    private String resumo; // Campo "Observações" do formulário

    @NotNull(message = "Departamento (código da área) é obrigatório")
    private Integer codigoArea; // ID do Departamento

    @NotNull(message = "Cargo (código do perfil) é obrigatório")
    private Integer codigoPerfil; // ID do Cargo
}