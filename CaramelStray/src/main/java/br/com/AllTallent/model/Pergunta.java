package br.com.AllTallent.model; // Ou o pacote onde seus modelos estão

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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo") // Boa prática para entidades
@Entity
@Table(name = "tb_cad_pergunta")
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assumindo que o ID é gerado, embora o dump não mostre sequência
    private Long codigo; // Usando Long porque no SQL está bigint

    // No SQL está "char", que é muito limitado (char(1)). 
    // Mapeando para String, que o JPA geralmente mapeia para VARCHAR ou TEXT.
    // Se precisar forçar TEXT, use @Column(columnDefinition = "TEXT")
    private String pergunta; 

    // Relacionamento: Muitas perguntas podem estar relacionadas a UMA competência.
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "codigo_competencia") // Nome da coluna FK no banco
    private Competencia competencia; 

    @Column(name = "tipo_pergunta") // Mapeia para a nova coluna do banco
    private String tipoPergunta; // Ex: "texto", "multipla escolha", "escala/nota"

    // --- NOVO RELACIONAMENTO ---
    // Uma Pergunta pode ter muitas Opcoes
    // cascade = CascadeType.ALL: Se salvar/deletar Pergunta, salva/deleta Opcoes tbm
    // orphanRemoval = true: Se remover uma Opcao da lista 'opcoes', ela é deletada do banco
    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PerguntaOpcao> opcoes;
}