package br.com.AllTallent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.FuncionarioCertificado;

@Repository
public interface CertificadoRepository extends JpaRepository<FuncionarioCertificado, Integer> {
}