package br.com.fichasordens.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@SuppressWarnings("rawtypes")
	@PostMapping
	public ResponseEntity pesquisar(@RequestBody final HistoricoPesquisaDto dto, final Pageable pageable) {
		Page<ResultadoPesquisaDto> pages = this.historicoService.pesquisar(dto.getTipo(), dto.getNumero(), dto.getCnpjcpf(), dto.getSituacao(), pageable);
		return new ResponseEntity<>(pages, HttpStatus.OK);
	}

}
