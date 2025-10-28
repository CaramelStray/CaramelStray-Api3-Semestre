package br.com.AllTallent.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
@Entity
@Table(name = "tb_cad_funcionario_certificado") 
public class FuncionarioCertificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;
    
    private String certificado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_funcionario") 
    private Funcionario funcionario;
}