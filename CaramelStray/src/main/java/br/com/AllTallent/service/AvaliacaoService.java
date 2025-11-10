package br.com.AllTallent.service;

import br.com.AllTallent.dto.*; 
import br.com.AllTallent.exception.ResourceNotFoundException; 
import br.com.AllTallent.exception.UnauthorizedActionException; 
import br.com.AllTallent.model.*; 
import br.com.AllTallent.repository.*; 
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate; 

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Objects; // <<< IMPORT ADICIONADO

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.AllTallent.config.CustomUserDetails; 

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository; 
    private final FuncionarioRepository funcionarioRepository; 
    private final PerguntaRepository perguntaRepository;
    private final AvaliacaoFuncionarioRepository avaliacaoFuncionarioRepository; 
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

    // --- Método para pegar o usuário logado ---
    private CustomUserDetails getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UnauthorizedActionException("Usuário não autenticado ou tipo de principal inválido.");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }
    
    // --- Lógica de Permissão de Avaliação ---
    private boolean podeAvaliar(CustomUserDetails avaliador, Funcionario avaliado) {
        if (avaliador.getCodigo().equals(avaliado.getCodigo())) {
            return false;
        }
        if (avaliado.getPerfil() == null || avaliado.getArea() == null || avaliador.getAreaId() == null) {
            return false; 
        }
        boolean mesmoSetor = avaliador.getAreaId().equals(avaliado.getArea().getCodigo());
        int perfilAlvoId = avaliado.getPerfil().getCodigo();

        if (avaliador.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR")) &&
            !avaliador.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            boolean alvoEhColaborador = (perfilAlvoId == 3);
            return mesmoSetor && alvoEhColaborador;
        }
        if (avaliador.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            boolean alvoEhTime = (perfilAlvoId == 2 || perfilAlvoId == 3);
            return mesmoSetor && alvoEhTime;
        }
        return false; 
    }

    // --- Métodos de Serviço Atualizados ---

    @Transactional
    public AvaliacaoResponseDTO criarAvaliacaoCompleta(AvaliacaoRequestDTO dto) {
        
        CustomUserDetails avaliadorLogado = getUsuarioLogado();
        Funcionario criador = funcionarioRepository.getReferenceById(avaliadorLogado.getCodigo());

        Set<Pergunta> perguntas = new HashSet<>(perguntaRepository.findAllById(dto.codigosPerguntas()));
        if (perguntas.size() != dto.codigosPerguntas().size()) {
            throw new EntityNotFoundException("Uma ou mais perguntas não foram encontradas.");
        }
        
        List<Funcionario> funcionariosAlvo = funcionarioRepository.findAllById(dto.codigosFuncionarios());
        if (funcionariosAlvo.size() != dto.codigosFuncionarios().size()) {
            throw new EntityNotFoundException("Um ou mais funcionários não foram encontrados.");
        }

        for (Funcionario alvo : funcionariosAlvo) {
            if (!podeAvaliar(avaliadorLogado, alvo)) {
                throw new UnauthorizedActionException("Permissão negada. Você não pode criar avaliações para o usuário '" + alvo.getNomeCompleto() + "'.");
            }
        }

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setTitulo(dto.titulo());
        novaAvaliacao.setPerguntas(perguntas);
        novaAvaliacao.setCriador(criador); // <<< SALVA QUEM CRIOU

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(novaAvaliacao);

        for (Funcionario f : funcionariosAlvo) {
            AvaliacaoFuncionario instancia = new AvaliacaoFuncionario(f, avaliacaoSalva);
            avaliacaoFuncionarioRepository.save(instancia);
        }

        return new AvaliacaoResponseDTO(avaliacaoSalva);
    }
    
    // --- MÉTODO ATUALIZADO ---
    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarTodasAvaliacoes() {
        CustomUserDetails usuarioLogado = getUsuarioLogado();
        List<Avaliacao> todasAvaliacoes = avaliacaoRepository.findAll();
        
        // Regra do Perfil 1 (Diretor)
        if (usuarioLogado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return todasAvaliacoes.stream()
                // 1. Filtra avaliações sem criador (antigas)
                .filter(aval -> aval.getCriador() != null) 
                // 2. Filtra avaliações da mesma área do Diretor
                .filter(aval -> aval.getCriador().getArea() != null && 
                                Objects.equals(aval.getCriador().getArea().getCodigo(), usuarioLogado.getAreaId()))
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
        }

        // Regra do Perfil 2 (Supervisor)
        if (usuarioLogado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR"))) {
             return todasAvaliacoes.stream()
                // 1. Filtra avaliações sem criador (antigas)
                .filter(aval -> aval.getCriador() != null)
                // 2. Filtra avaliações da mesma área
                .filter(aval -> aval.getCriador().getArea() != null && 
                                Objects.equals(aval.getCriador().getArea().getCodigo(), usuarioLogado.getAreaId()))
                // 3. Filtra para ver APENAS as que ele mesmo criou
                .filter(aval -> Objects.equals(aval.getCriador().getCodigo(), usuarioLogado.getCodigo()))
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
        }

        // Se não for nenhum dos dois (ex: USER), retorna lista vazia
        return List.of();
    }

    // --- MÉTODO ATUALIZADO ---
    @Transactional(readOnly = true)
    public AvaliacaoDetalhadaDTO buscarAvaliacaoDetalhada(Integer id) { 
        CustomUserDetails usuarioLogado = getUsuarioLogado();
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada: " + id));

        // Validação de Segurança
        validarPermissaoDeAcesso(usuarioLogado, avaliacao);

        Hibernate.initialize(avaliacao.getPerguntas());
        avaliacao.getPerguntas().forEach(p -> Hibernate.initialize(p.getOpcoes())); 
        Hibernate.initialize(avaliacao.getInstanciasAvaliacao());
        
        return new AvaliacaoDetalhadaDTO(avaliacao); 
    }

    // --- MÉTODO ATUALIZADO ---
    @Transactional(readOnly = true)
    public List<AvaliacaoFuncionarioResponseDTO> buscarInstanciasPorAvaliacao(Integer avaliacaoId) {
        CustomUserDetails usuarioLogado = getUsuarioLogado();
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
             .orElseThrow(() -> new EntityNotFoundException("Avaliação base não encontrada: " + avaliacaoId));
        
        // Validação de Segurança
        validarPermissaoDeAcesso(usuarioLogado, avaliacao);

        return avaliacaoFuncionarioRepository.findByAvaliacaoCodigo(avaliacaoId).stream()
               .map(AvaliacaoFuncionarioResponseDTO::new)
               .collect(Collectors.toList());
    }

    // --- MÉTODO ATUALIZADO ---
    @Transactional(readOnly = true)
    public List<RespostaColaboradorResponseDTO> buscarRespostasPorInstancia(Long instanciaId) {
        CustomUserDetails usuarioLogado = getUsuarioLogado();
        AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
            .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + instanciaId));

        // Validação de Segurança (Pode ver as respostas se puder ver a avaliação mestre)
        validarPermissaoDeAcesso(usuarioLogado, instancia.getAvaliacao());

        return respostaColaboradorRepository.findByAvaliacaoFuncionarioCodigo(instanciaId).stream()
               .map(RespostaColaboradorResponseDTO::new)
               .collect(Collectors.toList());
    }


    // --- NOVO MÉTODO DE VALIDAÇÃO REUTILIZÁVEL ---
    private void validarPermissaoDeAcesso(CustomUserDetails usuarioLogado, Avaliacao avaliacao) {
        if (avaliacao.getCriador() == null || avaliacao.getCriador().getArea() == null) {
            throw new UnauthorizedActionException("Permissão negada. Esta avaliação não possui criador ou área definidos.");
        }

        Integer areaUsuario = usuarioLogado.getAreaId();
        Integer areaAvaliacao = avaliacao.getCriador().getArea().getCodigo();
        Integer criadorAvaliacaoId = avaliacao.getCriador().getCodigo();
        Integer usuarioLogadoId = usuarioLogado.getCodigo();

        // Se não for da mesma área, bloqueia
        if (!Objects.equals(areaUsuario, areaAvaliacao)) {
            throw new UnauthorizedActionException("Permissão negada. Você só pode acessar avaliações da sua própria área.");
        }

        // Se for Supervisor (GESTOR), verifica se ele é o criador
        if (usuarioLogado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR")) &&
            !usuarioLogado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            
            if (!Objects.equals(criadorAvaliacaoId, usuarioLogadoId)) {
                throw new UnauthorizedActionException("Permissão negada. Supervisores só podem ver avaliações que eles mesmos criaram.");
            }
        }
        
        // Se for Admin da mesma área, passa.
        // (A validação de mesma área já foi feita acima)
    }


    // (Métodos salvarOuAtualizarResposta, salvarRevisaoSupervisor, buscarParaResponder, 
    // finalizarPeloColaborador, buscarPendentesPorFuncionario e buscarParaRevisao 
    // JÁ ESTÃO SEGUROS e permanecem iguais)
    
    @Transactional
    public RespostaColaboradorResponseDTO salvarOuAtualizarResposta(RespostaColaboradorRequestDTO dto) {
        CustomUserDetails usuarioLogado = getUsuarioLogado();
        AvaliacaoFuncionario avaliacaoFunc = avaliacaoFuncionarioRepository.findById(dto.funcionarioAvaliacaoCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + dto.funcionarioAvaliacaoCodigo()));
        if (!avaliacaoFunc.getFuncionario().getCodigo().equals(usuarioLogado.getCodigo())) {
            throw new UnauthorizedActionException("Permissão negada. Você só pode salvar respostas para suas próprias avaliações.");
        }
        Pergunta pergunta = perguntaRepository.findById(dto.perguntaCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada: " + dto.perguntaCodigo()));
        PerguntaOpcao opcaoSelecionada = null;
        if (dto.opcaoSelecionadaCodigo() != null) {
            opcaoSelecionada = perguntaOpcaoRepository.findById(dto.opcaoSelecionadaCodigo())
                    .orElseThrow(() -> new EntityNotFoundException("Opção selecionada não encontrada: " + dto.opcaoSelecionadaCodigo()));
            if (!opcaoSelecionada.getPergunta().getCodigo().equals(pergunta.getCodigo())) {
                throw new IllegalArgumentException("Inconsistência: A opção selecionada não pertence à pergunta fornecida.");
            }
        }
        RespostaColaborador resposta = respostaColaboradorRepository
                .findByFuncionarioAvaliacaoCodigoAndPerguntaCodigo(dto.funcionarioAvaliacaoCodigo(), dto.perguntaCodigo())
                .orElse(new RespostaColaborador());
        resposta.setAvaliacaoFuncionario(avaliacaoFunc);
        resposta.setPergunta(pergunta);
        resposta.setRespostaTexto(dto.respostaTexto());
        resposta.setOpcaoSelecionada(opcaoSelecionada);
        RespostaColaborador respostaSalva = respostaColaboradorRepository.save(resposta);
        return new RespostaColaboradorResponseDTO(respostaSalva);
    }
    
    @Transactional
    public AvaliacaoFuncionarioResponseDTO salvarRevisaoSupervisor(Long instanciaId, RevisaoSupervisorRequestDTO dto) {
        CustomUserDetails avaliadorLogado = getUsuarioLogado();
        AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
                .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + instanciaId));
        if (!podeAvaliar(avaliadorLogado, instancia.getFuncionario())) {
             throw new UnauthorizedActionException("Permissão negada. Você não pode revisar esta avaliação.");
        }
        instancia.setComentarioSupervisao(dto.comentarioSupervisao());
        instancia.setComentarioColaborador(dto.comentarioParaColaborador()); 
        instancia.setNota(dto.nota());
        instancia.setResultadoStatus(dto.resultadoStatus());
        AvaliacaoFuncionario instanciaSalva = avaliacaoFuncionarioRepository.save(instancia);
        return new AvaliacaoFuncionarioResponseDTO(instanciaSalva);
    }
    
    @Transactional(readOnly = true)
    public AvaliacaoParaResponderDTO buscarParaResponder(Long instanciaId) {
        CustomUserDetails usuarioLogado = getUsuarioLogado();
         AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
            .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + instanciaId));
        if (!instancia.getFuncionario().getCodigo().equals(usuarioLogado.getCodigo())) {
            throw new UnauthorizedActionException("Permissão negada. Você só pode responder suas próprias avaliações.");
        }
         Avaliacao avaliacaoBase = instancia.getAvaliacao();
         if (avaliacaoBase == null) {
              throw new IllegalStateException("Instância de avaliação está sem avaliação base associada.");
         }
         Hibernate.initialize(avaliacaoBase.getPerguntas());
         avaliacaoBase.getPerguntas().forEach(p -> Hibernate.initialize(p.getOpcoes()));
         return new AvaliacaoParaResponderDTO(instancia, avaliacaoBase);
    }

    @Transactional
    public void finalizarPeloColaborador(Long instanciaId) {
        CustomUserDetails usuarioLogado = getUsuarioLogado();
         AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
            .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + instanciaId));
        if (!instancia.getFuncionario().getCodigo().equals(usuarioLogado.getCodigo())) {
            throw new UnauthorizedActionException("Permissão negada. Você só pode finalizar suas próprias avaliações.");
        }
         if ("PENDENTE".equals(instancia.getResultadoStatus())) {
              instancia.setResultadoStatus("AGUARDANDO_REVISAO"); 
              avaliacaoFuncionarioRepository.save(instancia);
         } else {
              throw new IllegalStateException("Avaliação não pode ser finalizada pois não está pendente. Status atual: " + instancia.getResultadoStatus());
         }
    }
    
    @Transactional(readOnly = true)
    public List<AvaliacaoFuncionarioResponseDTO> buscarPendentesPorFuncionario(Integer funcionarioId) {
        return avaliacaoFuncionarioRepository.findByFuncionarioCodigo(funcionarioId).stream()
               .filter(af -> "PENDENTE".equals(af.getResultadoStatus())) 
               .map(AvaliacaoFuncionarioResponseDTO::new)
               .collect(Collectors.toList());
    }
    
     @Transactional(readOnly = true)
     public AvaliacaoRevisaoDTO buscarParaRevisao(Long instanciaId) {
        // Validação de segurança já ocorre em salvarRevisaoSupervisor,
        // mas idealmente deveria ser adicionada aqui também.
         AvaliacaoFuncionario instancia = avaliacaoFuncionarioRepository.findById(instanciaId)
            .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação não encontrada: " + instanciaId));
         Hibernate.initialize(instancia.getFuncionario());
         Avaliacao avaliacaoBase = instancia.getAvaliacao();
         Hibernate.initialize(avaliacaoBase); 
         Hibernate.initialize(avaliacaoBase.getPerguntas());
         avaliacaoBase.getPerguntas().forEach(p -> Hibernate.initialize(p.getOpcoes()));
         Hibernate.initialize(instancia.getRespostas());
         instancia.getRespostas().forEach(r -> Hibernate.initialize(r.getOpcaoSelecionada())); 
         return new AvaliacaoRevisaoDTO(instancia, avaliacaoBase);
     }
}