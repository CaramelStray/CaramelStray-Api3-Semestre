package br.com.AllTallent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.Experiencia;

@Repository
public interface ExperienciaRepository extends JpaRepository<Experiencia, Integer> {
}