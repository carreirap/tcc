package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.dto.ClienteDto;
import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.EnderecoEntity;
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
	
	@Test
	public void test_salvarCliente_fail() {
		
		try {
			doThrow(new ExcecaoRetorno()).when(this.mockCliente).salvarCliente(org.mockito.Mockito.any(Cliente.class));
		} catch (ExcecaoRetorno e) {
			e.printStackTrace();
		}
		
		ResponseEntity response = this.clienteController.salvarCliente(new ClienteDto());
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_pesquisarClienteCNPJ_success() {
		Pageable page = new PageRequest(0, 10);
		Page<ClienteEntity> paged = Mockito.mock(Page.class);
		when(this.mockCliente.pesquisarCliente(org.mockito.Mockito.any(Cliente.class), org.mockito.Mockito.any(Pageable.class))).thenReturn(paged);
		this.clienteController.pesquisarCliente("76189406000126", "", page);
		
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisarClienteNome_success() {
		Pageable pageable = new PageRequest(0, 10);
		Page<ClienteEntity> paged = new PageImpl<ClienteEntity>(Arrays.asList(this.createClienteEntity()), pageable, 10); 
		when(this.mockCliente.pesquisarCliente(org.mockito.Mockito.any(Cliente.class), org.mockito.Mockito.any(Pageable.class))).thenReturn(paged);
		this.clienteController.pesquisarCliente("", "Paul", pageable);
		
		assertNotNull(paged);
	}

	public ClienteEntity createClienteEntity() {
		ClienteEntity cli = new ClienteEntity();
		cli.setCelular("123456789");
		cli.setCnpjCpf("454646546465");
		cli.setEmail("test@email.com.br");
		cli.setEndereco(new EnderecoEntity());
		cli.getEndereco().setBairro("12313");
		cli.getEndereco().setCep("16370000");
		cli.getEndereco().setCidade("Curitiba");
		cli.getEndereco().setComplemento("");
		cli.getEndereco().setEstado("PR");
		cli.getEndereco().setId(11L);
		cli.getEndereco().setLogradouro("XV de novembro");
		cli.getEndereco().setNumero(999);
		cli.setFone("456789464");
		cli.setId(10L);
		cli.setNome("Test de Souza");
		return cli;
		
	}
}
