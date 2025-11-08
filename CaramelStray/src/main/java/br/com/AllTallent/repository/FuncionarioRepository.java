package br.com.AllTallent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    // Método para buscar pelo email (essencial para o login)
    Optional<Funcionario> findByEmail(String email);

    // Seu método que busca tudo
    @Query("SELECT f FROM Funcionario f " +
           "LEFT JOIN FETCH f.area " +
           "LEFT JOIN FETCH f.perfil " +
           "LEFT JOIN FETCH f.gestor " +
           "LEFT JOIN FETCH f.competencias " +
           "LEFT JOIN FETCH f.certificados " +
           "LEFT JOIN FETCH f.experiencias " +
           "WHERE f.codigo = :id")
    Optional<Funcionario> findByIdCompleto(Integer id);
}