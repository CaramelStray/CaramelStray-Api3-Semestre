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
@RequestMapping("/api/perfis") // Define o endereço base para este controller
public class PerfilController {

    private final PerfilRepository perfilRepository;

    // Injeção de dependência do repositório
    public PerfilController(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    // Endpoint para criar um novo Perfil (ex: POST http://localhost:8080/api/perfis)
    @PostMapping
    public ResponseEntity<Perfil> createPerfil(@RequestBody Perfil perfil) {
        Perfil novoPerfil = perfilRepository.save(perfil);
        return new ResponseEntity<>(novoPerfil, HttpStatus.CREATED);
    }

    // Endpoint para listar todos os Perfis (ex: GET http://localhost:8080/api/perfis)
    @GetMapping
    public List<Perfil> getAllPerfis() {
        return perfilRepository.findAll();
    }
}