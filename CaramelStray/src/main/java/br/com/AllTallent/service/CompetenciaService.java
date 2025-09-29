package br.com.AllTallent.service;

import br.com.AllTallent.dto.CompetenciaDTO;
import br.com.AllTallent.model.Competencia;
import br.com.AllTallent.repository.CompetenciaRepository;
import java.util.List;

public class CompetenciaService {
    private final CompetenciaRepository repository;

    public CompetenciaService(CompetenciaRepository repository){
        this.repository = repository;
    }

    public Competencia salvar (CompetenciaDTO dto) {
        Competencia competencia = new Competencia();
        competencia.setCodigo(dto.codigo());
        competencia.setNome(dto.nome());

        return repository.save(competencia);
    }

    public List <Competencia>listarcompetencia(){
        return repository.findAll();
    }

    public Competencia buscarPorId(Integer codigo){
        return  repository.findById(codigo)
                .orElseThrow(()-> new RuntimeException("Competencia nao encontrada"));
    }

    public void deletar(Integer codigo){
        repository.deleteById(codigo);
    }
}
