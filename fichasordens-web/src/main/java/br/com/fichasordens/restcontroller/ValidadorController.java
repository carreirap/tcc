package br.com.fichasordens.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.util.ValidadorCNPJ;
import br.com.fichasordens.util.ValidadorCPF;

@RestController
@RequestMapping("/validador")
@EnableResourceServer
public class ValidadorController {

	@RequestMapping(method = RequestMethod.POST, value="/cpf")
	public ResponseEntity validarCpf(@RequestBody final String cpf) {
		if (ValidadorCPF.validarCPF(cpf)) {
			return new ResponseEntity<>(new MensagemRetornoDto("OK"), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new MensagemRetornoDto("CPF informado inválido"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/cnpj")
	public ResponseEntity validarCnpj(@RequestBody final String cnpj) {
		if (ValidadorCNPJ.validarCNPJ(cnpj)) {
			return new ResponseEntity<>(new MensagemRetornoDto("OK"), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new MensagemRetornoDto("CNPJ informado inválido"), HttpStatus.BAD_REQUEST);
		}
	}
}
