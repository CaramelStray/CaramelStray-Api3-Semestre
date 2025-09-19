package br.com.AllTallent.controller;

import br.com.AllTallent.model.Area;
import br.com.AllTallent.repository.AreaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/area") // Define o endereço base para este controller
public class AreaController {

    private final AreaRepository areaRepository;

    // Injeção de dependência via construtor (melhor prática)
    public AreaController(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    /**
     * Endpoint para criar uma nova Área.
     * Mapeado para: POST http://localhost:8080/api/areas
     */
    @PostMapping
    public ResponseEntity<Area> createArea(@RequestBody Area area) {
        Area novaArea = areaRepository.save(area);
        return new ResponseEntity<>(novaArea, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todas as Áreas.
     * Mapeado para: GET http://localhost:8080/api/areas
     */
    @GetMapping
    public List<Area> getAllAreas() {
        return areaRepository.findAll();
    }
}