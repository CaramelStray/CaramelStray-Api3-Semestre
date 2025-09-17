package br.com.AllTallent.repository;

import br.com.AllTallent.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

// Usamos JpaRepository para mais funcionalidades, como paginação
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    // O Spring Data JPA cria todos os métodos CRUD para nós!
}
