package br.com.AllTallent.repository; // Ou o pacote de repositórios

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.RespostaColaborador;

@Repository
public interface RespostaColaboradorRepository extends JpaRepository<RespostaColaborador, Long> {

    // Método útil para buscar uma resposta existente para uma pergunta específica
    // dentro de uma instância de avaliação
    Optional<RespostaColaborador> findByFuncionarioAvaliacaoCodigoAndPerguntaCodigo(Long funcionarioAvaliacaoCodigo, Long perguntaCodigo);

    // Método útil para buscar todas as respostas de uma instância de avaliação
    List<RespostaColaborador> findByFuncionarioAvaliacaoCodigo(Long funcionarioAvaliacaoCodigo);
}