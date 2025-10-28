package br.com.AllTallent.repository; // Ou o pacote onde seus repositórios estão

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.Pergunta;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    // Método opcional: Pode ser útil para buscar perguntas por competência
    List<Pergunta> findByCompetenciaCodigo(Integer competenciaCodigo);

    // Método opcional: Verificar se já existe uma pergunta com o mesmo texto (evitar duplicatas)
    boolean existsByPerguntaIgnoreCase(String textoPergunta);
}