package br.com.AllTallent.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "tb_cad_pergunta")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Pergunta2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "pergunta")
    private String pergunta;

    @Column(name = "tipo_pergunta", length = 50)
    private String tipoPergunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_competencia", referencedColumnName = "codigo")
    @ToString.Exclude
    private Competencia competencia;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<OpcaoPergunta2> opcoes;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RespostaColaborador> respostas;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<AvaliacaoPergunta> avaliacoesVinculadas;

}
