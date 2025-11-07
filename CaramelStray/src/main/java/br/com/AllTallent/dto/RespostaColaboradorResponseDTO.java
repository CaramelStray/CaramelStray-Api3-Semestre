package br.com.AllTallent.dto; 

import br.com.AllTallent.model.RespostaColaborador;

public record RespostaColaboradorResponseDTO(
    Long codigo,
    Long funcionarioAvaliacaoCodigo,
    Long perguntaCodigo,
    String respostaTexto,
    Long opcaoSelecionadaCodigo
) {
    public RespostaColaboradorResponseDTO(RespostaColaborador entidade) {
        this(
            entidade.getCodigo(),
            entidade.getAvaliacaoFuncionario() != null ? entidade.getAvaliacaoFuncionario().getCodigo() : null,
            entidade.getPergunta() != null ? entidade.getPergunta().getCodigo() : null,
            entidade.getRespostaTexto(),
            entidade.getOpcaoSelecionada() != null ? entidade.getOpcaoSelecionada().getCodigo() : null
        );
    }
}