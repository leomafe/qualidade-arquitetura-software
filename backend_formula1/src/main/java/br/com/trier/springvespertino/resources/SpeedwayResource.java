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

import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.services.CountryService;
import br.com.trier.springvespertino.services.SpeedwayService;

@RestController
@RequestMapping("/speedway")
public class SpeedwayResource {

	@Autowired
	private SpeedwayService service;

	@Autowired
	private CountryService countryService;

	@GetMapping("/{id}")
	public ResponseEntity<Speedway> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	ResponseEntity<Speedway> insert(@RequestBody Speedway speedway) {
		countryService.findById(speedway.getCountry().getId());
		return ResponseEntity.ok(service.insert(speedway));
	}

	@GetMapping
	ResponseEntity<List<Speedway>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	@PutMapping("/{id}")
	ResponseEntity<Speedway> update(@PathVariable Integer id, @RequestBody Speedway speedway) {
		countryService.findById(speedway.getCountry().getId());
		speedway.setId(id);
		return ResponseEntity.ok(service.update(speedway));
	}

	@DeleteMapping
	ResponseEntity<Void> delete(Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/name/{name}")
	ResponseEntity<List<Speedway>> findByNameStartsWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartsWithIgnoreCase(name));
	}

	@GetMapping("/size/{sizeIn}/{sizeFin}")
	ResponseEntity<List<Speedway>> findBySizeBetween(@PathVariable Integer sizeIn, @PathVariable Integer sizeFin) {
		return ResponseEntity.ok(service.findBySizeBetween(sizeIn, sizeFin));
	}

	@GetMapping("/country/{idPais}")
	ResponseEntity<List<Speedway>> findByCountryOrderBySizeDesc(@PathVariable Integer idPais) {
		return ResponseEntity.ok(service.
				findByCountryOrderBySizeDesc(countryService.findById(idPais)));
	}

}
