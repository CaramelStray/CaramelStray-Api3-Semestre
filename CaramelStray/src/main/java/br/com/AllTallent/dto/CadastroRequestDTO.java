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
    private String idCracha; 

    private LocalDate dataAdmissao; 

    private String resumo; 

    @NotNull(message = "Departamento (código da área) é obrigatório")
    private Integer codigoArea; 

    @NotNull(message = "Cargo (código do perfil) é obrigatório")
    private Integer codigoPerfil; 

    // --- NOVOS CAMPOS ---
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    
    private String localizacao;
    
    private String tituloProfissional;
    
    private Integer codigoGestor; // Opcional
}