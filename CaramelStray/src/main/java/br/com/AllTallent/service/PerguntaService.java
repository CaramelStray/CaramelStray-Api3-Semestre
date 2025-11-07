package br.com.AllTallent.service; 

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
import br.com.AllTallent.model.Pergunta; 
import br.com.AllTallent.model.PerguntaOpcao;
import br.com.AllTallent.repository.CompetenciaRepository;
import br.com.AllTallent.repository.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PerguntaService {

    private final PerguntaRepository perguntaRepository;
    private final CompetenciaRepository competenciaRepository;

    public PerguntaService(PerguntaRepository perguntaRepository, CompetenciaRepository competenciaRepository) {
        this.perguntaRepository = perguntaRepository;
        this.competenciaRepository = competenciaRepository;
    }

    @Transactional 
    public PerguntaResponseDTO criarPergunta(PerguntaRequestDTO dto) {
        Competencia competencia = competenciaRepository.findById(dto.competenciaCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Competência não encontrada: " + dto.competenciaCodigo()));

        Pergunta novaPergunta = new Pergunta();
        novaPergunta.setPergunta(dto.pergunta());
        novaPergunta.setCompetencia(competencia);
        novaPergunta.setTipoPergunta(dto.tipoPergunta()); 

        
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
             System.out.println(">>> Não é múltipla escolha ou não há opções válidas a processar.");
        }

        System.out.println(">>> Salvando Pergunta no repositório..."); 
        Pergunta perguntaSalva = perguntaRepository.save(novaPergunta);
        System.out.println(">>> Pergunta salva com código: " + perguntaSalva.getCodigo()); 

        return new PerguntaResponseDTO(perguntaSalva);
    }

    @Transactional(readOnly = true) 
    public List<PerguntaResponseDTO> listarTodas() {
        return perguntaRepository.findAll().stream()
                .map(PerguntaResponseDTO::new)
                .collect(Collectors.toList());
    }

     
    @Transactional(readOnly = true)
    public PerguntaResponseDTO buscarPorId(Long id) {
        return perguntaRepository.findById(id)
                .map(PerguntaResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada: " + id));
    }

    @Transactional
    public void deletarPergunta(Long id) {
        if (!perguntaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pergunta não encontrada: " + id);
        }
        perguntaRepository.deleteById(id);
    }
}