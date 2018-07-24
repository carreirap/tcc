package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.Parametro;
import br.com.fichasordens.dto.ParametroDto;

public class ParametroControllerTest {
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private ParametroController parametroController;
	
	@Mock
	private Parametro mockParametro;
	
	@Test
	public void test_getParametros_sucess() {
		List<Parametro> list = new ArrayList<Parametro>();
		list.add(carregarParametro());
		
		when(this.mockParametro.recuperarParametros()).thenReturn(list);
		
		ResponseEntity<List<ParametroDto>> paramList = this.parametroController.recuperarParametros("user_test");
		
		assertNotNull(paramList.getBody());
	}
	
	@Test
	public void test_salvarParametro_sucess() {
		List<Parametro> list = new ArrayList<>();
		list.add(carregarParametro());
		
		ParametroDto dto = new ParametroDto();
		dto.setId((byte)1);
		dto.setValor(new BigDecimal(10));
		List<ParametroDto> dtoList = Arrays.asList(dto);
		
		doNothing().when(this.mockParametro).salvarParametro(list);
		
		ResponseEntity response = this.parametroController.salvarParametro(dtoList);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_salvarParametroValorNull_fail() {
		List<Parametro> list = new ArrayList<>();
		list.add(carregarParametro());
		
		ParametroDto dto = new ParametroDto();
		dto.setId((byte)1);
		dto.setValor(null);
		List<ParametroDto> dtoList = Arrays.asList(dto);
		
		doNothing().when(this.mockParametro).salvarParametro(list);
		
		ResponseEntity response = this.parametroController.salvarParametro(dtoList);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_salvarParametroValorZero_fail() {
		List<Parametro> list = new ArrayList<Parametro>();
		list.add(carregarParametro());
		
		ParametroDto dto = new ParametroDto();
		dto.setId((byte)1);
		dto.setValor(new BigDecimal(0.0));
		List<ParametroDto> dtoList = Arrays.asList(dto);
		
		doNothing().when(this.mockParametro).salvarParametro(list);
		
		ResponseEntity response = this.parametroController.salvarParametro(dtoList);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_salvarParametroIdZero_fail() {
		List<Parametro> list = new ArrayList<Parametro>();
		list.add(carregarParametro());
		
		ParametroDto dto = new ParametroDto();
		dto.setId((byte)0);
		dto.setValor(new BigDecimal(0.0));
		List<ParametroDto> dtoList = Arrays.asList(dto);
		
		doNothing().when(this.mockParametro).salvarParametro(list);
		
		ResponseEntity response = this.parametroController.salvarParametro(dtoList);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}



	private Parametro carregarParametro() {
		Parametro par = new Parametro();
		par.setId((byte)1);
		par.setDescricao("Test");
		par.setValor(new BigDecimal(10));
		
		return par;
	}

}
