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

import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.services.CountryService;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.TeamService;

@RestController
@RequestMapping("/pilot")
public class PilotResource {
	
	@Autowired
	private PilotService service;

	@Autowired
	private CountryService countryService;
	
	@Autowired
	private TeamService teamService;

	@GetMapping("/{id}")
	public ResponseEntity<Pilot> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	ResponseEntity<Pilot> insert(@RequestBody Pilot pilot) {
		countryService.findById(pilot.getCountry().getId());
		teamService.findById(pilot.getTeam().getId());
		return ResponseEntity.ok(service.insert(pilot));
	}

	@GetMapping
	ResponseEntity<List<Pilot>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	@PutMapping("/{id}")
	ResponseEntity<Pilot> update(@PathVariable Integer id, @RequestBody Pilot pilot) {
		countryService.findById(pilot.getCountry().getId());
		teamService.findById(pilot.getTeam().getId());
		pilot.setId(id);
		return ResponseEntity.ok(service.update(pilot));
	}

	@DeleteMapping
	ResponseEntity<Void> delete(Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/name/{name}")
	ResponseEntity<List<Pilot>> findByNameStartsWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartsWithIgnoreCase(name));
	}

	@GetMapping("/country/{idPais}")
	ResponseEntity<List<Pilot>> findByCountry(@PathVariable Integer idPais) {
		return ResponseEntity.ok(service.findByCountry(countryService.findById(idPais)));
	}
	
	@GetMapping("/team/{idTeam}")
	ResponseEntity<List<Pilot>> findByTeam(@PathVariable Integer idTeam) {
		return ResponseEntity.ok(service.findByTeam(teamService.findById(idTeam)));
	}

}
