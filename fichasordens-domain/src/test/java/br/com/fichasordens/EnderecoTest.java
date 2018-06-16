package br.com.fichasordens;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.entities.EnderecoEntity;
import br.com.fichasordens.repository.EnderecoRepository;
import br.com.fichasordens.util.DadosTestes;

public class EnderecoTest {
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private Endereco mockEndereco;
	
	@Mock
	private EnderecoRepository mockRepositoryEndereco;
	
	@Test
	public void test_salvarEndereco_sucess() {
		when(this.mockRepositoryEndereco.save(org.mockito.Mockito.any(EnderecoEntity.class))).thenReturn(DadosTestes.loadEnderecoEntity());
		
		final Endereco end = this.mockEndereco.salvarEndereco(DadosTestes.loadEndereco());
		
		assertNotNull(end);
	}
	
	@Test
	public void test_salvarEnderecoUpdate_sucess() {
		when(this.mockRepositoryEndereco.save(org.mockito.Mockito.any(EnderecoEntity.class))).thenReturn(DadosTestes.loadEnderecoEntity());
		Endereco enderecoAuxiliar = DadosTestes.loadEndereco();
		enderecoAuxiliar.setId(20L);;
		final Endereco end = this.mockEndereco.salvarEndereco(enderecoAuxiliar);
		
		assertNotNull(end);
	}

}
