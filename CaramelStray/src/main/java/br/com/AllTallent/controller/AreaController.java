package br.com.AllTallent.controller;

import br.com.AllTallent.model.Area;
import br.com.AllTallent.repository.AreaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/area") 
public class AreaController {

    private final AreaRepository areaRepository;

    
    public AreaController(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    
    @PostMapping
    public ResponseEntity<Area> createArea(@RequestBody Area area) {
        Area novaArea = areaRepository.save(area);
        return new ResponseEntity<>(novaArea, HttpStatus.CREATED);
    }

    
    @GetMapping
    public List<Area> getAllAreas() {
        return areaRepository.findAll();
    }
}