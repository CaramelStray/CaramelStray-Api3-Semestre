package br.com.AllTallent.service;

// --- IMPORTAÇÕES ---
import br.com.AllTallent.dto.DashboardResponseDTO;
import br.com.AllTallent.dto.MesQuantidadeDTO;
import br.com.AllTallent.dto.MesQuantidadeProjection; 
import br.com.AllTallent.dto.AreaQuantidadeDTO;       
import br.com.AllTallent.dto.CompetenciaQuantidadeDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.transaction.annotation.Transactional; 
import java.util.LinkedHashMap;
import java.util.Map;

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

    // --- MÉTODOS AUXILIARES NOVOS (Vieram do Git) ---
    public List<AreaQuantidadeDTO> getTotalColaboradoresArea() {
        return funcionarioRepo.countFuncionariosPorArea();
    }
    
    public List<CompetenciaQuantidadeDTO> getTotalColaboradoresCompetencia() {
        return funcionarioRepo.countFuncionariosPorCompetencia();
    }

    public List<CompetenciaQuantidadeDTO> getTop5CompetenciasMaisAvaliadas() {
        return avaliacaoFuncionarioRepo.findTopCompetenciasMaisAvaliadas(PageRequest.of(0, 5));
    }
    // ------------------------------------------------

    // --- NOVO MÉTODO PARA O NOVO DASHBOARD (MESCLADO) ---
    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardData(Integer codigoAreaFiltro) {

        // --- Cálculos de Data ---
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());

        Long totalColaboradores;
        Integer totalPendencias;
        Integer concluidosMes;
        Integer aprovadosMes;
        
        List<MesQuantidadeProjection> evolucaoProj;

        // --- Lógica do Filtro de Área ---
        if (codigoAreaFiltro != null) {
            totalColaboradores = funcionarioRepo.countByAreaCodigo(codigoAreaFiltro);
            totalPendencias = avaliacaoFuncionarioRepo.countTotalPendentesByArea(codigoAreaFiltro);
            concluidosMes = avaliacaoFuncionarioRepo.countConcluidasNoMesByArea(inicioMes, fimMes, codigoAreaFiltro);
            aprovadosMes = avaliacaoFuncionarioRepo.countAprovadasNoMesByArea(inicioMes, fimMes, codigoAreaFiltro);
            evolucaoProj = funcionarioRepo.findEvolucaoMensalByArea(codigoAreaFiltro);
        } else {
            totalColaboradores = funcionarioRepo.count();
            totalPendencias = avaliacaoFuncionarioRepo.countTotalPendentes();
            concluidosMes = avaliacaoFuncionarioRepo.countConcluidasNoMes(inicioMes, fimMes);
            aprovadosMes = avaliacaoFuncionarioRepo.countAprovadasNoMes(inicioMes, fimMes);
            evolucaoProj = funcionarioRepo.findEvolucaoMensal();
        }

        // --- Conversão de Projeção para DTO ---
        List<MesQuantidadeDTO> evolucao = evolucaoProj.stream()
            .map(p -> new MesQuantidadeDTO(p.getMes(), p.getQuantidade()))
            .collect(Collectors.toList());

        // --- Cálculo da Meta ---
        Double metaMensal = 0.0;
        if (concluidosMes != null && concluidosMes > 0) {
            metaMensal = (aprovadosMes * 100.0) / concluidosMes;
        }

        // --- Construção da Resposta Final (Mesclada) ---
        return DashboardResponseDTO.builder()
                // Dados Originais
                .totalColaboradores(totalColaboradores)
                .avaliacoesConcluidasMes(concluidosMes)
                .metaMensal(metaMensal)
                .totalPendencias(totalPendencias)
                .evolucaoMensal(evolucao)
                // Novos Gráficos (do Git)
                .totalColaboradoresCompetencia(getTotalColaboradoresCompetencia())
                .top5CompetenciasMaisAvaliadas(getTop5CompetenciasMaisAvaliadas())
                .totalColaboradoresArea(getTotalColaboradoresArea())
                .build();
    }

    // --- MÉTODOS ANTIGOS MANTIDOS (LEGADO) ---
    public Map<String, Object> gerarResumo() {
        List<Funcionario> funcionarios = funcionarioRepo.findAll();
        List<Avaliacao> instancias = avaliacaoRepo.findAll();
        List<AvaliacaoFuncionario> instanciasFuncionarios = avaliacaoFuncionarioRepo.findAll();
        
        long totalColaboradores = funcionarios.size();
        long avaliacoesConcluidas = instancias.stream()
                .filter(i -> "CONCLUIDO".equalsIgnoreCase(i.getStatus()))
                .count();
        long avaliacoesPendentes = instancias.stream()
                .filter(i -> "PENDENTE".equalsIgnoreCase(i.getStatus()))
                .count();
        
        List<String> colaboradoresPendentes = instanciasFuncionarios.stream()
                .filter(i -> "PENDENTE".equalsIgnoreCase(i.getResultadoStatus()))
                .map(i -> i.getFuncionario().getNomeCompleto())
                .distinct()
                .toList();

        List<String> colaboradoresSemEntrega = instanciasFuncionarios.stream()
                .filter(instancia -> {
                    List<RespostaColaborador> respostas = respostaColaboradorRepo
                            .findByAvaliacaoFuncionarioCodigo(instancia.getCodigo());
                    return respostas.isEmpty();
                })
                .map(instancia -> instancia.getFuncionario().getNomeCompleto())
                .distinct()
                .toList();

        Map<String, Object> dados = new LinkedHashMap<>();
        dados.put("totalColaboradores", totalColaboradores);
        dados.put("avaliacoesConcluidas", avaliacoesConcluidas);
        dados.put("avaliacoesPendentes", avaliacoesPendentes);
        dados.put("colaboradoresPendentes", colaboradoresPendentes);
        dados.put("colaboradoresSemEntrega", colaboradoresSemEntrega);

        return dados;
    }

    public Map<String, Long> getDistribuicaoPorArea() {
        return funcionarioRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        f -> (f.getArea() == null) ? "Sem área" : f.getArea().getNome(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> getDistribuicaoPorCompetencias() {
        return funcionarioRepo.findAll().stream()
                .flatMap(func -> func.getCompetencias().stream())
                .collect(Collectors.groupingBy(
                        comp -> (comp.getNome() == null || comp.getNome().isBlank()) ? "Sem nome" : comp.getNome(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }
}