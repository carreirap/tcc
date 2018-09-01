package br.com.fichasordens;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
	
	@Test(expected=ExcecaoRetorno.class)
	public void test_clienteCadastrado_fail() throws ExcecaoRetorno {
		when(this.mockClienteRepository.save(org.mockito.Mockito.any(ClienteEntity.class))).thenReturn(new ClienteEntity());
		when(this.endereco.salvarEndereco(loadEndereco())).thenReturn(loadEndereco());
		ClienteEntity clienteCadastrado = new ClienteEntity();
		clienteCadastrado.setId(1L);
		when(this.mockClienteRepository.findByCnpjCpf(org.mockito.Mockito.any(String.class))).thenReturn(clienteCadastrado);
		
		this.cliente.salvarCliente(loadCliente());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_PesquisarClienteCnpj_success() {
		Cliente cli = new Cliente();
		cli.setCnpjCpf("cnpjcpf");
		cli.setNome(null);
		Pageable p = new PageRequest(0, 10);
		@SuppressWarnings("unchecked")
		Page<ClienteEntity> paged = Mockito.mock(Page.class);
		when(this.mockClienteRepository.findAll(org.mockito.Mockito.any(Example.class), org.mockito.Mockito.any(Pageable.class))).thenReturn(paged);
		Page<ClienteEntity> page = this.cliente.pesquisarCliente(cli, p);
		assertNotNull(page);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_PesquisarClienteNome_success() {
		Cliente cli = new Cliente();
		cli.setCnpjCpf(null);
		cli.setNome("nome");
		Pageable p = new PageRequest(0, 10);
		@SuppressWarnings("unchecked")
		Page<ClienteEntity> paged = Mockito.mock(Page.class);
		when(this.mockClienteRepository.findAll(org.mockito.Mockito.any(Example.class), org.mockito.Mockito.any(Pageable.class))).thenReturn(paged);
		Page<ClienteEntity> page = this.cliente.pesquisarCliente(cli, p);
		assertNotNull(page);
	}
	
	public static Cliente loadCliente() {
		Cliente cli = new Cliente();
		cli.setCelular("99999999999999");
		cli.setCnpjCpf("cnpjCpf");
		cli.setEmail("test");
		cli.setFone("988888888");
		cli.setNome("test");
		cli.setEndereco(loadEndereco());
		return cli;
	}
	
	private static Endereco loadEndereco() {
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
