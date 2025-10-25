package br.com.AllTallent.service;

// --- Imports ---
import br.com.AllTallent.dto.*; // Importar todos os DTOs do pacote
import br.com.AllTallent.exception.ResourceNotFoundException; // Sua exceção customizada
import br.com.AllTallent.model.*; // Importar todos os Modelos do pacote
import br.com.AllTallent.repository.*; // Importar todos os Repositórios do pacote
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate; // Importar LocalDate
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    @Transactional // Garante atomicidade: ou salva tudo ou não salva nada
    public AvaliacaoResponseDTO criarAvaliacaoCompleta(AvaliacaoRequestDTO dto) {

        // 1. Cria a Avaliacao2 base
        Avaliacao2 novaAvaliacao = new Avaliacao2();
        novaAvaliacao.setTitulo(dto.titulo());
        // Adicionar lógica para dataPrazo se você a incluir no AvaliacaoRequestDTO
        // Ex: novaAvaliacao.setDataPrazo(dto.dataPrazo());
        // O status será 'Rascunho' automaticamente pelo @PrePersist

        // 2. Busca e Associa Perguntas existentes
        // Usamos Set<> para evitar duplicatas e garantir a busca correta
        Set<Pergunta> perguntas = new HashSet<>(perguntaRepository.findAllById(dto.codigosPerguntas()));
        // Valida se todas as perguntas solicitadas foram encontradas
        if (perguntas.size() != dto.codigosPerguntas().size()) {
            // Encontra quais IDs não foram encontrados (opcional, para melhor mensagem de erro)
            Set<Long> idsEncontrados = perguntas.stream().map(Pergunta::getCodigo).collect(Collectors.toSet());
            List<Long> idsFaltando = dto.codigosPerguntas().stream().filter(id -> !idsEncontrados.contains(id)).collect(Collectors.toList());
            throw new EntityNotFoundException("Uma ou mais perguntas não foram encontradas. IDs não encontrados: " + idsFaltando);
        }
        novaAvaliacao.setPerguntas(perguntas); // Associa as perguntas encontradas à avaliação

        // 3. Salva a Avaliacao2 para obter seu ID gerado
        Avaliacao2 avaliacaoSalva = avaliacaoRepository.save(novaAvaliacao);

        // 4. Busca Funcionários existentes que receberão a avaliação
        List<Funcionario> funcionarios = funcionarioRepository.findAllById(dto.codigosFuncionarios());
         // Valida se todos os funcionários solicitados foram encontrados
        if (funcionarios.size() != dto.codigosFuncionarios().size()) {
            Set<Integer> idsEncontrados = funcionarios.stream().map(Funcionario::getCodigo).collect(Collectors.toSet());
            List<Integer> idsFaltando = dto.codigosFuncionarios().stream().filter(id -> !idsEncontrados.contains(id)).collect(Collectors.toList());
            // Lançar exceção causa rollback automático devido ao @Transactional
            throw new EntityNotFoundException("Um ou mais funcionários não foram encontrados. IDs não encontrados: " + idsFaltando);
        }

        // 5. Cria as instâncias AvaliacaoFuncionario para cada funcionário
        // Não precisamos guardar em 'instanciasSalvas' a menos que seja necessário depois
        for (Funcionario f : funcionarios) {
            // Cria a instância ligando o Funcionario e a Avaliacao2 recém-salva
            AvaliacaoFuncionario instancia = new AvaliacaoFuncionario(f, avaliacaoSalva);
            // Salva a instância na tabela tb_cad_funcionario_avalicacao
            // O status inicial 'PENDENTE' é definido no construtor de AvaliacaoFuncionario
            avaliacaoFuncionarioRepository.save(instancia);
        }
        // Nota: O relacionamento @OneToMany em Avaliacao2 (instanciasAvaliacao) com CascadeType.ALL
        // faria com que o save(novaAvaliacao) também salvasse as instâncias, mas criá-las
        // explicitamente após salvar a avaliação garante que temos o ID da avaliação.
        // A abordagem de salvar as instâncias separadamente é mais explícita aqui.

        // 6. Retorna um DTO da avaliação criada
        return new AvaliacaoResponseDTO(avaliacaoSalva);
    }


    // --- Método para Salvar/Atualizar Resposta de um Colaborador ---
    @Transactional
    public RespostaColaboradorResponseDTO salvarOuAtualizarResposta(RespostaColaboradorRequestDTO dto) {

        // 1. Valida e busca as entidades relacionadas pelos IDs recebidos
        AvaliacaoFuncionario avaliacaoFunc = avaliacaoFuncionarioRepository.findById(dto.avaliacaoFuncionarioCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Instância de avaliação (AvaliacaoFuncionario) não encontrada com ID: " + dto.avaliacaoFuncionarioCodigo()));

        Pergunta pergunta = perguntaRepository.findById(dto.perguntaCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada com ID: " + dto.perguntaCodigo()));

        PerguntaOpcao opcaoSelecionada = null;
        // Se um código de opção foi enviado, busca a opção
        if (dto.opcaoSelecionadaCodigo() != null) {
            opcaoSelecionada = perguntaOpcaoRepository.findById(dto.opcaoSelecionadaCodigo())
                    .orElseThrow(() -> new EntityNotFoundException("Opção selecionada não encontrada com ID: " + dto.opcaoSelecionadaCodigo()));
            // Validação extra: Garante que a opção selecionada pertence à pergunta correta
            if (!opcaoSelecionada.getPergunta().getCodigo().equals(pergunta.getCodigo())) {
                throw new IllegalArgumentException("Inconsistência: A opção selecionada (ID " + dto.opcaoSelecionadaCodigo()
                        + ") não pertence à pergunta fornecida (ID " + dto.perguntaCodigo() + ").");
            }
        }

        // 2. Tenta encontrar uma resposta existente para esta pergunta nesta instância de avaliação
        // Se encontrar, atualiza; se não, cria uma nova.
        RespostaColaborador resposta = respostaColaboradorRepository
                .findByAvaliacaoFuncionarioCodigoAndPerguntaCodigo(dto.avaliacaoFuncionarioCodigo(), dto.perguntaCodigo())
                .orElse(new RespostaColaborador());

        // 3. Preenche/Atualiza os dados da entidade RespostaColaborador
        resposta.setAvaliacaoFuncionario(avaliacaoFunc); // Associa à instância correta
        resposta.setPergunta(pergunta);                 // Associa à pergunta correta
        resposta.setRespostaTexto(dto.respostaTexto()); // Define a resposta textual (será null se for múltipla escolha sem texto)
        resposta.setOpcaoSelecionada(opcaoSelecionada); // Define a opção selecionada (será null se for textual)

        // 4. Salva a resposta (INSERT ou UPDATE) no banco de dados
        RespostaColaborador respostaSalva = respostaColaboradorRepository.save(resposta);

        // 5. Retorna o DTO da resposta salva/atualizada
        return new RespostaColaboradorResponseDTO(respostaSalva);
    }

    // --- Métodos para Buscar Dados ---

    // Lista todas as definições de avaliação