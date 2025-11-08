package br.com.AllTallent.model; // O pacote está correto

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
@ToString(of = {"codigo", "nomeCompleto"}) 
@EqualsAndHashCode(of = "codigo") 
@Entity
@Table(name = "tb_cad_funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "email") // Adicionado @Column para garantir
    private String email;
    
    private String cpf;
    private String telefone;

    @Column(name = "senha_hash")
    private String senhaHash; // O @Getter do Lombok vai criar o getSenhaHash()

    @Column(name = "data_cadastro", updatable = false)
    private OffsetDateTime dataCadastro;
    
    @Column(name ="titulo_profissional")
    private String tituloProfissional;
    
    private String localizacao;
    private String resumo;

    // --- RELACIONAMENTOS ---

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

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FuncionarioCertificado> certificados;
    
    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Experiencia> experiencias;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_cad_funcionario_competencia",
        joinColumns = @JoinColumn(name = "codigo_funcionario"),
        inverseJoinColumns = @JoinColumn(name = "codigo_competencia")
    )
    private Set<Competencia> competencias;
    
    // Os campos duplicados e getters manuais foram REMOVIDOS.
}