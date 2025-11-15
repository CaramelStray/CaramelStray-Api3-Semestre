package br.com.AllTallent.controller;

import br.com.AllTallent.dto.DashboardResponseDTO;
import br.com.AllTallent.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Retorna todos os dados consolidados para o Painel de Controle.
     * Protegido, apenas usuários autenticados podem acessar.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardResponseDTO> getDashboardData() {
        // Chama o NOVO método performático
        DashboardResponseDTO data = dashboardService.getDashboardData();
        return ResponseEntity.ok(data);
    }
}