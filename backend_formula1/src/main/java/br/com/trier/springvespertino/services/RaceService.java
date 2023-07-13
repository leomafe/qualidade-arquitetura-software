package br.com.trier.springvespertino.services;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Speedway;

public interface RaceService {

	Race findById(Integer id);

	Race insert(Race race);

	List<Race> listAll();

	Race update(Race race);

	void delete(Integer id);

	List<Race> findByDate(ZonedDateTime date);

	List<Race> findBySpeedway(Speedway speedway);

	List<Race> findByChampionship(Championship championship);

}
