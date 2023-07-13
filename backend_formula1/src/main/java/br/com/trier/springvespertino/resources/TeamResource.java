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

import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.services.TeamService;

@RestController
@RequestMapping("/teams")
public class TeamResource {
	
	@Autowired
	private TeamService service;
	
	@PostMapping
	public ResponseEntity<Team> insert(@RequestBody Team equipe) {
		Team newEquipe = service.salvar(equipe);
		return newEquipe != null ? ResponseEntity.ok(newEquipe) : ResponseEntity.badRequest().build();
	}
	
	@GetMapping
	public ResponseEntity<List<Team>> listarTodos(){
		List<Team> lista = service.listAll();
		return lista != null ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}
	
	@GetMapping ("/{id}")
	public ResponseEntity<Team> buscaPorCodigo(@PathVariable Integer id) {
		Team newEquipe = service.findById(id);
		return newEquipe != null ? ResponseEntity.ok(newEquipe) : ResponseEntity.noContent().build();
	}
	
	@PutMapping ("/{id}")
	public ResponseEntity<Team> update(@PathVariable Integer id, @RequestBody Team equipe){
		equipe.setId(id);
		equipe = service.update(equipe);
		return equipe != null ? ResponseEntity.ok(equipe) : ResponseEntity.badRequest().build();
	}
	
	@DeleteMapping ("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping ("/name/{name}")
	public ResponseEntity<List<Team>> buscaPorNome(@PathVariable String name) {
		List<Team> lista = service.findByNameIgnoreCase(name);
		return lista.size() > 0 ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}
	
	@GetMapping ("/name/contains/{name}")
	public ResponseEntity<List<Team>> buscaPorNomeContem(@PathVariable String name) {
		List<Team> lista = service.findByNameContains(name);
		return lista.size() > 0 ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}

}
