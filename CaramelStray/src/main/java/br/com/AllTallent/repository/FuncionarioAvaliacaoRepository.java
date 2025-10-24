package br.com.AllTallent.repository; // Ou o pacote de repositórios

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.FuncionarioAvaliacao;

@Repository
public interface FuncionarioAvaliacaoRepository extends JpaRepository<FuncionarioAvaliacao, Long> {

    // Método útil para encontrar a instância específica pelo funcionário e avaliação
    Optional<FuncionarioAvaliacao> findByFuncionarioAvaliadoCodigoAndAvaliacaoCodigo(Integer funcionarioCodigo, Integer avaliacaoCodigo);

}