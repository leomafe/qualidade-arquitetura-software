package br.com.trier.springvespertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.repositories.PilotRepository;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class PilotServiceImpl implements PilotService {

	@Autowired
	private PilotRepository repository;

	@Override
	public Pilot findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("Pilot %s não existe".formatted(id)));
	}

	@Override
	public Pilot insert(Pilot pilot) {
		return repository.save(pilot);
	}

	@Override
	public List<Pilot> listAll() {
		List<Pilot> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto cadastrado");
		}
		return lista;
	}

	@Override
	public Pilot update(Pilot pilot) {
		findById(pilot.getId());
		return repository.save(pilot);
	}

	@Override
	public void delete(Integer id) {
		repository.delete(findById(id));

	}

	@Override
	public List<Pilot> findByNameStartsWithIgnoreCase(String name) {
		List<Pilot> lista = repository.findByNameStartsWithIgnoreCase(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto com esse nome");
		}
		return lista;
	}

	@Override
	public List<Pilot> findByCountry(Country country) {
		List<Pilot> lista = repository.findByCountry(country);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto nesse país");
		}
		return lista;
	}

	@Override
	public List<Pilot> findByTeam(Team team) {
		List<Pilot> lista = repository.findByTeam(team);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto nesse time");
		}
		return lista;
	}

}
