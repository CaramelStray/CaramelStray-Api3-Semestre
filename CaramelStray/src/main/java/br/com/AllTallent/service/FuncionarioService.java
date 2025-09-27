package br.com.AllTallent.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.AllTallent.dto.CertificadoDTO;
import br.com.AllTallent.dto.CertificadoRequestDTO;
import br.com.AllTallent.dto.FuncionarioPerfilDTO;
import br.com.AllTallent.dto.FuncionarioRequestDTO;
import br.com.AllTallent.dto.FuncionarioResponseDTO;
import br.com.AllTallent.exception.ResourceNotFoundException;
import br.com.AllTallent.model.Area;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.model.FuncionarioCertificado;
import br.com.AllTallent.model.Perfil;
import br.com.AllTallent.repository.AreaRepository;
import br.com.AllTallent.repository.FuncionarioRepository;
import br.com.AllTallent.repository.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FuncionarioService {

    // Injetando todos os repositórios necessários
    private final FuncionarioRepository funcionarioRepository;
    private final AreaRepository areaRepository;
    private final PerfilRepository perfilRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, AreaRepository areaRepository, PerfilRepository perfilRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.areaRepository = areaRepository;
        this.perfilRepository = perfilRepository;
    }

    // Retorna a lista de funcionários já convertida para DTOs
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
        Area area = areaRepository.findById(dto.areaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área não encontrada com o ID: " + dto.areaId()));
        Perfil perfil = perfilRepository.findById(dto.perfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com o ID: " + dto.perfilId()));

        entidade.setNomeCompleto(dto.nomeCompleto());
        entidade.setEmail(dto.email());
        entidade.setCpf(dto.cpf());
        entidade.setTelefone(dto.telefone());
        entidade.setSenhaHash(dto.senhaHash());
        entidade.setArea(area);
        entidade.setPerfil(perfil);

        if (dto.gestorId() != null) {
            Funcionario gestor = funcionarioRepository.findById(dto.gestorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Gestor não encontrado com o ID: " + dto.gestorId()));
            entidade.setGestor(gestor);
        } else {
            entidade.setGestor(null);
        }
    }
    @Transactional(readOnly = true)
public FuncionarioPerfilDTO buscarPerfilPorId(Integer id) {
    // Usa o novo método otimizado do repositório
    return funcionarioRepository.findByIdCompleto(id)
            .map(FuncionarioPerfilDTO::new) // Converte a entidade completa para o DTO de perfil
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
    }
    public CertificadoDTO adicionarCertificado(Integer funcionarioId, CertificadoRequestDTO dto) {
        // 1. Encontra o funcionário no banco de dados
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com o ID: " + funcionarioId));

        // 2. Cria uma nova entidade de certificado
        FuncionarioCertificado novoCertificado = new FuncionarioCertificado();
        novoCertificado.setCertificado(dto.nome());
        novoCertificado.setFuncionario(funcionario);

        // 3. Adiciona o novo certificado à lista de certificados do funcionário
        funcionario.getCertificados().add(novoCertificado);

        // 4. Salva o funcionário. Devido ao CascadeType.ALL na entidade Funcionario,
        //    o novo certificado será salvo automaticamente junto.
        funcionarioRepository.save(funcionario);

        // 5. Retorna um DTO do certificado recém-criado
        return new CertificadoDTO(novoCertificado);
    }
}