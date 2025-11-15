package br.com.AllTallent.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.AvaliacaoFuncionario;

// --- NOVAS IMPORTAÇÕES ---
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
// --- FIM DAS NOVAS IMPORTAÇÕES ---

@Repository
public interface AvaliacaoFuncionarioRepository extends JpaRepository<AvaliacaoFuncionario, Long> {
    
    Optional<AvaliacaoFuncionario> findByFuncionarioCodigoAndAvaliacaoCodigo(Integer funcionarioCodigo, Integer avaliacaoCodigo);
    List<AvaliacaoFuncionario> findByAvaliacaoCodigo(Integer avaliacaoCodigo);
    List<AvaliacaoFuncionario> findByFuncionarioCodigo(Integer funcionarioCodigo);

    // --- NOVAS QUERIES PARA OS CARDS DO DASHBOARD ---

    /**
     * Conta o total de avaliações com status PENDENTE em todo o sistema.
     * (Usado no Card "Pendências")
     */
    @Query(nativeQuery = true, value = """
        SELECT COUNT(*) 
        FROM tb_cad_funcionario_avalicacao 
        WHERE resultado_status = 'PENDENTE'
    """)
    Integer countTotalPendentes();

    /**
     * Conta avaliações CONCLUÍDAS (não pendentes) no mês, baseado na DATA PRAZO da avaliação.
     * (Usado no Card "Avaliações Concluídas")
     */
    @Query(nativeQuery = true, value = """
        SELECT COUNT(fa.*)
        FROM 
            tb_cad_funcionario_avalicacao fa
        JOIN 
            tb_cad_avaliacao a ON fa.codigo_avalicacao = a.codigo
        WHERE 
            fa.resultado_status <> 'PENDENTE'
            AND a.data_prazo BETWEEN :dataInicio AND :dataFim
    """)
    Integer countConcluidasNoMes(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    /**
     * Conta avaliações APROVADAS no mês, baseado na DATA PRAZO da avaliação.
     * (Usado para o cálculo da "Meta Mensal")
     */
    @Query(nativeQuery = true, value = """
        SELECT COUNT(fa.*)
        FROM 
            tb_cad_funcionario_avalicacao fa
        JOIN 
            tb_cad_avaliacao a ON fa.codigo_avalicacao = a.codigo
        WHERE 
            fa.resultado_status = 'APROVADO'
            AND a.data_prazo BETWEEN :dataInicio AND :dataFim
    """)
    Integer countAprovadasNoMes(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}