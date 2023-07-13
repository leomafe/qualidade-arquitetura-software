package br.com.trier.springvespertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.repositories.TeamRepository;
import br.com.trier.springvespertino.services.TeamService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class TeamServiceImpl implements TeamService{
	
	@Autowired
	private TeamRepository repository;
	
	private void findByName(Team team) {
		List<Team> lista = repository.findByNameIgnoreCase(team.getName());
		if(lista.size()>1 && !lista.get(0).getId().equals(team.getId())) {
			throw new IntegrityViolation("Nome já existente: %s".formatted(team.getName()));
		}
	}

	@Override
	public Team salvar(Team team) {
		findByName(team);
		return repository.save(team);
	}

	@Override
	public List<Team> listAll() {
		List<Team> lista = repository.findAll();
		if(lista.size()==0) {
			throw new ObjectNotFound("Não existe equipes cadastradas");
		}
		return lista;
	}

	@Override
	public Team findById(Integer id) {
		Optional<Team> team = repository.findById(id);
		return team.orElseThrow(()->new ObjectNotFound("Equipe %s não encontrada".formatted(id)));
	}

	@Override
	public Team update(Team team) {
		findById(team.getId());
		findByName(team);
		return repository.save(team);
	}

	@Override
	public void delete(Integer id) {
		Team team = findById(id);
		repository.delete(team);
		
	}

	@Override
	public List<Team> findByNameIgnoreCase(String name) {
		List<Team> lista = repository.findByNameIgnoreCase(name);
		if(lista.size()==0) {
			throw new ObjectNotFound("Equipe %s não encontrada".formatted(name));
		}
		return lista;
	}

	@Override
	public List<Team> findByNameContains(String name) {
		List<Team> lista = repository.findByNameContains(name);
		if(lista.size()==0) {
			throw new ObjectNotFound("Nome %s não encontrado em nenhuma equipe".formatted(name));
		}
		return lista;
	}

}
