package br.com.AllTallent.controller;

import br.com.AllTallent.model.Competencia;
import br.com.AllTallent.repository.CompetenciaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/competencias")
public class CompetenciaController {

    private final CompetenciaRepository competenciaRepository;

    public CompetenciaController(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository;
    }

    // ✅ Listar todas as competências
    @GetMapping
    public ResponseEntity<List<Competencia>> listar() {
        return ResponseEntity.ok(competenciaRepository.findAll());
    }

    // ✅ Buscar uma competência por ID
    @GetMapping("/{id}")
    public ResponseEntity<Competencia> buscarPorId(@PathVariable Integer id) {
        return competenciaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Criar uma nova competência
    @PostMapping
    public ResponseEntity<Competencia> criar(@RequestBody Competencia nova) {
        if (competenciaRepository.existsByNomeIgnoreCase(nova.getNome())) {
            return ResponseEntity.badRequest().build();
        }
        Competencia salva = competenciaRepository.save(nova);
        return ResponseEntity.created(URI.create("/api/competencias/" + salva.getCodigo())).body(salva);
    }

    // ✅ Atualizar competência existente
    @PutMapping("/{id}")
    public ResponseEntity<Competencia> atualizar(@PathVariable Integer id, @RequestBody Competencia atualizada) {
        return competenciaRepository.findById(id)
                .map(c -> {
                    c.setNome(atualizada.getNome());
                    Competencia salva = competenciaRepository.save(c);
                    return ResponseEntity.ok(salva);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Deletar competência
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!competenciaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        competenciaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
