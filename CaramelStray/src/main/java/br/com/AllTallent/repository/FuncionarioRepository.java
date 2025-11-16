package br.com.AllTallent.repository;

import java.util.Optional;

import br.com.AllTallent.dto.AreaQuantidadeDTO;
import br.com.AllTallent.dto.CompetenciaQuantidadeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.Funcionario;

// --- NOVAS IMPORTAÇÕES ---
import br.com.AllTallent.dto.MesQuantidadeDTO;
import java.util.List;
// --- FIM DAS NOVAS IMPORTAÇÕES ---

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    
    @Query("SELECT f FROM Funcionario f " +
           "LEFT JOIN FETCH f.area " +
           "LEFT JOIN FETCH f.perfil " +
           "LEFT JOIN FETCH f.gestor " +
           "LEFT JOIN FETCH f.competencias " +
           "LEFT JOIN FETCH f.certificados " +
           "LEFT JOIN FETCH f.experiencias " +
           "WHERE f.codigo = :id")
    Optional<Funcionario> findByIdCompleto(@Param("id") Integer id); 

    Optional<Funcionario> findByEmail(String email);

    @Query("SELECT f FROM Funcionario f " +
           "LEFT JOIN FETCH f.perfil " +
           "LEFT JOIN FETCH f.area " + 
           "WHERE f.email = :email")
    Optional<Funcionario> findByEmailForSecurity(@Param("email") String email);

    @Query(value = """
        SELECT 
            TO_CHAR(f.data_admissao, 'MM/YYYY') AS mes,
            COUNT(*) AS quantidade
        FROM tb_cad_funcionario f
        WHERE f.data_admissao IS NOT NULL
        GROUP BY TO_CHAR(f.data_admissao, 'MM/YYYY')
        ORDER BY MIN(f.data_admissao)
        """,
            nativeQuery = true)
    List<MesQuantidadeDTO> findEvolucaoMensal();

    //consulta para o grafico de total de colaboradores por área
    @Query("""
        SELECT COALESCE(a.nome, 'Sem área'), COUNT(f)
        FROM Funcionario f
        LEFT JOIN f.area a
        GROUP BY a.nome
        ORDER BY a.nome
    """)
    List<AreaQuantidadeDTO> countFuncionariosPorArea();


    //consulta para o grafico de colaboradores por competência
    @Query("""
        SELECT c.nome, COUNT(c)
        FROM Funcionario f
        JOIN f.competencias c
        GROUP BY c.nome
        ORDER BY COUNT(c) DESC
    """)
    List<CompetenciaQuantidadeDTO> countFuncionariosPorCompetencia();




}