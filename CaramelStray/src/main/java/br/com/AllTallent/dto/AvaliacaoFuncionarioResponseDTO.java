package br.com.AllTallent.dto; // Ou o pacote onde seus DTOs estão

import br.com.AllTallent.model.AvaliacaoFuncionario;
import lombok.Getter; // Usar @Getter aqui é útil para records com lógica no construtor

@Getter // Adiciona getters para todos os campos
public class AvaliacaoFuncionarioResponseDTO {

    private final Long codigo;
    private final Integer funcionarioCodigo; // ID do funcionário avaliado
    private final String funcionarioNome;    // Nome do funcionário avaliado
    private final Integer avaliacaoCodigo;   // ID da avaliação base
    private final String avaliacaoTitulo;    // Título da avaliação base
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
        // Nota: Não incluímos a lista de 'respostas' aqui para manter o DTO leve.
        // Se precisar das respostas, pode ser melhor ter um endpoint separado para buscá-las.
    }

    // Lombok @Getter gera os métodos getCodigo(), getFuncionarioCodigo(), etc.
}