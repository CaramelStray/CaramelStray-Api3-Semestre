package br.com.AllTallent.repository; // Ou o pacote de repositórios

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.AvaliacaoFuncionario;

@Repository
public interface AvaliacaoFuncionarioRepository extends JpaRepository<AvaliacaoFuncionario, Long> {
    Optional<AvaliacaoFuncionario> findByFuncionarioCodigoAndAvaliacaoCodigo(Integer funcionarioCodigo, Integer avaliacaoCodigo);
    List<AvaliacaoFuncionario> findByAvaliacaoCodigo(Integer avaliacaoCodigo);
    List<AvaliacaoFuncionario> findByFuncionarioCodigo(Integer funcionarioCodigo);
}