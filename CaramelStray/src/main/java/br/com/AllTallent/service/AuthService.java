package br.com.AllTallent.service;

import br.com.AllTallent.dto.CadastroRequestDTO;
import br.com.AllTallent.model.Area;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.model.Perfil;
import br.com.AllTallent.repository.AreaRepository;
import br.com.AllTallent.repository.FuncionarioRepository;
import br.com.AllTallent.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime; // Importante para a data de cadastro

@Service
@RequiredArgsConstructor
public class AuthService {

    private final FuncionarioRepository funcionarioRepository;
    private final AreaRepository areaRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder; 

    @Transactional
    public Funcionario register(CadastroRequestDTO request) {
        
        // 1. Validar se o email já existe
        if (funcionarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }

        // 2. Buscar relacionamentos obrigatórios
        Area area = areaRepository.findById(request.getCodigoArea())
                .orElseThrow(() -> new RuntimeException("Erro: Área (Departamento) não encontrada."));

        Perfil perfil = perfilRepository.findById(request.getCodigoPerfil())
                .orElseThrow(() -> new RuntimeException("Erro: Perfil (Cargo) não encontrado."));

        // 3. Criptografia
        String senhaCriptografada = passwordEncoder.encode(request.getSenha());

        // 4. Criar e popular o Funcionário
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNomeCompleto(request.getNomeCompleto());
        novoFuncionario.setEmail(request.getEmail());
        novoFuncionario.setSenhaHash(senhaCriptografada);
        novoFuncionario.setTelefone(request.getTelefone());
        novoFuncionario.setResumo(request.getResumo());
        novoFuncionario.setArea(area);
        novoFuncionario.setPerfil(perfil);
        
        // Novos campos
        novoFuncionario.setCpf(request.getCpf());
        novoFuncionario.setLocalizacao(request.getLocalizacao());
        novoFuncionario.setTituloProfissional(request.getTituloProfissional());
        novoFuncionario.setIdCracha(request.getIdCracha());
        novoFuncionario.setDataAdmissao(request.getDataAdmissao());
        
        // --- DATA DE CADASTRO AUTOMÁTICA ---
        novoFuncionario.setDataCadastro(OffsetDateTime.now()); 

        // Lógica do Gestor (Opcional)
        if (request.getCodigoGestor() != null) {
            Funcionario gestor = funcionarioRepository.findById(request.getCodigoGestor())
                .orElseThrow(() -> new RuntimeException("Erro: Gestor informado não encontrado."));
            novoFuncionario.setGestor(gestor);
        }
        
        // 5. Salvar
        return funcionarioRepository.save(novoFuncionario);
    }
}