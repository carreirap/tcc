package br.com.fichasordens.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Parametro;
import br.com.fichasordens.dto.ParametroDto;

@RestController
@RequestMapping("/parametro")
@EnableResourceServer 
public class ParametroController {
	
	private static final Logger LOGGER = LogManager.getLogger(ParametroController.class);
	
	@Autowired
	private Parametro parametro;

	 @RequestMapping(method = RequestMethod.GET)
	 public ResponseEntity<List<ParametroDto>> getUsuario(@RequestParam(required=false) final String user) {
		 final List<Parametro> lst = parametro.buscarParametros();
		 LOGGER.info("listando parametros");
		 return new ResponseEntity<List<ParametroDto>>(converterParaDto(lst),HttpStatus.OK);
	 }
	 
	 
	 private List<ParametroDto> converterParaDto(final List<Parametro> list) {
		 List<ParametroDto> listDto = new ArrayList<ParametroDto>();
		 
		 list.forEach(p->{
			 ParametroDto dto = new ParametroDto();
			 dto.setDescricao(p.getDescricao());
			 dto.setValor(p.getValor());
			 dto.setId(p.getId());
		 });
		 return listDto;
	 }
}
