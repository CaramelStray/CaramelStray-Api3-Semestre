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
    private final PasswordEncoder passwordEncoder; 

    @Transactional
    public Funcionario register(CadastroRequestDTO request) {
        
        
        if (funcionarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }

        
        Area area = areaRepository.findById(request.getCodigoArea())
                .orElseThrow(() -> new RuntimeException("Erro: Área (Departamento) não encontrada."));

        Perfil perfil = perfilRepository.findById(request.getCodigoPerfil())
                .orElseThrow(() -> new RuntimeException("Erro: Perfil (Cargo) não encontrado."));

        
        String senhaCriptografada = passwordEncoder.encode(request.getSenha());

        
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNomeCompleto(request.getNomeCompleto());
        novoFuncionario.setEmail(request.getEmail());
        novoFuncionario.setSenhaHash(senhaCriptografada);
        novoFuncionario.setTelefone(request.getTelefone());
        novoFuncionario.setResumo(request.getResumo());
        novoFuncionario.setArea(area);
        novoFuncionario.setPerfil(perfil);
        novoFuncionario.setCpf(request.getCpf());
        novoFuncionario.setLocalizacao(request.getLocalizacao());
        novoFuncionario.setTituloProfissional(request.getTituloProfissional());
        
        novoFuncionario.setIdCracha(request.getIdCracha());
        novoFuncionario.setDataAdmissao(request.getDataAdmissao());

        if (request.getCodigoGestor() != null) {
            Funcionario gestor = funcionarioRepository.findById(request.getCodigoGestor())
                .orElseThrow(() -> new RuntimeException("Erro: Gestor informado não encontrado."));
            novoFuncionario.setGestor(gestor);
        }
        
        return funcionarioRepository.save(novoFuncionario);
    }
}