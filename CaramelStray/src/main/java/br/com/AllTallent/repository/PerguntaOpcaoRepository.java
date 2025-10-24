package br.com.AllTallent.repository;

import br.com.AllTallent.model.PerguntaOpcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerguntaOpcaoRepository extends JpaRepository<PerguntaOpcao, Long> {
    // Métodos CRUD básicos são herdados
    // Pode adicionar métodos customizados se necessário depois
}