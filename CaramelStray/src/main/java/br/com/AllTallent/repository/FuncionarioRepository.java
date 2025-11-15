package br.com.AllTallent.repository;

import java.util.Optional;
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

    @Query(nativeQuery = true, name = "FuncionarioRepository.findEvolucaoMensal")
    List<MesQuantidadeDTO> findEvolucaoMensal();
}