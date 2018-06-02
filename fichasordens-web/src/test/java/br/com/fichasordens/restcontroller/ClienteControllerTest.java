package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.dto.ClienteDto;

public class ClienteControllerTest {
	
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private ClienteController clienteController;
	
	@Test
	public void test_salvarCliente_success() {
		ResponseEntity response = this.clienteController.salvarCliente(new ClienteDto());
		assertNotNull(response);
	}

}
