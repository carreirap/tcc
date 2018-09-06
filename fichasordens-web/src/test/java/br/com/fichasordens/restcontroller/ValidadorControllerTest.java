package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ValidadorControllerTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private ValidadorController validador;
	
	
	@Test
	public void test_validarCPF_success() {
		ResponseEntity<?> response = this.validador.validarCpf("661.205.610-05");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_validarCPF_fail() {
		ResponseEntity<?> response = this.validador.validarCpf("661.205.610-06");
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_validarCnpj_success() {
		ResponseEntity<?> response = this.validador.validarCnpj("54.204.541/0001-60");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_validarCnpj_fail() {
		ResponseEntity<?> response = this.validador.validarCnpj("54.204.541/0001-61");
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	

}
