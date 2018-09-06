package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.service.DashboardService;
import br.com.fichasordens.util.DashBoardDto;

public class DashboardControllerTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private DashboardController controller;
	
	@Mock
	private DashboardService dashBoardService;
	
	@Test
	public void test_listarFichas_success() {
		Map<String, DashBoardDto> map = new HashMap<String, DashBoardDto>();
		DashBoardDto dto = new DashBoardDto(21,new BigDecimal(23),'S');
		map.put("Trabalhando", dto);
		
		when(this.dashBoardService.contarFichasAtendimento()).thenReturn(map);
		
		ResponseEntity response = this.controller.listarFichas();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		
	}
	
	@Test
	public void test_listarOrdens_success() {
		Map<String, DashBoardDto> map = new HashMap<String, DashBoardDto>();
		DashBoardDto dto = new DashBoardDto(21,new BigDecimal(23),'S');
		map.put("Trabalhando", dto);
		
		when(this.dashBoardService.contarOrdensDeServicos()).thenReturn(map);
		
		ResponseEntity response = this.controller.listarOrdens();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		
	}
}
