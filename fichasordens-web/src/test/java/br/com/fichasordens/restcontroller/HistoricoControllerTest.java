package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.dto.HistoricoPesquisaDto;
import br.com.fichasordens.service.HistoricoService;
import br.com.fichasordens.util.ResultadoPesquisaDto;

public class HistoricoControllerTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private HistoricoController controller;
	
	@Mock
	private HistoricoService historicoService;
	
	@Test
	public void test_pesquisarHistorico_success() {
		Pageable page = new PageRequest(0, 10);
		Page<ResultadoPesquisaDto> paged = Mockito.mock(Page.class);
		when(this.historicoService.pesquisar("Ficha", 199, "11111111111", "Aberto", null, null, page)).thenReturn(paged);
		HistoricoPesquisaDto dto = new HistoricoPesquisaDto();
		dto.setCnpjcpf("11111111111");
		dto.setNumero(199);
		dto.setSituacao("Aberto");
		dto.setTipo("Ficha");
		ResponseEntity response = this.controller.pesquisar(dto, page);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());

		
	}

}
