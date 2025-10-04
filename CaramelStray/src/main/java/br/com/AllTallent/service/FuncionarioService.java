package br.com.AllTallent.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.AllTallent.dto.CertificadoDTO;
import br.com.AllTallent.dto.CertificadoRequestDTO;
import br.com.AllTallent.dto.ExperienciaDTO;
import br.com.AllTallent.dto.ExperienciaRequestDTO;
import br.com.AllTallent.dto.FuncionarioExperienciasResponseDTO;
import br.com.AllTallent.dto.FuncionarioPerfilDTO;
import br.com.AllTallent.dto.FuncionarioRequestDTO;
import br.com.AllTallent.dto.FuncionarioResponseDTO;
import br.com.AllTallent.exception.ResourceNotFoundException;
import br.com.AllTallent.exception.UnauthorizedActionException;
import br.com.AllTallent.model.Area;
import br.com.AllTallent.model.Competencia;
import br.com.AllTallent.model.Experiencia;
import br.com.AllTallent.model.Funcionario; 
import br.com.AllTallent.model.FuncionarioCertificado;
import br.com.AllTallent.model.Perfil;
import br.com.AllTallent.repository.AreaRepository;
import br.com.AllTallent.repository.CertificadoRepository;
import br.com.AllTallent.repository.CompetenciaRepository;
import br.com.AllTallent.repository.ExperienciaRepository;
import br.com.AllTallent.repository.FuncionarioRepository;
import br.com.AllTallent.repository.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FuncionarioService {

    // Injetando todos os repositórios necessários
    private final FuncionarioRepository funcionarioRepository;
    private final AreaRepository areaRepository;
    private final PerfilRepository perfilRepository;
    private final CompetenciaRepository competenciaRepository;
    private final ExperienciaRepository experienciaRepository; 
    private final CertificadoRepository certificadoRepository; 
    

   public FuncionarioService(
        FuncionarioRepository funcionarioRepository, 
        AreaRepository areaRepository, 
        PerfilRepository perfilRepository, 
        CompetenciaRepository competenciaRepository,
        ExperienciaRepository experienciaRepository,
        CertificadoRepository certificadoRepository // <-- NOVO
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.areaRepository = areaRepository;
        this.perfilRepository = perfilRepository;
        this.competenciaRepository = competenciaRepository;
        this.experienciaRepository = experienciaRepository;
        this.certificadoRepository = certificadoRepository;
    }

    
    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll().stream()
                .map(FuncionarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Busca um funcionário e o retorna como DTO
    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscarPorId(Integer id) {
        return funcionarioRepository.findById(id)
                .map(FuncionarioResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
    }

    // Cria um novo funcionário a partir de um DTO
    @Transactional
    public FuncionarioResponseDTO criar(FuncionarioRequestDTO dto) {
        Funcionario funcionario = new Funcionario();
        mapearDtoParaEntidade(dto, funcionario); // Reutiliza a lógica de mapeamento
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        return new FuncionarioResponseDTO(funcionarioSalvo);
    }

    // Atualiza um funcionário existente a partir de um DTO
    @Transactional
    public FuncionarioResponseDTO atualizar(Integer id, FuncionarioRequestDTO dto) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));

        mapearDtoParaEntidade(dto, funcionarioExistente);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioExistente);
        return new FuncionarioResponseDTO(funcionarioSalvo);
    }

    // Deleta um funcionário
    @Transactional
    public void deletar(Integer id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id);
        }
        funcionarioRepository.deleteById(id);
    }

    // Método auxiliar privado para converter o DTO de requisição para a Entidade
    private void mapearDtoParaEntidade(FuncionarioRequestDTO dto, Funcionario entidade) {
        // Busca as entidades relacionadas a partir dos IDs
        Optional.ofNullable(dto.nomeCompleto()).ifPresent(entidade::setNomeCompleto);
        Optional.ofNullable(dto.telefone()).ifPresent(entidade::setTelefone);
        Optional.ofNullable(dto.tituloProfissional()).ifPresent(entidade::setTituloProfissional);
        Optional.ofNullable(dto.localizacao()).ifPresent(entidade::setLocalizacao);
        Optional.ofNullable(dto.resumo()).ifPresent(entidade::setResumo);
        Optional.ofNullable(dto.email()).ifPresent(entidade::setEmail);

        Optional.ofNullable(dto.areaId()).ifPresent(areaId -> {
            Area area = areaRepository.findById(areaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Área não encontrada com o ID: " + areaId));
            entidade.setArea(area);
        });

        Optional.ofNullable(dto.perfilId()).ifPresent(perfilId -> {
            Perfil perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com o ID: " + perfilId));
            entidade.setPerfil(perfil);
        });

        Optional.ofNullable(dto.gestorId())
            .map(gestorId -> funcionarioRepository.findById(gestorId)
                .orElseThrow(() -> new ResourceNotFoundException("Gestor não encontrado com o ID: " + gestorId)))
            .ifPresentOrElse(entidade::setGestor, () -> entidade.setGestor(null));
    }

    
    @Transactional(readOnly = true)
    public FuncionarioPerfilDTO buscarPerfilPorId(Integer id) {
    return funcionarioRepository.findByIdCompleto(id)
            .map(FuncionarioPerfilDTO::new) 
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
    }
    public CertificadoDTO adicionarCertificado(Integer funcionarioId, CertificadoRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com o ID: " + funcionarioId));

        FuncionarioCertificado novoCertificado = new FuncionarioCertificado();
        novoCertificado.setCertificado(dto.nome());
        novoCertificado.setFuncionario(funcionario);

        funcionario.getCertificados().add(novoCertificado);

        funcionarioRepository.save(funcionario);

        return new CertificadoDTO(novoCertificado);
    }
    @Transactional
    public void associarCompetencias(Integer idLogado, Integer idAlvo, List<Integer> codigosCompetencia) {
        
        if (!podeAssociarCompetencias(idLogado, idAlvo)) {
            
            throw new UnauthorizedActionException("O usuário logado não tem permissão para alterar as competências deste funcionário ou ele pertence a outra área.");
        }

        Funcionario alvo = funcionarioRepository.findByIdCompleto(idAlvo) // Usando findByIdCompleto
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário alvo não encontrado."));
        
    
        List<Competencia> novasCompetencias = competenciaRepository.findAllById(codigosCompetencia);

        if (novasCompetencias.size() < codigosCompetencia.size()) {
        throw new ResourceNotFoundException("Um ou mais códigos de competência não foram encontrados. Certifique-se de que todos os IDs são válidos.");
        }
      
        alvo.setCompetencias(new HashSet<>(novasCompetencias)); 
        funcionarioRepository.save(alvo);
    }
    private boolean podeAssociarCompetencias(Integer idLogado, Integer idAlvo) {
    
        if (idLogado.equals(idAlvo)) {
            return true;
        }

        
        Funcionario logado = funcionarioRepository.findById(idLogado).orElse(null);
        Funcionario alvo = funcionarioRepository.findById(idAlvo).orElse(null);

        if (logado == null || alvo == null) {
            return false; 
        }
        Integer nivelLogado = logado.getPerfil() != null ? logado.getPerfil().getCodigo() : null;
        Integer nivelAlvo = alvo.getPerfil() != null ? alvo.getPerfil().getCodigo() : null;
        
        
        if (nivelLogado == null || nivelAlvo == null) {
            return false;
        }
        Integer areaLogado = logado.getArea() != null ? logado.getArea().getCodigo() : null;
        Integer areaAlvo = alvo.getArea() != null ? alvo.getArea().getCodigo() : null;

        if (areaLogado == null || areaAlvo == null || !areaLogado.equals(areaAlvo)) {
            return false;
        }
        return nivelLogado < nivelAlvo;
    }
     @Transactional(readOnly = true)
    public Funcionario buscarFuncionarioCompleto(Integer id) {
        // Usa o findByIdCompleto que já carrega as competências via FETCH
        return funcionarioRepository.findByIdCompleto(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
    }
    @Transactional(readOnly = true)
    public FuncionarioExperienciasResponseDTO listarExperienciasPorFuncionario(Integer id) {
        // O método findByIdCompleto já carrega tudo que precisamos de forma otimizada
        Funcionario funcionario = funcionarioRepository.findByIdCompleto(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
        
        // O construtor do DTO faz toda a "mágica" da conversão
        return new FuncionarioExperienciasResponseDTO(funcionario);
    }
    @Transactional
    public ExperienciaDTO adicionarExperiencia(Integer funcionarioId, ExperienciaRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + funcionarioId));

        Experiencia novaExperiencia = new Experiencia();
        novaExperiencia.setCargo(dto.cargo());
        novaExperiencia.setEmpresa(dto.empresa());
        novaExperiencia.setDataInicio(dto.dataInicio());
        novaExperiencia.setDataFim(dto.dataFim());
        novaExperiencia.setDescricao(dto.descricao());
        novaExperiencia.setFuncionario(funcionario);

        Experiencia experienciaSalva = experienciaRepository.save(novaExperiencia);
        return new ExperienciaDTO(experienciaSalva);
    }

    @Transactional
    public ExperienciaDTO atualizarExperiencia(Integer experienciaId, ExperienciaRequestDTO dto) {
        Experiencia experienciaExistente = experienciaRepository.findById(experienciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência não encontrada com o ID: " + experienciaId));
        
        // No futuro, aqui você pode adicionar uma verificação de permissão
        // para garantir que o usuário logado pode editar esta experiência.

        experienciaExistente.setCargo(dto.cargo());
        experienciaExistente.setEmpresa(dto.empresa());
        experienciaExistente.setDataInicio(dto.dataInicio());
        experienciaExistente.setDataFim(dto.dataFim());
        experienciaExistente.setDescricao(dto.descricao());

        Experiencia experienciaAtualizada = experienciaRepository.save(experienciaExistente);
        return new ExperienciaDTO(experienciaAtualizada);
    }
    @Transactional
    public void removerCertificado(Integer certificadoId) {
        // Verifica se o certificado realmente existe antes de tentar deletar
        if (!certificadoRepository.existsById(certificadoId)) {
            // Lança uma exceção se não for encontrado, o que resultará em um erro 404 Not Found
            throw new ResourceNotFoundException("Certificado não encontrado com o ID: " + certificadoId);
        }
        
        // Se existir, deleta pelo ID
        certificadoRepository.deleteById(certificadoId);
    }
   
}
