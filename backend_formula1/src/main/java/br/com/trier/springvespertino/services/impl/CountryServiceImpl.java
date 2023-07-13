package br.com.trier.springvespertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.repositories.CountryRepository;
import br.com.trier.springvespertino.services.CountryService;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class CountryServiceImpl implements CountryService{
	
	@Autowired
	private CountryRepository repository;

	@Override
	public Country salvar(Country country) {
		return repository.save(country);
	}

	@Override
	public Country update(Country country) {
		return repository.save(country);
	}

	@Override
	public void delete(Integer id) {
		Country country = findById(id);
		if(country != null) {
			repository.delete(country);
		}
		
	}

	@Override
	public List<Country> listAll() {
		return repository.findAll();
	}

	@Override
	public Country findById(Integer id) {
		Optional<Country> country = repository.findById(id);
		return country.orElseThrow(()->new ObjectNotFound("País não existe"));
	}

	@Override
	public List<Country> findByNomeEqualsIgnoreCase(String nome) {
		return repository.findByNameEqualsIgnoreCase(nome);
	}

}
