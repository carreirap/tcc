package br.com.fichasordens;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.entities.UsuarioEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.UsuarioRepository;

public class UsuarioTest {
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private Usuario mockUsuario;
	
	@Mock
	private UsuarioRepository mockUsuarioRepository;
	
	@Test
	public void test_adicionarUsuario_success() throws ExcecaoRetorno {
		final Usuario usuario = createUsuario();
		when(this.mockUsuarioRepository.save(org.mockito.Mockito.any(UsuarioEntity.class))).thenReturn(new UsuarioEntity());
		this.mockUsuario.adicionarUsuario(usuario);
		
		Mockito.verify(this.mockUsuarioRepository, Mockito.times(1)).save(org.mockito.Mockito.any(UsuarioEntity.class));
	}
	
	@Test
	public void test_adicionarUsuario2_success() throws ExcecaoRetorno {
		final Usuario usuario = createUsuario();
		usuario.setConfirmaSenha("");
		when(this.mockUsuarioRepository.save(org.mockito.Mockito.any(UsuarioEntity.class))).thenReturn(new UsuarioEntity());
		this.mockUsuario.adicionarUsuario(usuario);
		
		Mockito.verify(this.mockUsuarioRepository, Mockito.times(0)).save(org.mockito.Mockito.any(UsuarioEntity.class));
		
		usuario.setNovaSenha("");
		this.mockUsuario.adicionarUsuario(usuario);
		
		Mockito.verify(this.mockUsuarioRepository, Mockito.times(0)).save(org.mockito.Mockito.any(UsuarioEntity.class));
	}
	
	/*@Test(expected=ExcecaoRetorno.class)
	public void test_adicionarUsuario_failSenha() throws ExcecaoRetorno {
		final Usuario usuario = createUsuario();
		usuario.setConfirmaSenha("124578");
		when(this.mockUsuarioRepository.save(org.mockito.Mockito.any(UsuarioEntity.class))).thenReturn(new UsuarioEntity());
		this.mockUsuario.adicionarUsuario(usuario);
		
	}*/
	@Test(expected=ExcecaoRetorno.class)
	public void test_adicionarUsuario_failSenha() throws ExcecaoRetorno {
		final Usuario usuario = createUsuario();
		try {
			when(this.mockUsuarioRepository.save(org.mockito.Mockito.any(UsuarioEntity.class))).thenThrow(new RuntimeException("DB exception"));
		} catch(Exception e) {
			
		}
		this.mockUsuario.adicionarUsuario(usuario);
		
	}
	
	@Test
	public void test_alterarUsuario_success() throws ExcecaoRetorno {
		final Usuario usuario = createUsuario();
		usuario.setId(10L);
		UsuarioEntity ent = new UsuarioEntity();
		ent.setId(10);
		ent.setNome("Test");
		ent.setUsuario("Test");
		ent.setSenha("123456");
		when(this.mockUsuarioRepository.findOne(usuario.getId())).thenReturn(ent);
		when(this.mockUsuarioRepository.save(org.mockito.Mockito.any(UsuarioEntity.class))).thenReturn(new UsuarioEntity());
		this.mockUsuario.alterarUsuario(usuario);
		
		Mockito.verify(this.mockUsuarioRepository, Mockito.times(1)).findOne(usuario.getId());
		Mockito.verify(this.mockUsuarioRepository, Mockito.times(1)).save(org.mockito.Mockito.any(UsuarioEntity.class));
	}
	
	@Test(expected=ExcecaoRetorno.class)
	public void test_alterarUsuarioException_fail() throws ExcecaoRetorno {
		final Usuario usuario = createUsuario();
		usuario.setId(10L);
		UsuarioEntity ent = new UsuarioEntity();
		ent.setId(10);
		ent.setNome("Test");
		ent.setUsuario("Test");
		ent.setSenha("123456");
		when(this.mockUsuarioRepository.findOne(usuario.getId())).thenReturn(ent);
		try {
			when(this.mockUsuarioRepository.save(org.mockito.Mockito.any(UsuarioEntity.class))).thenThrow(new RuntimeException("DB exception"));
		} catch(Exception e) {
			
		}
		this.mockUsuario.alterarUsuario(usuario);
	}
	
	@Test
	public void test_alterarUsuario2_success() throws ExcecaoRetorno {
		final Usuario usuario = createUsuario();
		usuario.setId(10L);
		usuario.setSenha("123456");
		UsuarioEntity ent = new UsuarioEntity();
		ent.setId(10);
		ent.setNome("Test");
		ent.setUsuario("Test");
		ent.setSenha("123456");
		when(this.mockUsuarioRepository.findOne(usuario.getId())).thenReturn(ent);
		when(this.mockUsuarioRepository.save(org.mockito.Mockito.any(UsuarioEntity.class))).thenReturn(new UsuarioEntity());
		this.mockUsuario.alterarUsuario(usuario);
		
		Mockito.verify(this.mockUsuarioRepository, Mockito.times(1)).findOne(usuario.getId());
		Mockito.verify(this.mockUsuarioRepository, Mockito.times(1)).save(org.mockito.Mockito.any(UsuarioEntity.class));
	}
	
	@Test
	public void test_ListarUsuarios_success() {
		UsuarioEntity ent = new UsuarioEntity();
		ent.setId(10);
		ent.setNome("Test");
		ent.setUsuario("Test");
		ent.setSenha("123456");
		when(this.mockUsuarioRepository.findAll()).thenReturn(Arrays.asList(ent));
		
		List<Usuario> lst = this.mockUsuario.listarUsuario();
		assertEquals(1, lst.size());
	}
	
	@Test
	public void test_ListarUsuario_success() {
		UsuarioEntity ent = new UsuarioEntity();
		ent.setId(10);
		ent.setNome("Test");
		ent.setUsuario("Test");
		ent.setSenha("123456");
		String user = "test";
		when(this.mockUsuarioRepository.findByUsuario(user)).thenReturn(ent);
		
		List<Usuario> lst = this.mockUsuario.listarUsuario(user);
		assertEquals(1, lst.size());
	}
	
	
	private Usuario createUsuario() {
		final Usuario u = new Usuario();
		u.setNome("test");
		u.setNomeUsuario("Test");
		u.setPapel("user");
		u.setConfirmaSenha("654321");
		u.setNovaSenha("654321");
		return u;
	}
	

}
