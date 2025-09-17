package br.com.AllTallent.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_CAD_AREA") // Mapeia para a tabela "TB_CAD_AREA"
public class Area {

    @Id
    @Column(name = "CODIGO") // Mapeia para a coluna "CODIGO"
    private Integer codigo;

    @Column(name = "TAGS") // Mapeia para a coluna "TAGS"
    private String tags;

    // Construtor vazio (exigido pelo JPA)
    public Area() {
    }

    // Getters e Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
