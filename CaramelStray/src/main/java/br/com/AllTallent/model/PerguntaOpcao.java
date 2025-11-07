package br.com.AllTallent.model; // Ou o pacote onde seus modelos estão

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_pergunta_opcao")
public class PerguntaOpcao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_cad_pergunta_opcao_codigo_seq")
    @SequenceGenerator(name = "tb_cad_pergunta_opcao_codigo_seq", sequenceName = "tb_cad_pergunta_opcao_codigo_seq", allocationSize = 1)
    private Long codigo; // bigint

    // Relacionamento inverso: Muitas Opcoes pertencem a UMA Pergunta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pergunta", referencedColumnName = "codigo", nullable = false)
    @ToString.Exclude
    private Pergunta pergunta;

    @Column(name = "descricao_opcao", nullable = false, columnDefinition = "TEXT")
    private String descricaoOpcao;

    
    @Column(name = "is_correta", nullable = false)
    private boolean isCorreta = false; 

    public void setIsCorreta(boolean isCorreta) {
        this.isCorreta = isCorreta;
    }
}