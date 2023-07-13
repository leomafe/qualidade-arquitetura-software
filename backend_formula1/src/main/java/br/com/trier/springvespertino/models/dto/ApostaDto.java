package br.com.trier.springvespertino.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApostaDto {
	
	private List<Integer> dice;
	private Integer sum;
	private Double percent;
	private String message;

}
