package br.com.trier.springvespertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.PilotRace;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.repositories.PilotRaceRepository;
import br.com.trier.springvespertino.services.PilotRaceService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class PilotRaceServiceImpl implements PilotRaceService {

	@Autowired
	private PilotRaceRepository repository;

	private void checkPilotRace(PilotRace pilotRace) {
		
		if (pilotRace.getPlacement() == null) {
			throw new IntegrityViolation("Colocacao null!");
		}
		if (pilotRace.getPlacement() == 0) {
			throw new IntegrityViolation("Colocacao zero!");
		}
	}

	@Override
	public PilotRace findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("ID %s inválido!".formatted(id)));
	}

	@Override
	public PilotRace insert(PilotRace pilotRace) {
		checkPilotRace(pilotRace);
		return repository.save(pilotRace);
	}

	@Override
	public List<PilotRace> listAll() {
		List<PilotRace> list = repository.findAll();
		if (list.isEmpty()) {
			throw new ObjectNotFound("Nenhum PilotoCorrida cadastrado!");
		}
		return list;
	}

	@Override
	public PilotRace update(PilotRace pilotRace) {
		findById(pilotRace.getId());
		checkPilotRace(pilotRace);
		return repository.save(pilotRace);
	}

	@Override
	public void delete(Integer id) {
		PilotRace pilotRace = findById(id);
		repository.delete(pilotRace);

	}

	@Override
	public List<PilotRace> findByPlacement(Integer placement) {
		List<PilotRace> list = repository.findByPlacement(placement);
		if (list.isEmpty()) {
			throw new ObjectNotFound("Nenhum PilotoCorrida nesta posição!");
		}
		return list;
	}

	@Override
	public List<PilotRace> findByPilot(Pilot pilot) {
		List<PilotRace> list = repository.findByPilot(pilot);
		if (list.isEmpty()) {
			throw new ObjectNotFound("Nenhum PilotoCorrida com esse piloto!");
		}
		return list;
	}

	@Override
	public List<PilotRace> findByRaceOrderByPlacementAsc(Race race) {
		List<PilotRace> list = repository.findByRaceOrderByPlacementAsc(race);
		if (list.isEmpty()) {
			throw new ObjectNotFound("Nenhum PilotoCorrida nesta corrida!");
		}
		return list;
	}

	@Override
	public List<PilotRace> findByPlacementBetweenAndRace(Integer placementIn, Integer placementFin, Race race) {
		List<PilotRace> list = repository.findByPlacementBetweenAndRace(placementIn, placementFin, race);
		if (list.isEmpty()) {
			throw new ObjectNotFound("Nenhum PilotoCorrida com esses parâmetros de busca!");
		}
		return list;
	}

	@Override
	public PilotRace findByPilotAndRace(Pilot pilot, Race race) {
		PilotRace pilotRace = repository.findByPilotAndRace(pilot, race);
		if (pilotRace == null) {
			throw new ObjectNotFound("Nenhum PilotoCorrida com esses parâmetros de busca!");
		}
		return pilotRace;
	}

}
