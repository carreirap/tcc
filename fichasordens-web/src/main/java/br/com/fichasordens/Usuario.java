package br.com.fichasordens;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fichasordens.entities.UsuarioEntity;
import br.com.fichasordens.repository.UsuarioRepository;

@Component
public class Usuario {
	
	@Autowired private UsuarioRepository usuarioRepository;
	
	private String usuario;
	private String nome;
	private String senha;
	private String novaSenha;
	private String confirmaSenha;
	private String papel;
	
	
	public Usuario adicionarUsuario (final Usuario usuario) {
		
		final UsuarioEntity entity = new UsuarioEntity();
		entity.setNome(usuario.nome);
		entity.setSenha(usuario.senha);
		usuarioRepository.save(entity);
		usuario.setUsuario(entity.getUsuario());
		return usuario;
	}
	
	public List<Usuario> listarUsuario(String... usuario) {
		List<UsuarioEntity> usuarios = usuarioRepository.findAll();
		return convert(usuarios);
	}
	
	private List<Usuario> convert(final List<UsuarioEntity> user) {
		List<Usuario> lst = new ArrayList<Usuario>();
		for(UsuarioEntity e: user) {
			Usuario u = new Usuario();
			u.setUsuario(e.getUsuario());
			u.setSenha(e.getSenha());
			u.setNome(e.getNome());
			u.setPapel(e.getPapel());
			lst.add(u);
		}
		return lst;
		/*Field[] fields = cls.getDeclaredFields();
		Object o = clsDest.newInstance();
		for( int i = 0 ; i < fields.length ; i++ )
		{
			fields[i].setAccessible(true);
			System.out.println("Field Name-->"+fields[i].getName()+"\t" 
					+"Field Type-->"+ fields[i].getType().getName()+"\t"
					+"Field Value-->"+ fields[i].get(a));
		}*/
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getNovaSenha() {
		return novaSenha;
	}


	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}


	public String getConfirmaSenha() {
		return confirmaSenha;
	}


	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}

	
}
