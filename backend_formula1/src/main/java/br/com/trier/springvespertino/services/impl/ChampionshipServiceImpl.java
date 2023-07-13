package br.com.trier.springvespertino.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.repositories.ChampionshipRepository;
import br.com.trier.springvespertino.services.ChampionshipService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;

@Service
public class ChampionshipServiceImpl implements ChampionshipService {

	@Autowired
	private ChampionshipRepository repository;

	private void validYear(Championship championship) {
		if (championship.getYear() == null) {
			throw new IntegrityViolation("Ano não pode ser nulo");
		}
		if (championship.getYear() < 1990 || championship.getYear() > LocalDateTime.now().getYear() + 1) {
			throw new IntegrityViolation("Ano inválido: %s".formatted(championship.getYear()));
		}
	}

	@Override
	public Championship findById(Integer id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Championship insert(Championship championship) {
		validYear(championship);
		return repository.save(championship);
	}

	@Override
	public List<Championship> listAll() {
		return repository.findAll();
	}

	@Override
	public Championship update(Championship championship) {
		validYear(championship);
		return repository.save(championship);
	}

	@Override
	public void delete(Integer id) {
		Championship championship = findById(id);
		if (championship != null) {
			repository.delete(championship);
		}
	}

	@Override
	public List<Championship> findByYearBetween(Integer start, Integer end) {
		return repository.findByYearBetween(start, end);
	}

	@Override
	public List<Championship> findByYear(Integer year) {
		return repository.findByYear(year);
	}

	@Override
	public List<Championship> findByDescriptionContainsIgnoreCase(String descricao) {
		return repository.findByDescriptionContainsIgnoreCase(descricao);
	}

	@Override
	public List<Championship> findByescriptionContainsIgnoreCaseAndAnoEquals(String descricao, Integer ano) {
		return repository.findByDescriptionContainsIgnoreCaseAndYearEquals(descricao, ano);
	}

}
