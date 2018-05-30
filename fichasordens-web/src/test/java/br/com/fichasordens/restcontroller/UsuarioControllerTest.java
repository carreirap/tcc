package br.com.fichasordens.restcontroller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

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

import br.com.fichasordens.Usuario;
import br.com.fichasordens.dto.UsuarioDto;
import br.com.fichasordens.exception.ExcecaoRetorno;

public class UsuarioControllerTest {
	
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private UsuarioController usuarioController;
	
	@Mock
	private Usuario mockUsuario;
	
	@Test
	public void test_getUsuarios_success() {
		
		when(this.mockUsuario.listarUsuario()).thenReturn(Arrays.asList(carregarUsuario()));
		
		ResponseEntity response = this.usuarioController.getUsuario("");
		
		List<Usuario> list = (List) response.getBody();
		
		assertNotNull(list);
		
		
	}
	
	@Test
	public void test_getUsuarioDesativado_success() {
		Usuario usuario= carregarUsuario();
		usuario.setSituacao(0);
		when(this.mockUsuario.listarUsuario()).thenReturn(Arrays.asList(usuario));
		
		ResponseEntity response = this.usuarioController.getUsuario("");
		
		List<Usuario> list = (List) response.getBody();
		
		assertNotNull(list);
	}
	
	@Test
	public void test_alterarUsuario_success() throws ExcecaoRetorno {
		Usuario usuario= carregarUsuario();
		usuario.setNovaSenha("123456");
		usuario.setConfirmaSenha("123456");
		
		when(this.mockUsuario.alterarUsuario(org.mockito.Mockito.any(Usuario.class))).thenReturn(usuario);
		
		ResponseEntity response = this.usuarioController.alterarSenha(carregarUsuarioDto());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertNotNull(response.getBody());
		
	}
	
	@Test
	public void test_alterarUsuario_fail() throws ExcecaoRetorno {
		Usuario usuario= carregarUsuario();
		usuario.setNovaSenha("123456");
		usuario.setConfirmaSenha("123456");
		
		when(this.mockUsuario.alterarUsuario(org.mockito.Mockito.any(Usuario.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.usuarioController.alterarSenha(carregarUsuarioDto());
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
		assertNotNull(response.getBody());
		
	}
	
	@Test
	public void test_adicionarUsuario_success() throws ExcecaoRetorno {
		Usuario usuario= carregarUsuario();
		usuario.setNovaSenha("123456");
		usuario.setConfirmaSenha("123456");
		
		when(this.mockUsuario.adicionarUsuario(org.mockito.Mockito.any(Usuario.class))).thenReturn(usuario);
		
		ResponseEntity response = this.usuarioController.adicionarUsuario(carregarUsuarioDto());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertNotNull(response.getBody());
		
	}
	
	@Test
	public void test_adicionarUsuario_fail() throws ExcecaoRetorno {
		
		UsuarioDto dto = carregarUsuarioDto();
		dto.setSituacao("true");
		when(this.mockUsuario.adicionarUsuario(org.mockito.Mockito.any(Usuario.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.usuarioController.adicionarUsuario(dto);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
		assertNotNull(response.getBody());
		
	}
	
	private Usuario carregarUsuario() {
		Usuario user = new Usuario();
		user.setNome("test");
		user.setPapel("Admin");
		user.setSituacao(1);
		user.setUsuario("usuario");
		return user;
	}
	
	private UsuarioDto carregarUsuarioDto() {
		UsuarioDto dto = new UsuarioDto();
		dto.setId(1);
		dto.setNome("Test");
		dto.setUsuario("usuario");
		dto.setNovaSenha("123456");
		dto.setConfirmaSenha("1234567");
		dto.setSituacao("1");
		return dto;
	}
	
}
