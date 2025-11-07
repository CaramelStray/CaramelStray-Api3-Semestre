package br.com.AllTallent.controller;

import br.com.AllTallent.config.JwtService;
import br.com.AllTallent.dto.LoginRequestDTO;
import br.com.AllTallent.dto.LoginResponseDTO;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails; // <<< IMPORT FALTANTE
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final FuncionarioRepository funcionarioRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        
        
        var funcionario = funcionarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado após autenticação"));

        String jwtToken = jwtService.generateToken((UserDetails) authentication.getPrincipal()); // <<< CAST CORRIGIDO

        return ResponseEntity.ok(
            new LoginResponseDTO(jwtToken, funcionario.getCodigo(), funcionario.getNomeCompleto())
        );
    }

}