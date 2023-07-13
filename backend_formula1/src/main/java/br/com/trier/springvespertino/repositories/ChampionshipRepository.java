package br.com.trier.springvespertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Championship;

@Repository
public interface ChampionshipRepository extends JpaRepository<Championship, Integer>{
	
	List<Championship> findByYearBetween(Integer start, Integer end);
	List<Championship> findByYear(Integer year);
	List<Championship> findByDescriptionContainsIgnoreCase(String descricao);
	List<Championship> findByDescriptionContainsIgnoreCaseAndYearEquals(String descricao, Integer ano);
	

}
