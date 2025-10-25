package br.com.AllTallent.service;

// --- Imports ---
import br.com.AllTallent.dto.*; // Importar todos os DTOs do pacote
import br.com.AllTallent.exception.ResourceNotFoundException; // Sua exceção customizada
import br.com.AllTallent.model.*; // Importar todos os Modelos do pacote
import br.com.AllTallent.repository.*; // Importar todos os Repositórios do pacote
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate; // Import para inicializar coleções LAZY

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.AllTallent.repository.AvaliacaoRepository;
import br.com.AllTallent.dto.AvaliacaoDetalhadaDTO;

// --- Fim Imports ---

@Service
public class AvaliacaoService {

    // --- Injeção de Dependências ---
    private final AvaliacaoRepository avaliacaoRepository; // Para Avaliacao2
    private final FuncionarioRepository funcionarioRepository;
    private final PerguntaRepository perguntaRepository;
    private final AvaliacaoFuncionarioRepository avaliacaoFuncionarioRepository; // Para AvaliacaoFuncionario
    private final RespostaColaboradorRepository respostaColaboradorRepository;
    private final PerguntaOpcaoRepository perguntaOpcaoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                            FuncionarioRepository funcionarioRepository,
                            PerguntaRepository perguntaRepository,
                            AvaliacaoFuncionarioRepository avaliacaoFuncionarioRepository,
                            RespostaColaboradorRepository respostaColaboradorRepository,
                            PerguntaOpcaoRepository perguntaOpcaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.perguntaRepository = perguntaRepository;
        this.avaliacaoFuncionarioRepository = avaliacaoFuncionarioRepository;
        this.respostaColaboradorRepository = respostaColaboradorRepository;
        this.perguntaOpcaoRepository = perguntaOpcaoRepository;
    }
    // --- Fim Injeção ---

    // --- Método para Criar Avaliação e Instâncias ---
    @Transactional
    public AvaliacaoResponseDTO criarAvaliacaoCompleta(AvaliacaoRequestDTO dto) {

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setTitulo(dto.titulo());
        // Assumindo que AvaliacaoRequestDTO PODE ter dataPrazo (opcional)
        // if (dto.dataPrazo() != null) {
        //     novaAvaliacao.setDataPrazo(dto.dataPrazo());
        // }

        Set<Pergunta> perguntas = new HashSet<>(perguntaRepository.findAllById(dto.codigosPerguntas()));
        if (perguntas.size() != dto.codigosPerguntas().size()) {
            Set<Long> idsEncontrados = perguntas.stream().map(Pergunta::getCodigo).collect(Collectors.toSet());
            List<Long> idsFaltando = dto.codigosPerguntas().stream().filter(id -> !idsEncontrados.contains(id)).collect(Collectors.toList());
            throw new EntityNotFoundException("Uma ou mais perguntas não foram encontradas. IDs não encontrados: " + idsFaltando);
        }
        novaAvaliacao.setPerguntas(perguntas);

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(novaAvaliacao);

        List<Funcionario> funcionarios = funcionarioRepository.findAllById(dto.codigosFuncionarios());
        if (funcionarios.size() != dto.codigosFuncionarios().size()) {
            Set<Integer> idsEncontrados = funcionarios.stream().map(Funcionario::getCodigo).collect(Collectors.toSet());
            List<Integer> idsFaltando = dto.codigosFuncionarios().stream().filter(id -> !idsEncontrados.contains(id)).collect(Collectors.toList());
            throw new EntityNotFoundException("Um ou mais funcionários não foram encontrados. IDs não encontrados: " + idsFaltando);
        }

        for (Funcionario f : funcionarios) {
            AvaliacaoFuncionario instancia = new AvaliacaoFuncionario(f, avaliacaoSalva);
            avaliacaoFuncionarioRepository.save(instancia);
        }

        return new AvaliacaoResponseDTO(avaliacaoSalva); // Usa o DTO que você confirmou ter
    }


    // --- Método para Salvar/Atualizar Resposta de um Colaborador ---
    @Transactional
    public RespostaColaboradorResponseDTO salvarOuAtualizarResposta(RespostaColaboradorRequestDTO dto) {

        AvaliacaoFuncionario avaliacaoFunc = avaliacaoFuncionarioRepository.findById(dto.funcionarioAvaliacaoCodigo()) // Nome do campo correto no DTO
                .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação (AvaliacaoFuncionario) não encontrada com ID: " + dto.funcionarioAvaliacaoCodigo())); // Nome do campo correto no DTO

        Pergunta pergunta = perguntaRepository.findById(dto.perguntaCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada com ID: " + dto.perguntaCodigo()));

        PerguntaOpcao opcaoSelecionada = null;
        if (dto.opcaoSelecionadaCodigo() != null) {
            opcaoSelecionada = perguntaOpcaoRepository.findById(dto.opcaoSelecionadaCodigo())
                    .orElseThrow(() -> new EntityNotFoundException("Opção selecionada não encontrada com ID: " + dto.opcaoSelecionadaCodigo()));
            if (!opcaoSelecionada.getPergunta().getCodigo().equals(pergunta.getCodigo())) {
                throw new IllegalArgumentException("Inconsistência: A opção selecionada (ID " + dto.opcaoSelecionadaCodigo()
                        + ") não pertence à pergunta fornecida (ID " + dto.perguntaCodigo() + ").");
            }
        }

        RespostaColaborador resposta = respostaColaboradorRepository
                .findByFuncionarioAvaliacaoCodigoAndPerguntaCodigo(dto.funcionarioAvaliacaoCodigo(), dto.perguntaCodigo()) // Nome do campo correto no DTO
                .orElse(new RespostaColaborador());

        resposta.setAvaliacaoFuncionario(avaliacaoFunc);
        resposta.setPergunta(pergunta);
        resposta.setRespostaTexto(dto.respostaTexto());
        resposta.setOpcaoSelecionada(opcaoSelecionada);

        RespostaColaborador respostaSalva = respostaColaboradorRepository.save(resposta);

        return new RespostaColaboradorResponseDTO(respostaSalva);
    }

    // --- Métodos para Buscar Dados ---

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarTodasAvaliacoes() {
        return avaliacaoRepository.findAll().stream()
               .map(AvaliacaoResponseDTO::new) // Usa o DTO que você confirmou ter
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AvaliacaoDetalhadaDTO buscarAvaliacaoDetalhada(Integer id) { // Precisa criar AvaliacaoDetalhadaDTO
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada: " + id));

        // Força a inicialização das coleções LAZY
        Hibernate.initialize(avaliacao.getPerguntas());
        avaliacao.getPerguntas().forEach(p -> Hibernate.initialize(p.getOpcoes())); // Carrega opções das perguntas
        Hibernate.initialize(avaliacao.getInstanciasAvaliacao());
        // Opcional: Carregar mais detalhes das instâncias, se necessário no DTO
        // avaliacao.getInstanciasAvaliacao().forEach(i -> Hibernate.initialize(i.getFuncionario()));

        return new AvaliacaoDetalhadaDTO(avaliacao); // Crie este DTO
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoFuncionarioResponseDTO> buscarInstanciasPorAvaliacao(Integer avaliacaoId) {
        if (!avaliacaoRepository.existsById(avaliacaoId)) {
             throw new EntityNotFoundException("Avaliação base não encontrada: " + avaliacaoId);
        }
        return avaliacaoFuncionarioRepository.findByAvaliacaoCodigo(avaliacaoId).stream()
               .map(AvaliacaoFuncionarioResponseDTO::new)
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RespostaColaboradorResponseDTO> buscarRespostasPorInstancia(Long instanciaId) {
        if (!avaliacaoFuncionarioRepository.existsById(instanciaId)) {
             throw new EntityNotFoundException("Instância de avaliação (AvaliacaoFuncionario) não encontrada: " + instanciaId);
        }
        return respostaColaboradorRepository.findByAvaliacaoFuncionarioCodigo(instanciaId).stream() // Nome do método correto do repo
                .map(RespostaColaboradorResponseDTO::new)
                .collect(Collectors.toList());
    }

    // --- Método para Salvar a Revisão do Supervisor ---
    @Transactional
    public AvaliacaoFuncionarioResponseDTO salvarRevisaoSupervisor(Long instanciaId, RevisaoSupervisorRequestDTO dto) {
        AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
            .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação (AvaliacaoFuncionario) não encontrada: " + instanciaId));

        // Adicionar validação de permissão aqui (quem pode revisar?)

        instancia.setComentarioSupervisao(dto.comentarioSupervisao());
        // Se decidir ter um campo separado para feedback público:
        // instancia.setComentarioParaColaborador(dto.comentarioParaColaborador());
        instancia.setNota(dto.nota());
        instancia.setResultadoStatus(dto.resultadoStatus());

        AvaliacaoFuncionario instanciaSalva = avaliacaoFuncionarioRepository.save(instancia);

        return new AvaliacaoFuncionarioResponseDTO(instanciaSalva);
    }

    // --- Métodos Adicionais (Esboços) ---

    // Busca avaliações pendentes para um funcionário específico
    @Transactional(readOnly = true)
    public List<AvaliacaoFuncionarioResponseDTO> buscarPendentesPorFuncionario(Integer funcionarioId) {
        return avaliacaoFuncionarioRepository.findByFuncionarioCodigo(funcionarioId).stream()
               // Filtrar por status que indicam pendência (ex: "PENDENTE", "RASCUNHO_COLABORADOR")
               .filter(af -> "PENDENTE".equals(af.getResultadoStatus())) // Ajuste a lógica do status
               .map(AvaliacaoFuncionarioResponseDTO::new)
               .collect(Collectors.toList());
    }

    // Busca os dados completos para um colaborador responder uma avaliação específica
    @Transactional(readOnly = true)
    public AvaliacaoParaResponderDTO buscarParaResponder(Long instanciaId) {
         AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
            .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + instanciaId));

         // Garante que a avaliação base e suas perguntas/opções sejam carregadas
         Avaliacao avaliacaoBase = instancia.getAvaliacao();
         if (avaliacaoBase == null) {
              throw new IllegalStateException("Instância de avaliação está sem avaliação base associada.");
         }
         Hibernate.initialize(avaliacaoBase.getPerguntas());
         avaliacaoBase.getPerguntas().forEach(p -> Hibernate.initialize(p.getOpcoes()));

         return new AvaliacaoParaResponderDTO(instancia, avaliacaoBase);
    }

    // Marca uma avaliação como finalizada pelo colaborador
    @Transactional
    public void finalizarPeloColaborador(Long instanciaId) {
         AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
            .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + instanciaId));

         // Adicionar validação: Só pode finalizar se estiver 'PENDENTE'?
         if ("PENDENTE".equals(instancia.getResultadoStatus())) {
             instancia.setResultadoStatus("AGUARDANDO_REVISAO"); // Novo status
             // Poderia adicionar um campo dataFinalizacaoColaborador aqui
             avaliacaoFuncionarioRepository.save(instancia);
         } else {
             // Lançar exceção ou logar aviso se tentar finalizar algo já finalizado/revisado
             throw new IllegalStateException("Avaliação não pode ser finalizada pois não está pendente. Status atual: " + instancia.getResultadoStatus());
         }
    }

     // Busca os dados completos para um supervisor revisar uma avaliação específica
     @Transactional(readOnly = true)
     public AvaliacaoRevisaoDTO buscarParaRevisao(Long instanciaId) {
          AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
             .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + instanciaId));

          // Garante que tudo necessário para o DTO seja carregado
          Hibernate.initialize(instancia.getFuncionario());
          Avaliacao avaliacaoBase = instancia.getAvaliacao();
          Hibernate.initialize(avaliacaoBase); // Carrega dados da avaliação base
          Hibernate.initialize(avaliacaoBase.getPerguntas());
          avaliacaoBase.getPerguntas().forEach(p -> Hibernate.initialize(p.getOpcoes()));
          Hibernate.initialize(instancia.getRespostas());
          instancia.getRespostas().forEach(r -> Hibernate.initialize(r.getOpcaoSelecionada())); // Carrega a opção selecionada

          return new AvaliacaoRevisaoDTO(instancia, avaliacaoBase);
     }
}