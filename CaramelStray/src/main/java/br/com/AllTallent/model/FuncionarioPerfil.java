/*package br.com.AllTallent.model;

import jakarta.persistence.*; // Este import já inclui @Id, @GeneratedValue, etc.
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"funcionario", "experiencias"})
@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_funcionario_perfil")
public class FuncionarioPerfil {

    // --- CORREÇÃO AQUI ---
    // Adicionamos @Id para marcar como chave primária
    // e @GeneratedValue para indicar que o banco gera o valor.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @Column(name = "titulo_profissional")
    private String tituloProfissional;
    
    private String resumo;
    private String localizacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_funcionario")
    private Funcionario funcionario;

}*/