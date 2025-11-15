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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final FuncionarioRepository funcionarioRepository;
    private final AreaRepository areaRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder; // Spring vai injetar isso (presume-se que você o tenha em outra config)

    @Transactional
    public Funcionario register(CadastroRequestDTO request) {
        
        // 1. Validar se o email já existe
        if (funcionarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }

        // 2. Buscar as entidades de Area e Perfil pelos IDs
        Area area = areaRepository.findById(request.getCodigoArea())
                .orElseThrow(() -> new RuntimeException("Erro: Área (Departamento) não encontrada."));

        Perfil perfil = perfilRepository.findById(request.getCodigoPerfil())
                .orElseThrow(() -> new RuntimeException("Erro: Perfil (Cargo) não encontrado."));

        // 3. Criptografar a senha
        String senhaCriptografada = passwordEncoder.encode(request.getSenha());

        // 4. Criar a nova entidade Funcionario
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNomeCompleto(request.getNomeCompleto());
        novoFuncionario.setEmail(request.getEmail());
        novoFuncionario.setSenhaHash(senhaCriptografada);
        novoFuncionario.setTelefone(request.getTelefone());
        novoFuncionario.setResumo(request.getResumo());
        novoFuncionario.setArea(area);
        novoFuncionario.setPerfil(perfil);
        
        // 5. Adicionar os novos campos
        novoFuncionario.setIdCracha(request.getIdCracha());
        novoFuncionario.setDataAdmissao(request.getDataAdmissao());

        // 6. Salvar no banco
        return funcionarioRepository.save(novoFuncionario);
    }
}