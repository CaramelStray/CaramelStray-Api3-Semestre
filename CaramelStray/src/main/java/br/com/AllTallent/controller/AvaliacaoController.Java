package br.com.AllTallent.controller;

// ... (imports de Avaliacao, Funcionario, AvaliacaoRepository, FuncionarioRepository)
import br.com.AllTallent.dto.AvaliacaoRequestDTO;
import br.com.AllTallent.model.Pergunta; // IMPORTAR
import br.com.AllTallent.repository.PerguntaRepository; // IMPORTAR

import java.util.HashSet;
// ...

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoRepository avaliacaoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PerguntaRepository perguntaRepository; // MUDADO

    public AvaliacaoController(AvaliacaoRepository avaliacaoRepository, 
                               FuncionarioRepository funcionarioRepository, 
                               PerguntaRepository perguntaRepository) { // MUDADO
        this.avaliacaoRepository = avaliacaoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.perguntaRepository = perguntaRepository; // MUDADO
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criarAvaliacao(@RequestBody AvaliacaoRequestDTO dto) {
        try {
            Avaliacao novaAvaliacao = new Avaliacao();
            novaAvaliacao.setTitulo(dto.titulo());

            Set<Funcionario> funcionarios = new HashSet<>(funcionarioRepository.findAllById(dto.codigosFuncionarios()));
            novaAvaliacao.setFuncionariosAvaliados(funcionarios);

            // MUDADO: Buscar Perguntas em vez de Competências
            Set<Pergunta> perguntas = new HashSet<>(perguntaRepository.findAllById(dto.codigosPerguntas()));
            novaAvaliacao.setPerguntas(perguntas);
            
            Avaliacao avaliacaoSalva = avaliacaoRepository.save(novaAvaliacao);
            return new ResponseEntity<>(avaliacaoSalva, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<Avaliacao> listarTodasAvaliacoes() {
        return avaliacaoRepository.findAll();
    }
}