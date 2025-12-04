package br.com.AllTallent.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.dto.MesQuantidadeProjection; // Nossa interface
import br.com.AllTallent.dto.AreaQuantidadeDTO;       // Novo do Git
import br.com.AllTallent.dto.CompetenciaQuantidadeDTO; // Novo do Git

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    
    @Query("SELECT f FROM Funcionario f LEFT JOIN FETCH f.area LEFT JOIN FETCH f.perfil WHERE f.codigo = :id")
    Optional<Funcionario> findByIdCompleto(@Param("id") Integer id); 

    Optional<Funcionario> findByEmail(String email);

    @Query("SELECT f FROM Funcionario f LEFT JOIN FETCH f.perfil LEFT JOIN FETCH f.area WHERE f.email = :email")
    Optional<Funcionario> findByEmailForSecurity(@Param("email") String email);

    @Query(nativeQuery = true, value = """
        SELECT TO_CHAR(DATE_TRUNC('month', data_admissao), 'YYYY-MM') AS mes, COUNT(*) AS quantidade
        FROM tb_cad_funcionario
        WHERE data_admissao IS NOT NULL
        GROUP BY DATE_TRUNC('month', data_admissao)
        ORDER BY mes
    """)
    List<MesQuantidadeProjection> findEvolucaoMensal();

    @Query(nativeQuery = true, value = """
        SELECT TO_CHAR(DATE_TRUNC('month', data_admissao), 'YYYY-MM') AS mes, COUNT(*) AS quantidade
        FROM tb_cad_funcionario
        WHERE data_admissao IS NOT NULL AND codigo_area = :codigoArea
        GROUP BY DATE_TRUNC('month', data_admissao)
        ORDER BY mes
    """)
    List<MesQuantidadeProjection> findEvolucaoMensalByArea(@Param("codigoArea") Integer codigoArea);

    long countByAreaCodigo(Integer codigoArea);
    
    @Query("""
        SELECT new br.com.AllTallent.dto.AreaQuantidadeDTO(COALESCE(a.nome, 'Sem área'), COUNT(f))
        FROM Funcionario f
        LEFT JOIN f.area a
        GROUP BY a.nome
        ORDER BY a.nome
    """)
    List<AreaQuantidadeDTO> countFuncionariosPorArea();

    @Query("""
        SELECT new br.com.AllTallent.dto.CompetenciaQuantidadeDTO(c.nome, COUNT(c))
        FROM Funcionario f
        JOIN f.competencias c
        GROUP BY c.nome
        ORDER BY COUNT(c) DESC
    """)
    List<CompetenciaQuantidadeDTO> countFuncionariosPorCompetencia();

    @Query("""
    SELECT DISTINCT f
    FROM Funcionario f
    LEFT JOIN f.area a
    LEFT JOIN f.perfil p
    LEFT JOIN f.competencias c
    WHERE
        LOWER(f.nomeCompleto) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(f.email) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :texto, '%'))
""")
    List<Funcionario> buscarPorTexto(@Param("texto") String texto);

}