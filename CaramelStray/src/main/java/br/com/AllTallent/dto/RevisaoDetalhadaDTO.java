package br.com.AllTallent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevisaoDetalhadaDTO {
    
    private String perguntaTexto;
    private String respostaDada; // O texto que o colaborador escreveu ou a opção escolhida
    private Long opcaoSelecionadaId; // ID da opção (se for múltipla escolha)
    private Long perguntaId;
}