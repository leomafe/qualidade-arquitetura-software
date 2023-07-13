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

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.services.CountryService;

@RestController
@RequestMapping("/countries")
public class CountryResources {
	
	@Autowired
	private CountryService service;
	
	@PostMapping
	public ResponseEntity<Country> save(@RequestBody Country pais){
		Country newPais = service.salvar(pais);
		return newPais != null ? ResponseEntity.ok(newPais) : ResponseEntity.badRequest().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Country> update(@PathVariable Integer id, @RequestBody Country pais){
		pais.setId(id);
		pais = service.update(pais);
		return pais != null ? ResponseEntity.ok(pais) : ResponseEntity.badRequest().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping()
	public ResponseEntity<List<Country>> listAll(){
		List<Country> paises = service.listAll();
		return paises.size()>0 ? ResponseEntity.ok(paises) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Country> findById(@PathVariable Integer id){
		Country pais = service.findById(id);
		return pais != null ? ResponseEntity.ok(pais) : ResponseEntity.noContent().build();
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Country>> findByNomeEqualsIgnoreCase(@PathVariable String name){
		List<Country> lista = service.findByNomeEqualsIgnoreCase(name);
		return lista.size() > 0 ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}

}
