package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Country;

public interface CountryService {

	Country salvar(Country country);

	Country update(Country country);

	void delete(Integer id);

	List<Country> listAll();

	Country findById(Integer id);

	List<Country> findByNomeEqualsIgnoreCase(String nome);

}
