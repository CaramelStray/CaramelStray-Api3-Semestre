package br.com.AllTallent.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tb_cad_avaliacao_pergunta")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AvaliacaoPergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pergunta", referencedColumnName = "codigo")
    @ToString.Exclude
    private Pergunta2 pergunta2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_avaliacao", referencedColumnName = "codigo")
    @ToString.Exclude
    private Avaliacao2 avaliacao;


}
