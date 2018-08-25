package br.com.fichasordens.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.dto.HistoricoPesquisaDto;
import br.com.fichasordens.service.HistoricoService;
import br.com.fichasordens.util.ResultadoPesquisaDto;

@RestController
@RequestMapping("/historico")
@EnableResourceServer
public class HistoricoController {
	
	@Autowired
	private HistoricoService historicoService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity pesquisar(@RequestBody final HistoricoPesquisaDto dto) {
		List<ResultadoPesquisaDto> list = this.historicoService.pesquisar(dto.getTipo(), dto.getNumero(), dto.getCnpjcpf(), dto.getIdResponsavel(), dto.getSituacao());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

}
