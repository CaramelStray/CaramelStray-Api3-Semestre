package br.com.AllTallent.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "tb_cad_pergunta_opcao")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OpcaoPergunta2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "descricao_opcao", columnDefinition = "TEXT", nullable = false)
    private String descricaoOpcao;

    @Column(name = "is_correta", nullable = false)
    private Boolean isCorreta = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pergunta", referencedColumnName = "codigo", nullable = false)
    @ToString.Exclude
    private Pergunta2 pergunta2;

    @OneToMany(mappedBy = "opcaoSelecionada", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RespostaColaborador>  respostaColaboradores;

}
