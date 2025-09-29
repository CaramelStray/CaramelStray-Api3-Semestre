package br.com.AllTallent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.Funcionario;

// Usamos JpaRepository para mais funcionalidades, como paginação
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    // O Spring Data JPA cria todos os métodos CRUD para nós!
    @Query("SELECT f FROM Funcionario f " +
           "LEFT JOIN FETCH f.area " +
           "LEFT JOIN FETCH f.perfil " +
           "LEFT JOIN FETCH f.gestor " +
           "LEFT JOIN FETCH f.competencias " +
           "LEFT JOIN FETCH f.certificados " +
           "LEFT JOIN FETCH f.experiencias " + // <-- JOIN CORRETO AQUI
           "WHERE f.codigo = :id")
    Optional<Funcionario> findByIdCompleto(Integer id);
}

