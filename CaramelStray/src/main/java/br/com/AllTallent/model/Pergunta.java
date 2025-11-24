package br.com.AllTallent.model; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo") 
@Entity
@Table(name = "tb_cad_pergunta")
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long codigo; 


    private String pergunta; 

    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "codigo_competencia") 
    private Competencia competencia; 

    @Column(name = "tipo_pergunta") 
    private String tipoPergunta; 

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PerguntaOpcao> opcoes;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<AvaliacaoPergunta> avaliacoesVinculadas;

}