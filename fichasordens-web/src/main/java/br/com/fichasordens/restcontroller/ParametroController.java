package br.com.fichasordens.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Parametro;
import br.com.fichasordens.dto.ParametroDto;
import br.com.fichasordens.exception.ExcecaoRetorno;

@RestController
@RequestMapping("/parametro")
@EnableResourceServer
public class ParametroController {

	private static final Logger LOGGER = LogManager.getLogger(ParametroController.class);

	@Autowired
	private Parametro parametro;

	@GetMapping
	public ResponseEntity<List<ParametroDto>> recuperarParametros(@RequestParam(required = false) final String user) {
		final List<Parametro> lst = parametro.recuperarParametros();
		LOGGER.info("Listando todos parametros");
		return new ResponseEntity<>(converterParaDto(lst), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity salvarParametro(@RequestBody final List<ParametroDto> listDto) {
		LOGGER.info("Salvar dados da empresa");
		try {
			this.validarParametros(listDto);
			this.parametro.salvarParametro(this.convertDtoParaParametro(listDto));
		} catch (ExcecaoRetorno e) {
			LOGGER.error("Erro salvando parametros", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private List<Parametro> convertDtoParaParametro(final List<ParametroDto> listDto) {
		List<Parametro> list = new ArrayList<>();
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
		List<ParametroDto> listDto = new ArrayList<>();

		list.forEach(p -> {
			ParametroDto dto = new ParametroDto();
			dto.setDescricao(p.getDescricao());
			dto.setValor(p.getValor());
			dto.setId(p.getId());
			listDto.add(dto);
		});
		return listDto;
	}
	
	private void validarParametros(final List<ParametroDto> listDto) throws ExcecaoRetorno {
		for(ParametroDto p : listDto) {
			if (p.getId() == 0) {
				throw new ExcecaoRetorno("Parametro inválido");
			}
			if (p.getValor() == null || p.getValor().doubleValue() == 0) {
				throw new ExcecaoRetorno("Valor do parametro é inválido");
			}
		}
	}
}
