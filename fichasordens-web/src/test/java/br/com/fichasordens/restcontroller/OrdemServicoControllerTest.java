package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.dto.ClienteDto;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.OrdemServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;

public class OrdemServicoControllerTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private OrdemServicoController ordemServicoController;
	
	@Mock 
	OrdemServico ordemServico;
	
	@Test
	public void test_salvarOrdemServico_success() throws ExcecaoRetorno {
		OrdemServicoDto ordem = criarDto();
		OrdemServico ord = new OrdemServico();
		ord.setId(10L);
		when(this.ordemServico.salvarOrdem(org.mockito.Mockito.any(OrdemServico.class))).thenReturn(ord);
		
		ResponseEntity response = this.ordemServicoController.salvarOrdemServico(ordem);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	private OrdemServicoDto criarDto() {
		OrdemServicoDto ordem = new OrdemServicoDto();
		ordem.setTipoServico("true");
		ordem.setCliente(new ClienteDto());
		ordem.setLancamento(new LancamentoDto());
		return ordem;
	}
	
	@Test
	public void test_salvarOrdemServico_fail() throws ExcecaoRetorno {
		OrdemServicoDto ordem = criarDto();
		when(this.ordemServico.salvarOrdem(org.mockito.Mockito.any(OrdemServico.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.ordemServicoController.salvarOrdemServico(ordem);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

}
