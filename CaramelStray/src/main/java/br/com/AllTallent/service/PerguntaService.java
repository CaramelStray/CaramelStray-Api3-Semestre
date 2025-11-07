package br.com.AllTallent.service; // Ou o pacote de serviços

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.AllTallent.dto.PerguntaRequestDTO;
import br.com.AllTallent.dto.PerguntaResponseDTO;
import br.com.AllTallent.dto.OpcaoRequest;
import br.com.AllTallent.model.Competencia;
import br.com.AllTallent.model.Pergunta; // Importante para transações
import br.com.AllTallent.model.PerguntaOpcao;
import br.com.AllTallent.repository.CompetenciaRepository;
import br.com.AllTallent.repository.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PerguntaService {

    private final PerguntaRepository perguntaRepository;
    private final CompetenciaRepository competenciaRepository;
    // Não precisamos injetar PerguntaOpcaoRepository aqui, pois o CascadeType.ALL cuida disso.

    public PerguntaService(PerguntaRepository perguntaRepository, CompetenciaRepository competenciaRepository) {
        this.perguntaRepository = perguntaRepository;
        this.competenciaRepository = competenciaRepository;
    }

    @Transactional // Garante que tudo (pergunta + opções) seja salvo ou nada
    public PerguntaResponseDTO criarPergunta(PerguntaRequestDTO dto) {
        // 1. Busca Competencia
        Competencia competencia = competenciaRepository.findById(dto.competenciaCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Competência não encontrada: " + dto.competenciaCodigo()));

        // 2. Cria Pergunta e seta dados básicos
        Pergunta novaPergunta = new Pergunta();
        novaPergunta.setPergunta(dto.pergunta());
        novaPergunta.setCompetencia(competencia);
        novaPergunta.setTipoPergunta(dto.tipoPergunta()); // Salva o tipo

        // 3. Processa Opções SE for múltipla escolha e houver opções
        if ("multipla escolha".equalsIgnoreCase(dto.tipoPergunta()) && dto.opcoes() != null && !dto.opcoes().isEmpty()) {
            System.out.println(">>> Processando " + dto.opcoes().size() + " opções recebidas.");
            
            Set<PerguntaOpcao> opcoesSet = new HashSet<>();
            

            for (OpcaoRequest opRequest : dto.opcoes()) { 
        if (opRequest.descricao() != null && !opRequest.descricao().trim().isEmpty()) {
            PerguntaOpcao opcao = new PerguntaOpcao();
            
            // Configure os dois campos
            opcao.setDescricaoOpcao(opRequest.descricao().trim());
            opcao.setIsCorreta(opRequest.isCorreta()); // <--- AQUI
            
            opcao.setPergunta(novaPergunta);
            opcoesSet.add(opcao);
        }
    }
    
    novaPergunta.setOpcoes(opcoesSet);
}else {
             // -> DEBUG: Log se não for múltipla escolha ou não houver opções
             System.out.println(">>> Não é múltipla escolha ou não há opções válidas a processar.");
        }

        // 4. Salva a Pergunta (e as Opcoes, por causa do CascadeType.ALL)
        System.out.println(">>> Salvando Pergunta no repositório..."); // -> DEBUG: Antes do save
        Pergunta perguntaSalva = perguntaRepository.save(novaPergunta);
        System.out.println(">>> Pergunta salva com código: " + perguntaSalva.getCodigo()); // -> DEBUG: Depois do save

        // 5. Retorna o DTO
        return new PerguntaResponseDTO(perguntaSalva);
    }

    // Método para listar todas (pode manter a lógica simples aqui ou no controller)
    @Transactional(readOnly = true) // readOnly para otimizar consultas
    public List<PerguntaResponseDTO> listarTodas() {
        return perguntaRepository.findAll().stream()
                .map(PerguntaResponseDTO::new)
                .collect(Collectors.toList());
    }

     // Método para buscar por ID (pode manter a lógica simples aqui ou no controller)
    @Transactional(readOnly = true)
    public PerguntaResponseDTO buscarPorId(Long id) {
        return perguntaRepository.findById(id)
                .map(PerguntaResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada: " + id));
    }

    // Método para deletar
    @Transactional
    public void deletarPergunta(Long id) {
        if (!perguntaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pergunta não encontrada: " + id);
        }
        // A deleção em cascata cuidará das opções associadas
        perguntaRepository.deleteById(id);
    }
}