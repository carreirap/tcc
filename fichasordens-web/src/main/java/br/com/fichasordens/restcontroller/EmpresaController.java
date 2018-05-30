package br.com.fichasordens.restcontroller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvarEmpresa(@RequestBody final EmpresaDto dto	) {
		LOGGER.info("Salvar dados da empresa");
		final Empresa emp = this.empresa.salvarEmpresa(this.convertDtoParaEmpresa(dto));
		
		return new ResponseEntity<>(this.convertEmpresaParaDto(emp), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	 public ResponseEntity<EmpresaDto> getEmpresa() {
		LOGGER.info("Buscar dados da empresa");
				
		final Empresa empresa = this.empresa.buscarEmpresa();
		 
		return new ResponseEntity<EmpresaDto>(this.convertEmpresaParaDto(empresa),HttpStatus.OK);
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
		 
//		 dto.setNome("IslucNet");
//		 dto.setCnpj("76189406000126");
//		 dto.setBairro("Capão Raso");
//		 dto.setCep("81070-310");
//		 dto.setCidade("Curitiba");
//		 dto.setComplemento("n/a");
//		 dto.setEmail("israel@islucnet.com.br");
//		 dto.setEstado("PR");
//		 dto.setFone("(41)33459299");
//		 dto.setLogradouro("Rua das flores");
//		 dto.setNumero(500);
//		 dto.setSite("www.islucnet.com.br");
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
		return end;
	}
	
	
}
