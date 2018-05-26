package br.com.fichasordens.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Empresa;
import br.com.fichasordens.Parametro;
import br.com.fichasordens.dto.EmpresaDto;
import br.com.fichasordens.dto.ParametroDto;

@RestController
@RequestMapping("/parametro")
@EnableResourceServer
public class ParametroController {

	private static final Logger LOGGER = LogManager.getLogger(ParametroController.class);

	@Autowired
	private Parametro parametro;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ParametroDto>> getUsuario(@RequestParam(required = false) final String user) {
		final List<Parametro> lst = parametro.buscarParametros();
		LOGGER.info("Listando todos parametros");
		return new ResponseEntity<List<ParametroDto>>(converterParaDto(lst), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvarParametro(@RequestBody final List<ParametroDto> listDto) {
		LOGGER.info("Salvar dados da empresa");
		this.parametro.salvarParametro(this.convertDtoParaParametro(listDto));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private List<Parametro> convertDtoParaParametro(final List<ParametroDto> listDto) {
		List<Parametro> list = new ArrayList<Parametro>();
		listDto.forEach(p -> {
			Parametro param = new Parametro();
			param.setDescricao(p.getDescricao());
			param.setValor(p.getValor());
			param.setId(p.getId());
			list.add(param);
		});
		return list;
	}

	private List<ParametroDto> converterParaDto(final List<Parametro> list) {
		List<ParametroDto> listDto = new ArrayList<ParametroDto>();

		list.forEach(p -> {
			ParametroDto dto = new ParametroDto();
			dto.setDescricao(p.getDescricao());
			dto.setValor(p.getValor());
			dto.setId(p.getId());
			listDto.add(dto);
		});
		return listDto;
	}
}
