package br.com.AllTallent.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

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
import br.com.AllTallent.model.*;
import br.com.AllTallent.repository.*;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.AllTallent.config.CustomUserDetails;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final AreaRepository areaRepository;
    private final PerfilRepository perfilRepository;
    private final CompetenciaRepository competenciaRepository;
    private final ExperienciaRepository experienciaRepository; 
    private final CertificadoRepository certificadoRepository; 
    private final PasswordEncoder passwordEncoder;

   public FuncionarioService(
        FuncionarioRepository funcionarioRepository, 
        AreaRepository areaRepository, 
        PerfilRepository perfilRepository, 
        CompetenciaRepository competenciaRepository,
        ExperienciaRepository experienciaRepository,
        CertificadoRepository certificadoRepository,
        PasswordEncoder passwordEncoder 
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.areaRepository = areaRepository;
        this.perfilRepository = perfilRepository;
        this.competenciaRepository = competenciaRepository;
        this.experienciaRepository = experienciaRepository;
        this.certificadoRepository = certificadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll().stream()
                .map(FuncionarioResponseDTO::new)
                .collect(Collectors.toList());
    }*/

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarTodos(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            return funcionarioRepository.findAll()
                    .stream()
                    .map(FuncionarioResponseDTO::new)
                    .collect(Collectors.toList());
        }

        return funcionarioRepository.buscarPorTexto(texto)
                .stream()
                .map(FuncionarioResponseDTO::new)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscarPorId(Integer id) {
        return funcionarioRepository.findById(id)
                .map(FuncionarioResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
    }

    @Transactional
    public FuncionarioResponseDTO criar(FuncionarioRequestDTO dto) {
        Funcionario funcionario = new Funcionario();
        mapearDtoParaEntidade(dto, funcionario);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        return new FuncionarioResponseDTO(funcionarioSalvo);
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(Integer id, FuncionarioRequestDTO dto) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));

        mapearDtoParaEntidade(dto, funcionarioExistente);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioExistente);
        return new FuncionarioResponseDTO(funcionarioSalvo);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id);
        }
        funcionarioRepository.deleteById(id);
    }

    private void mapearDtoParaEntidade(FuncionarioRequestDTO dto, Funcionario entidade) {
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
        if (dto.senhaHash() != null && !dto.senhaHash().isEmpty()) {
             entidade.setSenhaHash(passwordEncoder.encode(dto.senhaHash()));
        }
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

        if (funcionario.getCertificados() == null) {
            funcionario.setCertificados(new HashSet<>());
        }
        funcionario.getCertificados().add(novoCertificado);

        funcionarioRepository.save(funcionario);

        return new CertificadoDTO(novoCertificado);
    }
    @Transactional
    public void associarCompetencias( Integer idAlvo, List<Integer> codigosCompetencia) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails usuarioLogado = (CustomUserDetails) authentication.getPrincipal();

        Funcionario alvo = funcionarioRepository.findByIdCompleto(idAlvo)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário alvo não encontrado."));

        if (!podeAssociar(usuarioLogado, alvo)) { 
            throw new UnauthorizedActionException("Você não tem permissão para alterar as competências deste funcionário.");
        }
    
        List<Competencia> novasCompetencias = competenciaRepository.findAllById(codigosCompetencia);

        if (novasCompetencias.size() < codigosCompetencia.size()) {
        throw new ResourceNotFoundException("Um ou mais códigos de competência não foram encontrados. Certifique-se de que todos os IDs são válidos.");
        }
        
        alvo.setCompetencias(new HashSet<>(novasCompetencias)); 
        funcionarioRepository.save(alvo);
    }

    private boolean podeAssociar(CustomUserDetails logado, Funcionario alvo) {
    
        if (logado.getCodigo().equals(alvo.getCodigo())) {
            return true;
        }

        if (logado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
            !logado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR"))) {
            
            return false;
        }

        if (alvo.getPerfil() == null || logado.getAreaId() == null) {
            return false; 
        }
        
        if (alvo.getArea() == null) {
            return false; 
        }
        
        boolean mesmoSetor = logado.getAreaId().equals(alvo.getArea().getCodigo());
        int perfilAlvoId = alvo.getPerfil().getCodigo();
        
        if (logado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR")) &&
            !logado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            boolean alvoEhColaborador = (perfilAlvoId == 3);
            return mesmoSetor && alvoEhColaborador;
        }

        if (logado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            boolean alvoEhTime = (perfilAlvoId == 2 || perfilAlvoId == 3);
            return mesmoSetor && alvoEhTime;
        }
           return false;
        
    }
     @Transactional(readOnly = true)
    public Funcionario buscarFuncionarioCompleto(Integer id) {
        return funcionarioRepository.findByIdCompleto(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
    }
    @Transactional(readOnly = true)
    public FuncionarioExperienciasResponseDTO listarExperienciasPorFuncionario(Integer id) {
        Funcionario funcionario = funcionarioRepository.findByIdCompleto(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
        
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

        if (funcionario.getExperiencias() == null) {
            funcionario.setExperiencias(new HashSet<>());
        }
        funcionario.getExperiencias().add(novaExperiencia);

        funcionarioRepository.save(funcionario);
        
        return new ExperienciaDTO(novaExperiencia);
    }

    @Transactional
    public ExperienciaDTO atualizarExperiencia(Integer experienciaId, ExperienciaRequestDTO dto) {
        Experiencia experienciaExistente = experienciaRepository.findById(experienciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência não encontrada com o ID: " + experienciaId));
        

        experienciaExistente.setCargo(dto.cargo());
        experienciaExistente.setEmpresa(dto.empresa());
        experienciaExistente.setDataInicio(dto.dataInicio());
        experienciaExistente.setDataFim(dto.dataFim());
        experienciaExistente.setDescricao(dto.descricao());

        Experiencia experienciaAtualizada = experienciaRepository.save(experienciaExistente);
        return new ExperienciaDTO(experienciaAtualizada);
    }

    @Transactional(readOnly = true)
    public boolean usuarioPodeEditarExperiencia(Integer experienciaId, Integer codigoUsuarioLogado) {
        Experiencia exp = experienciaRepository.findById(experienciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Experiência não encontrada"));
        return exp.getFuncionario().getCodigo().equals(codigoUsuarioLogado);
    }

    @Transactional(readOnly = true)
    public boolean usuarioPodeRemoverCertificado(Integer certificadoId, Integer codigoUsuarioLogado) {
        FuncionarioCertificado cert = certificadoRepository.findById(certificadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificado não encontrado"));
        return cert.getFuncionario().getCodigo().equals(codigoUsuarioLogado);
    }

    @Transactional
    public void removerCertificado(Integer certificadoId) {
        if (!certificadoRepository.existsById(certificadoId)) {
            throw new ResourceNotFoundException("Certificado não encontrado com o ID: " + certificadoId);
        }
        
        certificadoRepository.deleteById(certificadoId);
    }
    
}