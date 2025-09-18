package br.com.AllTallent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    // O JpaRepository já nos fornece os métodos básicos de CRUD (Create, Read, Update, Delete)
    // O <Perfil, Integer> indica que este repositório gerencia a entidade Perfil,
    // cuja chave primária (@Id) é do tipo Integer.
}