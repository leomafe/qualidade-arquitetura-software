package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Team;

public interface PilotService {

	Pilot findById(Integer id);

	Pilot insert(Pilot pilot);

	List<Pilot> listAll();

	Pilot update(Pilot pilot);

	void delete(Integer id);

	List<Pilot> findByNameStartsWithIgnoreCase(String name);

	List<Pilot> findByCountry(Country country);

	List<Pilot> findByTeam(Team team);

}
