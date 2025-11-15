package br.com.AllTallent.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MesQuantidadeDTO {
    
    private String mes;
    private Integer quantidade;

    /**
     * Construtor especial para a query nativa do Spring.
     * A query retorna um 'Number' (BigInteger/BigDecimal) que convertemos para Integer.
     */
    public MesQuantidadeDTO(String mes, Number quantidade) {
        this.mes = mes;
        this.quantidade = (quantidade != null) ? quantidade.intValue() : 0;
    }
}