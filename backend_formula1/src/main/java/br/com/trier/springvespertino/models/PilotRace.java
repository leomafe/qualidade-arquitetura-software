package br.com.trier.springvespertino.models;

import br.com.trier.springvespertino.models.dto.PilotRaceDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "piloto_corrida")
public class PilotRace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@Setter
	private Integer id;

	@Column(name = "colocacao")
	private Integer placement;

	@ManyToOne
	private Pilot pilot;

	@ManyToOne
	private Race race;

	public PilotRace(PilotRaceDTO dto, Pilot pilot, Race race) {
		this(dto.getId(), dto.getPlacement(), pilot, race);
	}

	public PilotRaceDTO toDTO() {
		return new PilotRaceDTO(id, placement, pilot.getId(), pilot.getName(), race.getId());
	}

}