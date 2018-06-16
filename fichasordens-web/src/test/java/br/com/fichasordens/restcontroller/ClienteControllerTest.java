package br.com.fichasordens.restcontroller;

import static org.mockito.Mockito.doNothing;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.dto.ClienteDto;
import br.com.fichasordens.exception.ExcecaoRetorno;

public class ClienteControllerTest {
	
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private ClienteController clienteController;
	
	@Mock
	private Cliente mockCliente;
	
	@Test
	public void test_salvarCliente_success() {
		
		try {
			doNothing().when(this.mockCliente).salvarCliente(org.mockito.Mockito.any(Cliente.class));
		} catch (ExcecaoRetorno e) {
			e.printStackTrace();
		}
		
		ResponseEntity response = this.clienteController.salvarCliente(new ClienteDto());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
