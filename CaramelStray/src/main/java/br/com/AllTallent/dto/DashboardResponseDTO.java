package br.com.AllTallent.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardResponseDTO {
    

    private Long totalColaboradores;
    private Integer avaliacoesConcluidasMes;
    private Double metaMensal;
    private Integer totalPendencias;
    private List<MesQuantidadeDTO> evolucaoMensal;

    
    // Gráfico: Distribuição de colaboradores por competência
    private List<CompetenciaQuantidadeDTO> totalColaboradoresCompetencia;

    // Gráfico de pizza: Distribuição de colaboradores por área de atuação
    private List<AreaQuantidadeDTO> totalColaboradoresArea;

    // Gráfico: Ranking de 5 competencias mais avaliadas
    private List<CompetenciaQuantidadeDTO> top5CompetenciasMaisAvaliadas;
}