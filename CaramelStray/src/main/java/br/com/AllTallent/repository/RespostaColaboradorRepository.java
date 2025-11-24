package br.com.AllTallent.repository; // Ou o pacote de repositórios

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.RespostaColaborador;

@Repository
public interface RespostaColaboradorRepository extends JpaRepository<RespostaColaborador, Long> {

    @Query("SELECT r FROM RespostaColaborador r WHERE r.avaliacaoFuncionario.codigo = :funcionarioAvaliacaoCodigo AND r.pergunta.codigo = :perguntaCodigo")
    Optional<RespostaColaborador> findByFuncionarioAvaliacaoCodigoAndPerguntaCodigo(
            @Param("funcionarioAvaliacaoCodigo") Long funcionarioAvaliacaoCodigo,
            @Param("perguntaCodigo") Long perguntaCodigo
    );

    List<RespostaColaborador> findByAvaliacaoFuncionarioCodigo(Long avaliacaoFuncionarioCodigo);
}