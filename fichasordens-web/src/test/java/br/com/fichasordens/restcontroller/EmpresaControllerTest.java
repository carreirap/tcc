package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.Empresa;
import br.com.fichasordens.Endereco;
import br.com.fichasordens.dto.EmpresaDto;

public class EmpresaControllerTest {
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private EmpresaController empresaController;

	@Mock
	private Empresa mockEmpresa;
	
	@Test
	public void test_getEmpresa_sucess() {
		
		when(this.mockEmpresa.buscarEmpresa()).thenReturn(carregarEmpresa());
		
		ResponseEntity response = empresaController.getEmpresa();	
			
		assertNotNull(response.getBody());
	}
	
	@Test
	public void test_salvarEmpresa_sucess() {
		
		when(this.mockEmpresa.salvarEmpresa(org.mockito.Mockito.any(Empresa.class))).thenReturn(carregarEmpresa());
		
		ResponseEntity response = this.empresaController.salvarEmpresa(carregarDto());
		
		assertNotNull(response.getBody());
	}
	
	private EmpresaDto carregarDto() {
		EmpresaDto dto = new EmpresaDto();
		dto.setBairro("Test");
		dto.setCep("00000000");
		dto.setCidade("test");
		dto.setCnpj("000000000000");
		dto.setComplemento("");
		dto.setEmail("email@email.com");
		dto.setEstado("TST");
		dto.setFone("99999999999");
		dto.setLogradouro("");
		dto.setNome("Test");
		dto.setNumero(0);
		dto.setSite("www.site.com.br");
		
		return dto;
	}
	
	private Empresa carregarEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setCnpj("76189400000289");
		empresa.setEmail("test@email.com");
		
		empresa.setEndereco(new Endereco());
		empresa.getEndereco().setBairro("Test");
		empresa.getEndereco().setCep("0000000000");
		empresa.getEndereco().setCidade("Test");
		empresa.getEndereco().setComplemento("");
		empresa.getEndereco().setEstado("Test");
		empresa.getEndereco().setId(1);
		empresa.getEndereco().setLogradouro("Test");
		empresa.getEndereco().setNumero(890);
		
		empresa.setFone("9999999999");
		empresa.setNome("Test");
		empresa.setSite("www.site.test.com");
		return empresa;
	}
}
