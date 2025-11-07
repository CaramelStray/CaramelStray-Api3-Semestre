package br.com.AllTallent.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity; // Importar o Service
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.AllTallent.dto.PerguntaRequestDTO;
import br.com.AllTallent.dto.PerguntaResponseDTO;
import br.com.AllTallent.service.PerguntaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/perguntas")
public class PerguntaController {

    private final PerguntaService perguntaService; 

    public PerguntaController(PerguntaService perguntaService) { 
        this.perguntaService = perguntaService;
    }

    @PostMapping
    public ResponseEntity<PerguntaResponseDTO> criarPergunta(@Valid @RequestBody PerguntaRequestDTO dto) {
        try {
            
            PerguntaResponseDTO perguntaSalvaDTO = perguntaService.criarPergunta(dto);

            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(perguntaSalvaDTO.codigo())
                    .toUri();

            
            return ResponseEntity.created(location).body(perguntaSalvaDTO);
        } catch (EntityNotFoundException e) {
            
            return ResponseEntity.badRequest().body(null); 
        }
    }

    @GetMapping
    public ResponseEntity<List<PerguntaResponseDTO>> listarTodasPerguntas() {
        return ResponseEntity.ok(perguntaService.listarTodas());
    }

     @GetMapping("/{id}")
     public ResponseEntity<PerguntaResponseDTO> buscarPerguntaPorId(@PathVariable Long id) {
         try {
             return ResponseEntity.ok(perguntaService.buscarPorId(id));
         } catch (EntityNotFoundException e) {
             return ResponseEntity.notFound().build();
         }
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deletarPergunta(@PathVariable Long id) {
         try {
             perguntaService.deletarPergunta(id);
             return ResponseEntity.noContent().build();
         } catch (EntityNotFoundException e) {
             return ResponseEntity.notFound().build();
         }
     }
}