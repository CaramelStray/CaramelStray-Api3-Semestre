package br.com.AllTallent.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.AllTallent.dto.CertificadoDTO;
import br.com.AllTallent.dto.CertificadoRequestDTO;
import br.com.AllTallent.dto.ExperienciaDTO;
import br.com.AllTallent.dto.ExperienciaRequestDTO;
import br.com.AllTallent.dto.FuncionarioCompetenciaUpdateDTO;
import br.com.AllTallent.dto.FuncionarioCompetenciasResponseDTO;
import br.com.AllTallent.dto.FuncionarioExperienciasResponseDTO;
import br.com.AllTallent.dto.FuncionarioPerfilDTO;
import br.com.AllTallent.dto.FuncionarioRequestDTO;
import br.com.AllTallent.dto.FuncionarioResponseDTO;
import br.com.AllTallent.exception.ResourceNotFoundException;
import br.com.AllTallent.exception.UnauthorizedActionException;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.service.FuncionarioService; 
import jakarta.validation.Valid; 

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/funcionario")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodos(@RequestParam(required = false) String texto) {
        return ResponseEntity.ok(funcionarioService.listarTodos(texto));
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR') or (principal.codigo == #id)")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> criar(@RequestBody FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO novoFuncionario = funcionarioService.criar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoFuncionario.codigo()).toUri();
        return ResponseEntity.created(uri).body(novoFuncionario);
    }

    @PutMapping("/{id}")
    @PreAuthorize("principal.codigo == #id")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable Integer id, @RequestBody FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO funcionarioAtualizado = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/perfil")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR') or (principal.codigo == #id)")
    public ResponseEntity<FuncionarioPerfilDTO> buscarPerfilPorId(@PathVariable Integer id) {
    FuncionarioPerfilDTO perfilDTO = funcionarioService.buscarPerfilPorId(id);
    return ResponseEntity.ok(perfilDTO);
    }
    
    @PostMapping("/{id}/certificados")
    @PreAuthorize("hasRole('ADMIN') or (principal.codigo == #id)")
    public ResponseEntity<CertificadoDTO> adicionarCertificado(
            @PathVariable Integer id,
            @RequestBody CertificadoRequestDTO dto) {

        CertificadoDTO novoCertificado = funcionarioService.adicionarCertificado(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCertificado);
    }

    @DeleteMapping("/certificados/{certificadoId}")
    @PreAuthorize("hasRole('ADMIN') or @funcionarioService.usuarioPodeRemoverCertificado(#certificadoId, principal.codigo)")
    public ResponseEntity<Void> removerCertificado(@PathVariable Integer certificadoId) {
    funcionarioService.removerCertificado(certificadoId);
    return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/competencias")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> atualizarCompetencias(
            @PathVariable Integer id,
            @RequestBody FuncionarioCompetenciaUpdateDTO dto) {

        try {
            funcionarioService.associarCompetencias( 
                id, 
                dto.codigosCompetencia()
            );
            return ResponseEntity.noContent().build(); 
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedActionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{id}/competencias")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR') or (principal.codigo == #id)")
    public ResponseEntity<FuncionarioCompetenciasResponseDTO> listarCompetenciasPorFuncionario(@PathVariable Integer id) {
        Funcionario funcionario = funcionarioService.buscarFuncionarioCompleto(id);
        
        return ResponseEntity.ok(new FuncionarioCompetenciasResponseDTO(funcionario));
    }

    @GetMapping("/{id}/experiencias")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR') or (principal.codigo == #id)")
    public ResponseEntity<FuncionarioExperienciasResponseDTO> listarExperienciasPorFuncionario(@PathVariable Integer id) {
        FuncionarioExperienciasResponseDTO experienciasDTO = funcionarioService.listarExperienciasPorFuncionario(id);
        return ResponseEntity.ok(experienciasDTO);
    }

    @PostMapping("/{id}/experiencias")
    @PreAuthorize("hasRole('ADMIN') or (principal.codigo == #id)")
    public ResponseEntity<ExperienciaDTO> adicionarExperiencia(
            @PathVariable Integer id,
            @Valid @RequestBody ExperienciaRequestDTO dto) {
        
        ExperienciaDTO novaExperiencia = funcionarioService.adicionarExperiencia(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaExperiencia);
    }


    @PutMapping("/experiencias/{experienciaId}")
    @PreAuthorize("hasRole('ADMIN') or @funcionarioService.usuarioPodeEditarExperiencia(#experienciaId, principal.codigo)")
    public ResponseEntity<ExperienciaDTO> atualizarExperiencia(
            @PathVariable Integer experienciaId,
            @Valid @RequestBody ExperienciaRequestDTO dto) {
        
        ExperienciaDTO experienciaAtualizada = funcionarioService.atualizarExperiencia(experienciaId, dto);
        return ResponseEntity.ok(experienciaAtualizada);
    }
}