package br.com.AllTallent.controller; // Pacote correto

import br.com.AllTallent.dto.LoginRequestDTO;
import br.com.AllTallent.dto.LoginResponseDTO;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*; // Importe PathVariable

import java.util.Optional;

@RestController
@RequestMapping("/auth") 
public class AuthenticationController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // O "tradutor" de senhas

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        
        // 1. Buscar o usuário pelo email
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByEmail(dto.email());

        // 2. Se o email não existir, retorna erro 401
        if (funcionarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Email ou senha inválidos.");
        }

        Funcionario funcionario = funcionarioOpt.get();

        // 3. Comparar a senha do DTO com o hash do banco
        // Usamos o getSenhaHash() que o Lombok criou
        boolean senhaCorreta = passwordEncoder.matches(dto.senha(), funcionario.getSenhaHash());

        // 4. Se a senha estiver errada, retorna erro 401
        if (!senhaCorreta) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Email ou senha inválidos.");
        }

        // 5. Se chegou aqui, o login está CORRETO!
        LoginResponseDTO resposta = new LoginResponseDTO(
            "Login bem-sucedido!",
            funcionario.getNomeCompleto() // Usamos o getNomeCompleto() que o Lombok criou
        );
        
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/hash/{senha}")
    public String gerarHash(@PathVariable String senha) {
        // Este método usa o mesmo encoder para criptografar
        // a senha que você passar na URL.
        String hash = passwordEncoder.encode(senha);
        return hash;
    }
}