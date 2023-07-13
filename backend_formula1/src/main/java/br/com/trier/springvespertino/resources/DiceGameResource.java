package br.com.trier.springvespertino.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.dto.ApostaDto;

@RestController
@RequestMapping(value = "/dados")
public class DiceGameResource {
	
	@GetMapping("jogar/{qtDados}/{aposta}")
	public ResponseEntity<ApostaDto> jogar(@PathVariable int qtDados, @PathVariable int aposta) {
		ApostaDto retorno = null;
		if(! validaDados(qtDados)) {
			retorno = new ApostaDto(null, null, null, "Quantidade de dados inválida. Permitido entre 1 e 4" );
			return new ResponseEntity<ApostaDto>(retorno, HttpStatus.NO_CONTENT);
		}
		if(!validaAposta(qtDados, aposta)) {
			retorno = new ApostaDto(null, null, null, "Valor da aposta inválida de acordo com a quantidade de dados informada" );
			return new ResponseEntity<ApostaDto>(retorno, HttpStatus.NO_CONTENT);
		}
		
		int soma = 0;
		List<Integer> sorteados = new ArrayList<Integer>();
		for(int i=0; i<qtDados; i++) {
			int vl = sorteia();
			sorteados.add(vl);
			soma += vl;
		}
		
		if(soma == aposta) {
			retorno = new ApostaDto(sorteados, soma, null, "Parabéns, você acertou!");
		}else {
			retorno = new ApostaDto(sorteados, soma, percDif(aposta, soma), "Não foi desta vez!");
		}
		return ResponseEntity.ok(retorno);
	}
	
	private boolean validaAposta(int qtDados, int aposta) {
		int min = qtDados;
		int max = qtDados * 6;
		if(aposta < min || aposta > max) {
			return false;
		}
		return true;
	}
	
	private boolean validaDados(int qtDados) {
		return qtDados >= 1 && qtDados <= 4;
	}
	
	private int sorteia() {
		Random r = new Random();
		return r.nextInt(6) + 1;
	}
	
	private double percDif(int n1, int n2) {
		double diferenca = Math.abs(n1 - n2);
		return (diferenca / Math.max(n1, n2)) * 100;
	}
	

}
