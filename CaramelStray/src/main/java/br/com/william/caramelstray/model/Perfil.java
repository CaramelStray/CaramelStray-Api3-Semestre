package br.com.william.caramelstray.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_CAD_FUNCIONARIO_PERFIL") // Mapeia para a tabela "TB_CAD_FUNCIONARIO_PERFIL"
public class Perfil {

    @Id
    @Column(name = "CODIGO")
    private Integer codigo;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "PERMISSOES")
    private String permissoes;

    // Construtor vazio
    public Perfil() {
    }

    // Getters e Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(String permissoes) {
        this.permissoes = permissoes;
    }
}
