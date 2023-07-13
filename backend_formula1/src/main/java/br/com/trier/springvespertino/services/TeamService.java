package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Team;

public interface TeamService {

	Team salvar(Team team);

	List<Team> listAll();

	Team findById(Integer id);

	Team update(Team team);

	void delete(Integer id);

	List<Team> findByNameIgnoreCase(String name);

	List<Team> findByNameContains(String name);

}
