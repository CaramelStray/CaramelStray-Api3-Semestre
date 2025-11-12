package br.com.AllTallent.service;


import br.com.AllTallent.model.Avaliacao;
import br.com.AllTallent.model.AvaliacaoFuncionario;
import br.com.AllTallent.model.Funcionario;
import br.com.AllTallent.model.RespostaColaborador;
import br.com.AllTallent.repository.AvaliacaoFuncionarioRepository;
import br.com.AllTallent.repository.AvaliacaoRepository;
import br.com.AllTallent.repository.FuncionarioRepository;
import br.com.AllTallent.repository.RespostaColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
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

    public Map<String, Object> gerarResumo() {
        List<Funcionario> funcionarios = funcionarioRepo.findAll();
        List<Avaliacao> instancias = avaliacaoRepo.findAll();
        List<AvaliacaoFuncionario> instanciasFuncionarios = avaliacaoFuncionarioRepo.findAll();
        List<RespostaColaborador>  instanciasRespostas = respostaColaboradorRepo.findAll();

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
                    // Busca respostas dessa instância
                    List<RespostaColaborador> respostas = respostaColaboradorRepo
                            .findByAvaliacaoFuncionarioCodigo(instancia.getCodigo());
                    return respostas.isEmpty(); // não respondeu nada
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
                        f -> {
                            if (f.getArea() == null) {
                                return "Sem área";
                            }
                            return f.getArea().getNome(); // ajuste se o campo do nome for outro
                        },
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> getDistribuicaoPorCompetencias() {
        return funcionarioRepo.findAll().stream()
                .flatMap(func -> func.getCompetencias().stream())
                .collect(Collectors.groupingBy(
                        comp -> {
                            String nome = comp.getNome();
                            return (nome == null || nome.isBlank()) ? "Sem nome" : nome;
                        },
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

}
