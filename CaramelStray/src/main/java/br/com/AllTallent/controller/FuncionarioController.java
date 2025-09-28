package br.com.AllTallent.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus; // <- Importante
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.AllTallent.dto.CertificadoDTO;
import br.com.AllTallent.dto.CertificadoRequestDTO;
import br.com.AllTallent.dto.FuncionarioCompetenciaUpdateDTO;
import br.com.AllTallent.dto.FuncionarioCompetenciasResponseDTO;
import br.com.AllTallent.dto.FuncionarioPerfilDTO;
import br.com.AllTallent.dto.FuncionarioRequestDTO;
import br.com.AllTallent.dto.FuncionarioResponseDTO;
import br.com.AllTallent.exception.ResourceNotFoundException;
import br.com.AllTallent.exception.UnauthorizedActionException;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.service.FuncionarioService; 
@RestController
@RequestMapping("/api/funcionario")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    // ✅ CORRETO: Retorna uma lista de DTOs
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(funcionarioService.listarTodos());
    }

    // ✅ CORRETO: Retorna um DTO
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

    // ✅ CORRETO: Recebe um DTO de requisição e retorna um DTO de resposta
    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> criar(@RequestBody FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO novoFuncionario = funcionarioService.criar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoFuncionario.codigo()).toUri();
        return ResponseEntity.created(uri).body(novoFuncionario);
    }

    // ✅ CORRETO: Recebe um DTO de requisição e retorna um DTO de resposta
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable Integer id, @RequestBody FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO funcionarioAtualizado = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/perfil")
    public ResponseEntity<FuncionarioPerfilDTO> buscarPerfilPorId(@PathVariable Integer id) {
    FuncionarioPerfilDTO perfilDTO = funcionarioService.buscarPerfilPorId(id);
    return ResponseEntity.ok(perfilDTO);
    }
    @PostMapping("/{Id}/certificados")
    public ResponseEntity<CertificadoDTO> adicionarCertificado(
            @PathVariable Integer funcionarioId,
            @RequestBody CertificadoRequestDTO dto) {

        CertificadoDTO novoCertificado = funcionarioService.adicionarCertificado(funcionarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCertificado);
    }
    @PutMapping("/{id}/competencias")
    public ResponseEntity<Void> atualizarCompetencias(
            // Recebe o ID do usuário que está logado
            @RequestHeader("X-Usuario-Logado-Id") Integer idUsuarioLogado, 
            // Recebe o ID do funcionário que será alterado
            @PathVariable Integer id,
            @RequestBody FuncionarioCompetenciaUpdateDTO dto) {

        try {
            funcionarioService.associarCompetencias(
                idUsuarioLogado, 
                id, 
                dto.codigosCompetencia()
            );
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (UnauthorizedActionException e) {
            // Lançamos a exceção. O Spring vai mapear para 403 Forbidden
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @GetMapping("/{id}/competencias")
    public ResponseEntity<FuncionarioCompetenciasResponseDTO> listarCompetenciasPorFuncionario(@PathVariable Integer id) {
        // Usa o service para buscar o funcionário completo
        Funcionario funcionario = funcionarioService.buscarFuncionarioCompleto(id);
        
        // Retorna apenas as competências no formato DTO
        return ResponseEntity.ok(new FuncionarioCompetenciasResponseDTO(funcionario));
    }
}