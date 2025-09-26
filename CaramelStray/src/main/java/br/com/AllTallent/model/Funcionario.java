package br.com.AllTallent.model;

import java.time.OffsetDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// --- NOVO PADRÃO AQUI ---
// Gera um toString() seguro, ex: "Funcionario(codigo=1, nomeCompleto=Leonardo Vianna)"
@ToString(of = {"codigo", "nomeCompleto"}) 
// Gera equals() e hashCode() baseados APENAS no código. É a forma correta e segura.
@EqualsAndHashCode(of = "codigo") 
@Entity
@Table(name = "tb_cad_funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    private String email;
    private String cpf;
    private String telefone;

    @Column(name = "senha_hash")
    private String senhaHash;

    @Column(name = "data_cadastro", updatable = false)
    private OffsetDateTime dataCadastro;

    // --- RELACIONAMENTOS (não mudam) ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_area")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_perfil")
    private Perfil perfil;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_gestor")
    private Funcionario gestor;

    @OneToMany(mappedBy = "gestor")
    private Set<Funcionario> equipe;
    
    @OneToOne(mappedBy = "funcionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FuncionarioPerfil perfilProfissional;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FuncionarioCertificado> certificados;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_cad_funcionario_competencia",
        joinColumns = @JoinColumn(name = "codigo_funcionario"),
        inverseJoinColumns = @JoinColumn(name = "codigo_competencia")
    )
    private Set<Competencia> competencias;
}
