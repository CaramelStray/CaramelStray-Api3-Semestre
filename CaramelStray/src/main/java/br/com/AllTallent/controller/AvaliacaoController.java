package br.com.AllTallent.controller;

// --- Imports ---
import br.com.AllTallent.dto.*; // Importar todos os DTOs necessários
import br.com.AllTallent.exception.ResourceNotFoundException; // Importar exceção customizada
import br.com.AllTallent.service.AvaliacaoService; // Importar o Service
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // Para validar DTOs de Request
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
// --- Fim Imports ---

@RestController
@RequestMapping("/api/avaliacoes") // Path base para avaliações
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService; // Injetar o Service

    // Injeção de dependência via construtor
    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    // --- Endpoint para CRIAR uma nova Avaliação e suas Instâncias ---
    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criarAvaliacao(
            @Valid @RequestBody AvaliacaoRequestDTO dto) {
        try {
            // Delega a criação completa para o Service
            AvaliacaoResponseDTO avaliacaoCriada = avaliacaoService.criarAvaliacaoCompleta(dto);

            // Cria a URI para o recurso recém-criado (Avaliacao base)
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest() // Base: /api/avaliacoes
                    .path("/{id}")        // Adiciona /id
                    .buildAndExpand(avaliacaoCriada.codigo()) // Substitui {id} pelo código
                    .toUri();

            // Retorna 201 Created com a URI e o DTO da avaliação base criada
            return ResponseEntity.created(location).body(avaliacaoCriada);

        } catch (EntityNotFoundException e) {
            // Se funcionários ou perguntas não foram encontrados no Service
            // Retorna 400 Bad Request com a mensagem de erro
            return ResponseEntity.badRequest().body(null); // Idealmente, retornar um DTO de erro com e.getMessage()
        } catch (Exception e) {
            // Outros erros inesperados
            // Logar o erro aqui é uma boa prática: log.error("Erro ao criar avaliação", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- Endpoint para LISTAR todas as Avaliações (visão geral) ---
    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarTodasAvaliacoes() {
        List<AvaliacaoResponseDTO> lista = avaliacaoService.listarTodasAvaliacoes();
        return ResponseEntity.ok(lista);
    }

    // --- Endpoint para BUSCAR detalhes de uma Avaliação específica (incluindo perguntas e instâncias) ---
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDetalhadaDTO> buscarAvaliacaoDetalhada(@PathVariable Integer id) {
        try {
            AvaliacaoDetalhadaDTO detalhadaDTO = avaliacaoService.buscarAvaliacaoDetalhada(id);
            return ResponseEntity.ok(detalhadaDTO);
        } catch (ResourceNotFoundException e) { // Usando sua exceção customizada
            return ResponseEntity.notFound().build();
        }
    }

    // --- Endpoint para LISTAR INSTÂNCIAS de uma Avaliação específica ---
    // Útil para o supervisor ver quem está fazendo/fez a avaliação X
    @GetMapping("/{id}/instancias")
    public ResponseEntity<List<AvaliacaoFuncionarioResponseDTO>> buscarInstanciasPorAvaliacao(@PathVariable Integer id) {
         try {
             // Validação da avaliação base pode ser feita aqui ou no service
             List<AvaliacaoFuncionarioResponseDTO> instancias = avaliacaoService.buscarInstanciasPorAvaliacao(id);
             return ResponseEntity.ok(instancias);
         } catch (EntityNotFoundException e) { // Se a avaliação base não existir
             return ResponseEntity.notFound().build();
         }
    }

    // --- Endpoint para SALVAR ou ATUALIZAR uma Resposta do Colaborador ---
    // Recebe UMA resposta por vez. O ID da instância vem no corpo.
    @PostMapping("/respostas") // Endpoint genérico para salvar respostas
    public ResponseEntity<?> salvarResposta( // Usando <?> para permitir retorno de erro customizado
            @Valid @RequestBody RespostaColaboradorRequestDTO respostaDTO) {
        try {
            RespostaColaboradorResponseDTO respostaSalva = avaliacaoService.salvarOuAtualizarResposta(respostaDTO);
            return ResponseEntity.ok(respostaSalva); // Retorna 200 OK com a resposta salva/atualizada
        } catch (EntityNotFoundException e) {
            // Se a instância, pergunta ou opção não for encontrada
            // Retorna 400 Bad Request com a mensagem de erro
            return ResponseEntity.badRequest().body("Erro ao salvar resposta: " + e.getMessage());
        } catch (IllegalArgumentException e) {
             // Se a opção não pertencer à pergunta
             return ResponseEntity.badRequest().body("Erro ao salvar resposta: " + e.getMessage());
        } catch (Exception e) {
            // Outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao salvar resposta.");
        }
    }

    // --- Endpoint para BUSCAR todas as Respostas de uma Instância específica ---
    // Útil para montar a tela de revisão do supervisor ou para o colaborador ver suas respostas
    @GetMapping("/instancias/{instanciaId}/respostas")
    public ResponseEntity<?> buscarRespostasPorInstancia(@PathVariable Long instanciaId) {
        try {
            List<RespostaColaboradorResponseDTO> respostas = avaliacaoService.buscarRespostasPorInstancia(instanciaId);
            return ResponseEntity.ok(respostas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Se a instância não for encontrada
        }
    }

    // --- Endpoint para o SUPERVISOR salvar a Revisão ---
    @PutMapping("/instancias/{instanciaId}/revisar") // Usa PUT pois está atualizando a instância
    public ResponseEntity<?> salvarRevisaoSupervisor(
            @PathVariable Long instanciaId,
            @Valid @RequestBody RevisaoSupervisorRequestDTO revisaoDTO) {
        try {
            AvaliacaoFuncionarioResponseDTO instanciaAtualizada = avaliacaoService.salvarRevisaoSupervisor(instanciaId, revisaoDTO);
            return ResponseEntity.ok(instanciaAtualizada); // Retorna 200 OK com a instância atualizada
        } catch (EntityNotFoundException e) {
            // Se a instância não for encontrada
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
             // Outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao salvar revisão.");
        }
    }

    // --- Endpoints Adicionais (Exemplos para o fluxo do Colaborador) ---

    /*
    // Endpoint para o colaborador ver suas avaliações pendentes
    @GetMapping("/pendentes/{funcionarioId}") // Recebe o ID do funcionário logado
    public ResponseEntity<List<AvaliacaoFuncionarioResponseDTO>> buscarAvaliacoesPendentes(@PathVariable Integer funcionarioId) {
        // Implementar lógica no AvaliacaoService para buscar AvaliacaoFuncionario
        // onde funcionarioCodigo = funcionarioId E resultadoStatus = 'PENDENTE' (ou similar)
        // List<AvaliacaoFuncionarioResponseDTO> pendentes = avaliacaoService.buscarPendentesPorFuncionario(funcionarioId);
        // return ResponseEntity.ok(pendentes);
        return ResponseEntity.ok(List.of()); // Placeholder
    }

    // Endpoint para o colaborador buscar os detalhes de uma avaliação para responder
    @GetMapping("/instancias/{instanciaId}/responder") // ID da AvaliacaoFuncionario
    public ResponseEntity<?> buscarAvaliacaoParaResponder(@PathVariable Long instanciaId) {
        try {
            // Implementar lógica no AvaliacaoService para montar o AvaliacaoParaResponderDTO
            // AvaliacaoParaResponderDTO dto = avaliacaoService.buscarParaResponder(instanciaId);
            // return ResponseEntity.ok(dto);
            return ResponseEntity.ok().build(); // Placeholder
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para o colaborador finalizar sua avaliação
    @PutMapping("/instancias/{instanciaId}/finalizar") // ID da AvaliacaoFuncionario
    public ResponseEntity<Void> finalizarAvaliacaoColaborador(@PathVariable Long instanciaId) {
        try {
             // Implementar lógica no AvaliacaoService para mudar o status da AvaliacaoFuncionario
             // para "AGUARDANDO_REVISAO" (ou similar)
             // avaliacaoService.finalizarPeloColaborador(instanciaId);
             return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
             return ResponseEntity.notFound().build();
        }
    }
    */

}