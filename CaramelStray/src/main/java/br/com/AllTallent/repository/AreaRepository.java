package br.com.AllTallent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.AllTallent.model.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
    
}