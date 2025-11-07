package br.com.AllTallent.dto; 

import br.com.AllTallent.model.AvaliacaoFuncionario;
import lombok.Getter; 

@Getter 
public class AvaliacaoFuncionarioResponseDTO {

    private final Long codigo;
    private final Integer funcionarioCodigo;
    private final String funcionarioNome;
    private final Integer avaliacaoCodigo;
    private final String avaliacaoTitulo;
    private final String comentarioColaborador;
    private final String comentarioSupervisao;
    private final String resultadoStatus;
    private final Integer nota;

    // Construtor que converte a Entidade para o DTO
    public AvaliacaoFuncionarioResponseDTO(AvaliacaoFuncionario entidade) {
        this.codigo = entidade.getCodigo();
        this.funcionarioCodigo = (entidade.getFuncionario() != null) ? entidade.getFuncionario().getCodigo() : null;
        this.funcionarioNome = (entidade.getFuncionario() != null) ? entidade.getFuncionario().getNomeCompleto() : null;
        this.avaliacaoCodigo = (entidade.getAvaliacao() != null) ? entidade.getAvaliacao().getCodigo() : null;
        this.avaliacaoTitulo = (entidade.getAvaliacao() != null) ? entidade.getAvaliacao().getTitulo() : null;
        this.comentarioColaborador = entidade.getComentarioColaborador();
        this.comentarioSupervisao = entidade.getComentarioSupervisao();
        this.resultadoStatus = entidade.getResultadoStatus();
        this.nota = entidade.getNota();
    }
}