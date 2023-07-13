package br.com.trier.springvespertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.PilotRace;
import br.com.trier.springvespertino.models.dto.PilotRaceDTO;
import br.com.trier.springvespertino.services.PilotRaceService;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.RaceService;

@RestController
@RequestMapping("/pilot-race")
public class PilotRaceResource {
	
	
	@Autowired
    PilotRaceService service;
    
    @Autowired
    PilotService pilotoService;
    
    @Autowired
    RaceService corridaService;

    @PostMapping
    public ResponseEntity<PilotRaceDTO> insert(@RequestBody PilotRaceDTO pilotoCorridaDTO) {
    	return ResponseEntity.ok(service.insert(new PilotRace(pilotoCorridaDTO,
    			pilotoService.findById(pilotoCorridaDTO.getIdPilot()),
    			corridaService.findById(pilotoCorridaDTO.getIdRace())))
    			.toDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PilotRaceDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id).toDTO());
    }

    @GetMapping
    public ResponseEntity<List<PilotRaceDTO>> listAll() {
    	return ResponseEntity.ok(service.listAll().stream().map(PilotRace::toDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PilotRaceDTO> update(@PathVariable Integer id, @RequestBody PilotRaceDTO pilotoCorridaDTO) {
    	PilotRace pilotoCorrida = new PilotRace(pilotoCorridaDTO,
    			pilotoService.findById(pilotoCorridaDTO.getIdPilot()),
    			corridaService.findById(pilotoCorridaDTO.getIdRace()));
    	pilotoCorrida.setId(id);
    	pilotoCorrida = service.update(pilotoCorrida);
        return ResponseEntity.ok(pilotoCorrida.toDTO());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/placement/{colocacao}")
    public ResponseEntity<List<PilotRaceDTO>> findByColocacao(@PathVariable Integer colocacao) {
        return ResponseEntity.ok(service.findByPlacement(colocacao).stream().map(PilotRace::toDTO).toList());
    }
    
    @GetMapping("/pilot/{idPiloto}")
    public ResponseEntity<List<PilotRaceDTO>> findByPiloto(@PathVariable Integer idPiloto) {
        return ResponseEntity.ok(service.findByPilot(pilotoService.findById(idPiloto)).stream().map(PilotRace::toDTO).toList());
    }
    
    @GetMapping("/race/{idCorrida}")
    public ResponseEntity<List<PilotRaceDTO>> findByCorridaOrderByColocacaoAsc(@PathVariable Integer idCorrida) {
        return ResponseEntity.ok(service.findByRaceOrderByPlacementAsc(corridaService.findById(idCorrida)).stream().map(PilotRace::toDTO).toList());
    }
    
    @GetMapping("/placement-race/{colocacaoInicial}/{colocacaoFinal}/{idCorrida}")
    public ResponseEntity<List<PilotRaceDTO>> findByColocacaoBetweenAndCorrida(@PathVariable Integer colocacaoInicial, @PathVariable Integer colocacaoFinal, @PathVariable Integer idCorrida) {
        return ResponseEntity.ok(
        		service.findByPlacementBetweenAndRace(colocacaoInicial, colocacaoFinal, corridaService.findById(idCorrida)).stream().map(PilotRace::toDTO).toList());
    }
    
    @GetMapping("/pilot-race/{idPiloto}/{idCorrida}")
    public ResponseEntity<PilotRaceDTO> findByPilotoAndCorrida(@PathVariable Integer idPiloto, @PathVariable Integer idCorrida) {
    	return ResponseEntity.ok(service.findByPilotAndRace(pilotoService.findById(idPiloto), corridaService.findById(idCorrida)).toDTO());
    }

}
