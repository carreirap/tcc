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
		
		return new ResponseEntity<>(this.convertEmpresaParaDto(emp), HttpStatus.OK);
	}
	
	@GetMapping
	 public ResponseEntity<EmpresaDto> getEmpresa() {
		LOGGER.info("Buscar dados da empresa");
				
		final Empresa emp = this.empresa.buscarEmpresa();
		 
		return new ResponseEntity<>(this.convertEmpresaParaDto(emp),HttpStatus.OK);
	 }
	
	private EmpresaDto convertEmpresaParaDto (final Empresa empresa) {
		final EmpresaDto dto = new EmpresaDto();		
		
		dto.setNome(empresa.getNome());
		dto.setCnpj(empresa.getCnpj());
		dto.setBairro(empresa.getEndereco().getBairro());
		dto.setCep(empresa.getEndereco().getCep());
		dto.setCidade(empresa.getEndereco().getCidade());
		dto.setComplemento(empresa.getEndereco().getComplemento());
		dto.setEmail(empresa.getEmail());
		dto.setEstado(empresa.getEndereco().getEstado());
		dto.setFone(empresa.getFone());
		dto.setLogradouro(empresa.getEndereco().getLogradouro());
		dto.setNumero(empresa.getEndereco().getNumero());
		dto.setSite(empresa.getSite());
		dto.setIdEndereco(empresa.getEndereco().getId());
		return dto;
	}
	
	private Empresa convertDtoParaEmpresa(EmpresaDto dto) {
		final Empresa emp = new Empresa();
		emp.setCnpj(dto.getCnpj());
		emp.setEmail(dto.getEmail());
		emp.setFone(dto.getFone());
		emp.setNome(dto.getNome());
		emp.setSite(dto.getSite());
		Endereco end = converteDtoParaEndereco(dto);
		emp.setEndereco(end);
		return emp;
	}

	private Endereco converteDtoParaEndereco(EmpresaDto dto) {
		Endereco end = new Endereco();
		end.setBairro(dto.getBairro());
		end.setCep(dto.getCep());
		end.setCidade(dto.getCidade());
		end.setComplemento(dto.getComplemento());
		end.setEstado(dto.getEstado());
		end.setLogradouro(dto.getLogradouro());
		end.setNumero(dto.getNumero());
		if (dto.getIdEndereco() != 0) {
			end.setId(dto.getIdEndereco());
		}
		return end;
	}
	
	
}
