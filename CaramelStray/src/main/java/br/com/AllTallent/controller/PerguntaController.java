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

    private final PerguntaService perguntaService; // Injetar o Service

    public PerguntaController(PerguntaService perguntaService) { // Construtor com o Service
        this.perguntaService = perguntaService;
    }

    @PostMapping
    public ResponseEntity<PerguntaResponseDTO> criarPergunta(@Valid @RequestBody PerguntaRequestDTO dto) {
        try {
            // Delega a criação para o Service
            PerguntaResponseDTO perguntaSalvaDTO = perguntaService.criarPergunta(dto);

            // Cria a URI
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(perguntaSalvaDTO.codigo())
                    .toUri();

            // Retorna 201 Created
            return ResponseEntity.created(location).body(perguntaSalvaDTO);
        } catch (EntityNotFoundException e) {
            // Se a competência não for encontrada no Service
            return ResponseEntity.badRequest().body(null); // Ou retornar uma mensagem de erro melhor
        }
        // Outros erros (como validação) serão tratados pelo Spring
    }

    @GetMapping
    public ResponseEntity<List<PerguntaResponseDTO>> listarTodasPerguntas() {
        // Delega a listagem para o Service
        return ResponseEntity.ok(perguntaService.listarTodas());
    }

     @GetMapping("/{id}")
     public ResponseEntity<PerguntaResponseDTO> buscarPerguntaPorId(@PathVariable Long id) {
         try {
             // Delega a busca para o Service
             return ResponseEntity.ok(perguntaService.buscarPorId(id));
         } catch (EntityNotFoundException e) {
             return ResponseEntity.notFound().build();
         }
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deletarPergunta(@PathVariable Long id) {
         try {
             // Delega a deleção para o Service
             perguntaService.deletarPergunta(id);
             return ResponseEntity.noContent().build();
         } catch (EntityNotFoundException e) {
             return ResponseEntity.notFound().build();
         }
     }
}