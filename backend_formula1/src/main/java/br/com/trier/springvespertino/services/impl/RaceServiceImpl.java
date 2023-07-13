package br.com.trier.springvespertino.services.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.repositories.RaceRepository;
import br.com.trier.springvespertino.services.RaceService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class RaceServiceImpl implements RaceService {

	@Autowired
	private RaceRepository repository;

	private void validateRace(Race race) {
		if (race.getChampionship() == null) {
			throw new IntegrityViolation("Campeonato não pode ser nulo");
		}
		if (race.getDate() == null) {
			throw new IntegrityViolation("Data inválida");
		}
		int championShipYear = race.getChampionship().getYear();
		int raceYear = race.getDate().getYear();
		if (championShipYear != raceYear) {
			throw new IntegrityViolation("Ano da corrida diferente do ano do campeonato");
		}
	}

	@Override
	public Race findById(Integer id) {
		return repository.findById(id)
				.orElseThrow(()->new ObjectNotFound("Corrida %s não existe".formatted(id)));
	}

	@Override
	public Race insert(Race race) {
		validateRace(race);
		return repository.save(race);
	}

	@Override
	public List<Race> listAll() {
		List<Race> lista = repository.findAll();
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Não existem corridas cadastradas");
		}
		return lista;
	}

	@Override
	public Race update(Race race) {
		findById(race.getId());
		validateRace(race);
		return repository.save(race);
	}

	@Override
	public void delete(Integer id) {
		repository.delete(findById(id));
	}

	@Override
	public List<Race> findByDate(ZonedDateTime date) {
		List<Race> lista = repository.findByDate(date);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Não existe corrida para a data especificada");
		}
		return lista;
	}

	@Override
	public List<Race> findBySpeedway(Speedway speedway) {
		List<Race> lista = repository.findBySpeedway(speedway);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Não existe corrida na pista especificada");
		}
		return lista;
	}

	@Override
	public List<Race> findByChampionship(Championship championship) {
		List<Race> lista = repository.findByChampionship(championship);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Não existe corrida para o campeonato especificado");
		}
		return lista;
	}

}
