package br.com.AllTallent.service;

// --- NOVAS IMPORTAÇÕES ---
import br.com.AllTallent.dto.AreaQuantidadeDTO;
import br.com.AllTallent.dto.CompetenciaQuantidadeDTO;
import br.com.AllTallent.dto.DashboardResponseDTO;
import br.com.AllTallent.dto.MesQuantidadeDTO;
import java.time.LocalDate;
import java.util.List;
// --- FIM NOVAS IMPORTAÇÕES ---

import br.com.AllTallent.model.Avaliacao;
import br.com.AllTallent.model.AvaliacaoFuncionario;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.model.RespostaColaborador;
import br.com.AllTallent.repository.AvaliacaoFuncionarioRepository;
import br.com.AllTallent.repository.AvaliacaoRepository;
import br.com.AllTallent.repository.FuncionarioRepository;
import br.com.AllTallent.repository.RespostaColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private FuncionarioRepository funcionarioRepo;

    @Autowired
    private AvaliacaoRepository avaliacaoRepo;

    @Autowired
    private AvaliacaoFuncionarioRepository avaliacaoFuncionarioRepo;

    @Autowired
    private RespostaColaboradorRepository respostaColaboradorRepo;



    // --- NOVO MÉTODO PARA O NOVO DASHBOARD ---
    // Este método é performático e busca os dados corretos (mensais)
    @Transactional(readOnly = true)

    // TOTAL DE COLABORADORES POR ÁREA
    public List<AreaQuantidadeDTO> getTotalColaboradoresArea() {
        return funcionarioRepo.countFuncionariosPorArea();
    }
    // --- TOTAL DE COLABORADORES POR COMPETÊNCIA ---
    public List<CompetenciaQuantidadeDTO> getTotalColaboradoresCompetencia() {
        return funcionarioRepo.countFuncionariosPorCompetencia();
    }

    // --- TOP 5 COMPETÊNCIAS MAIS AVALIADAS ---
    public List<CompetenciaQuantidadeDTO> getTop5CompetenciasMaisAvaliadas() {
        return avaliacaoFuncionarioRepo
                .findTopCompetenciasMaisAvaliadas(PageRequest.of(0, 5));
    }

    public DashboardResponseDTO getDashboardData() {

        // --- Cálculos de Data ---
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());

        // --- Queries dos Cards (Usando os novos métodos dos repositórios) ---
        Long totalColaboradores = funcionarioRepo.count();
        Integer totalPendencias = avaliacaoFuncionarioRepo.countTotalPendentes();
        Integer concluidosMes = avaliacaoFuncionarioRepo.countConcluidasNoMes(inicioMes, fimMes);
        Integer aprovadosMes = avaliacaoFuncionarioRepo.countAprovadasNoMes(inicioMes, fimMes);

        // --- Cálculo da Meta (%) ---
        Double metaMensal = 0.0;
        if (concluidosMes != null && concluidosMes > 0) {
            metaMensal = (aprovadosMes * 100.0) / concluidosMes;
        }

        // --- Query do Gráfico ---
        List<MesQuantidadeDTO> evolucao = funcionarioRepo.findEvolucaoMensal();

        // --- Monta o DTO de Resposta ---
        return DashboardResponseDTO.builder()
                .totalColaboradores(totalColaboradores)
                .avaliacoesConcluidasMes(concluidosMes)
                .metaMensal(metaMensal)
                .totalPendencias(totalPendencias)
                .evolucaoMensal(evolucao)
                .totalColaboradoresCompetencia(getTotalColaboradoresCompetencia())
                .top5CompetenciasMaisAvaliadas(getTop5CompetenciasMaisAvaliadas())
                .totalColaboradoresArea(getTotalColaboradoresArea())
                .build();
    }
    // --- FIM DO NOVO MÉTODO ---

}