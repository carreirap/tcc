package br.com.fichasordens;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.ClienteRepository;

public class ClienteTest {

	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private Cliente cliente;
	
	@Mock
	private Endereco endereco;
	
	@Mock
	private ClienteRepository mockClienteRepository;
	
	@Test
	public void test_salvarCliente_sucess() throws ExcecaoRetorno {
		when(this.mockClienteRepository.save(org.mockito.Mockito.any(ClienteEntity.class))).thenReturn(new ClienteEntity());
		when(this.endereco.salvarEndereco(loadEndereco())).thenReturn(loadEndereco());
		
		this.cliente.salvarCliente(loadCliente());
		
		Mockito.verify(this.mockClienteRepository, Mockito.times(1)).save(org.mockito.Mockito.any(ClienteEntity.class));
	}
	
	private Cliente loadCliente() {
		Cliente cli = new Cliente();
		cli.setCelular("99999999999999");
		cli.setCnpjCpf("cnpjCpf");
		cli.setEmail("test");
		cli.setFone("988888888");
		cli.setNome("test");
		cli.setEndereco(this.loadEndereco());
		return cli;
	}
	
	private Endereco loadEndereco() {
		Endereco end = new Endereco(); 
		end.setBairro("test");
		end.setCep("81444-444");
		end.setCidade("cidade");
		end.setComplemento("test");
		end.setEstado("PR");
		end.setLogradouro("logradouro");
		end.setNumero(999);
		return end;
	}
}
