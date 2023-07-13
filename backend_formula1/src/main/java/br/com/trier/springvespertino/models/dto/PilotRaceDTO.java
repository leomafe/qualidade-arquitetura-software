package br.com.trier.springvespertino.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PilotRaceDTO {
	
	private Integer id;
	private Integer placement;
	private Integer idPilot;
	private String namePilot;
	private Integer idRace;

}