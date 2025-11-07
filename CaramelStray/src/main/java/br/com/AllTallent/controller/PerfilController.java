package br.com.AllTallent.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.AllTallent.model.Perfil;
import br.com.AllTallent.repository.PerfilRepository;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    private final PerfilRepository perfilRepository;

    public PerfilController(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    
    @PostMapping
    public ResponseEntity<Perfil> createPerfil(@RequestBody Perfil perfil) {
        Perfil novoPerfil = perfilRepository.save(perfil);
        return new ResponseEntity<>(novoPerfil, HttpStatus.CREATED);
    }

    
    @GetMapping
    public List<Perfil> getAllPerfis() {
        return perfilRepository.findAll();
    }
}