package br.com.AllTallent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
   
}