package br.com.fichasordens.restcontroller;

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
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Empresa;
import br.com.fichasordens.Endereco;
import br.com.fichasordens.dto.EmpresaDto;
import br.com.fichasordens.web.util.ConversorEmpresa;
import br.com.fichasordens.web.util.ConverterEndereco;

@RestController
@RequestMapping("/empresa")
@EnableResourceServer 
public class EmpresaController {
	
	private static final Logger LOGGER = LogManager.getLogger(EmpresaController.class);
	
	@Autowired private Empresa empresa;
	
	@SuppressWarnings("rawtypes")
	@PostMapping
	public ResponseEntity salvarEmpresa(@RequestBody final EmpresaDto dto	) {
		LOGGER.info("Salvar dados da empresa");
		final Empresa emp = this.empresa.salvarEmpresa(this.convertDtoParaEmpresa(dto));
		
		return new ResponseEntity<>(ConversorEmpresa.convertEmpresaParaDto(emp), HttpStatus.OK);
	}
	
	@GetMapping
	 public ResponseEntity<EmpresaDto> getEmpresa() {
		LOGGER.info("Buscar dados da empresa");
				
		final Empresa emp = this.empresa.buscarEmpresa();
		 
		return new ResponseEntity<>(ConversorEmpresa.convertEmpresaParaDto(emp),HttpStatus.OK);
	 }
	
	
	
	private Empresa convertDtoParaEmpresa(EmpresaDto dto) {
		final Empresa emp = new Empresa();
		emp.setCnpj(dto.getCnpj());
		emp.setEmail(dto.getEmail());
		emp.setFone(dto.getFone());
		emp.setNome(dto.getNome());
		emp.setSite(dto.getSite());
		Endereco end = ConverterEndereco.converteDtoParaEndereco(dto);
		emp.setEndereco(end);
		return emp;
	}

	
	
	
}
