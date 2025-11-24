package br.com.AllTallent.repository;

import java.util.Optional;
import java.util.List;
import br.com.AllTallent.dto.CompetenciaQuantidadeDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.AllTallent.model.AvaliacaoFuncionario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

@Repository
public interface AvaliacaoFuncionarioRepository extends JpaRepository<AvaliacaoFuncionario, Long> {
    
    Optional<AvaliacaoFuncionario> findByFuncionarioCodigoAndAvaliacaoCodigo(Integer funcionarioCodigo, Integer avaliacaoCodigo);
    List<AvaliacaoFuncionario> findByAvaliacaoCodigo(Integer avaliacaoCodigo);
    List<AvaliacaoFuncionario> findByFuncionarioCodigo(Integer funcionarioCodigo);

    // --- QUERIES GERAIS ---

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tb_cad_funcionario_avalicacao WHERE resultado_status = 'PENDENTE'")
    Integer countTotalPendentes();

    @Query(nativeQuery = true, value = """
        SELECT COUNT(fa.*) FROM tb_cad_funcionario_avalicacao fa
        JOIN tb_cad_avaliacao a ON fa.codigo_avalicacao = a.codigo
        WHERE fa.resultado_status <> 'PENDENTE' AND a.data_prazo BETWEEN :dataInicio AND :dataFim
    """)
    Integer countConcluidasNoMes(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query(nativeQuery = true, value = """
        SELECT COUNT(fa.*) FROM tb_cad_funcionario_avalicacao fa
        JOIN tb_cad_avaliacao a ON fa.codigo_avalicacao = a.codigo
        WHERE fa.resultado_status = 'APROVADO' AND a.data_prazo BETWEEN :dataInicio AND :dataFim
    """)
    Integer countAprovadasNoMes(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    // --- QUERIES FILTRADAS POR ÁREA (Nossa Correção) ---

    @Query(nativeQuery = true, value = """
        SELECT COUNT(fa.*) FROM tb_cad_funcionario_avalicacao fa
        JOIN tb_cad_funcionario f ON fa.codigo_funcionario_avalidado = f.codigo
        WHERE fa.resultado_status = 'PENDENTE' AND f.codigo_area = :codigoArea
    """)
    Integer countTotalPendentesByArea(@Param("codigoArea") Integer codigoArea);

    @Query(nativeQuery = true, value = """
        SELECT COUNT(fa.*) FROM tb_cad_funcionario_avalicacao fa
        JOIN tb_cad_avaliacao a ON fa.codigo_avalicacao = a.codigo
        JOIN tb_cad_funcionario f ON fa.codigo_funcionario_avalidado = f.codigo
        WHERE fa.resultado_status <> 'PENDENTE' AND a.data_prazo BETWEEN :dataInicio AND :dataFim AND f.codigo_area = :codigoArea
    """)
    Integer countConcluidasNoMesByArea(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, @Param("codigoArea") Integer codigoArea);

    @Query(nativeQuery = true, value = """
        SELECT COUNT(fa.*) FROM tb_cad_funcionario_avalicacao fa
        JOIN tb_cad_avaliacao a ON fa.codigo_avalicacao = a.codigo
        JOIN tb_cad_funcionario f ON fa.codigo_funcionario_avalidado = f.codigo
        WHERE fa.resultado_status = 'APROVADO' AND a.data_prazo BETWEEN :dataInicio AND :dataFim AND f.codigo_area = :codigoArea
    """)
    Integer countAprovadasNoMesByArea(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, @Param("codigoArea") Integer codigoArea);

    // --- NOVO (Veio do Git): Ranking Top 5 Competências ---
    // Atenção: Isso requer que a classe CompetenciaQuantidadeDTO tenha um construtor compatível
    @Query("""
        SELECT new br.com.AllTallent.dto.CompetenciaQuantidadeDTO(c.nome, COUNT(rc))
        FROM RespostaColaborador rc
        JOIN rc.pergunta p
        JOIN p.competencia c
        GROUP BY c.nome
        ORDER BY COUNT(rc) DESC
    """)
    List<CompetenciaQuantidadeDTO> findTopCompetenciasMaisAvaliadas(Pageable pageable);
}