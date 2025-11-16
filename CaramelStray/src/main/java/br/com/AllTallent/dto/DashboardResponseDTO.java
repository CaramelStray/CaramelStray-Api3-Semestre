package br.com.AllTallent.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder // Usar o pattern Builder para facilitar a construção
public class DashboardResponseDTO {
    
    // Card 1: Total de Colaboradores
    private Long totalColaboradores;

    // Card 2: Avaliações Concluídas no Mês
    private Integer avaliacoesConcluidasMes;

    // Card 3: Meta Mensal (% de aprovação)
    private Double metaMensal;

    // Card 4: Pendências (Total)
    private Integer totalPendencias;

    // Gráfico: Evolução Mensal (Colaboradores contratados por mês)
    private List<MesQuantidadeDTO> evolucaoMensal;

    //Gráfico: Distribuição de colaboradores por competência
    private List<CompetenciaQuantidadeDTO> totalColaboradoresCompetencia;


    //Gráfico de pizza: Distribuição de colaboradores por área de atuação
    private List<AreaQuantidadeDTO> totalColaboradoresArea;


    //Gráfico: ranking de 5 competencias mais avaliadas
    private List<CompetenciaQuantidadeDTO> top5CompetenciasMaisAvaliadas;
}