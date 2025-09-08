package br.com.william.caramelstray.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_CAD_FUNCIONARIO") // Mapeia para a tabela "TB_CAD_FUNCIONARIO"
public class Funcionario {

    @Id
    @Column(name = "CODIGO")
    private Integer codigo;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "TELEFONE")
    private String telefone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CPF")
    private String cpf;

    // --- Relacionamentos ---

    @ManyToOne // Muitos funcionários para um perfil
    @JoinColumn(name = "CODIGO_PERFIL") // Chave estrangeira na tabela TB_CAD_FUNCIONARIO
    private Perfil perfil;

    @ManyToOne // Muitos funcionários para uma área
    @JoinColumn(name = "CODIGO_AREA") // Chave estrangeira na tabela TB_CAD_FUNCIONARIO
    private Area area;


    // Construtor vazio
    public Funcionario() {
    }

    // Getters e Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
