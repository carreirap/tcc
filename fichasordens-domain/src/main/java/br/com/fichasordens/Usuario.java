package br.com.fichasordens;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fichasordens.entities.UsuarioEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.UsuarioRepository;

@Component
public class Usuario {
	
	@Autowired private UsuarioRepository usuarioRepository;
	
	private long id;
	private String usuario;
	private String nome;
	private String senha;
	private String novaSenha;
	private String confirmaSenha;
	private String papel;
	private int situacao;
	
	public Usuario adicionarUsuario (final Usuario usuario) throws ExcecaoRetorno {
		
		if (!usuario.confirmaSenha.equals("")) {
			if (this.verificarSenhas(usuario.getNovaSenha(), usuario.getConfirmaSenha())) {
				usuario.setSenha(usuario.getNovaSenha());
				
				final UsuarioEntity entity = converterParaEntity(usuario);
				try {
					usuarioRepository.save(entity);
					usuario.setId(entity.getId());
				} catch (Exception ex) {
					throw new ExcecaoRetorno("Erro ao tentar cadastrar o usuario");
				}
			}
		}
		
		return usuario;
	}

	private UsuarioEntity converterParaEntity(final Usuario usuario) {
		final UsuarioEntity entity = new UsuarioEntity();
		entity.setNome(usuario.nome);
		entity.setSenha(usuario.senha);
		entity.setUsuario(usuario.getUsuario());
		entity.setSituacao(1);
		entity.setPapel(usuario.getPapel());
		entity.setDataCad(new Date());
		return entity;
	}
	
	public Usuario alterarUsuario (final Usuario usuario) throws ExcecaoRetorno {
		
		UsuarioEntity entity = usuarioRepository.findOne(usuario.getId());
		if (usuario.getSenha() != null && entity.getSenha().equals(usuario.getSenha())) {
			if (!usuario.confirmaSenha.equals("")) {
				if (this.verificarSenhas(usuario.getNovaSenha(), usuario.getConfirmaSenha())) {
					entity.setSenha(usuario.getNovaSenha());
				}
			}
		}
		
		entity.setNome(usuario.nome);
		entity.setUsuario(usuario.getUsuario());
		entity.setSituacao(usuario.getSituacao());
		entity.setPapel(usuario.getPapel());
		
		
		try {
			usuarioRepository.save(entity);
		} catch (Exception ex) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar o usuario");
		}
		
		
		return usuario;
	}
	
	public List<Usuario> listarUsuario(String... usuario) {
		List<UsuarioEntity> usuarios = usuarioRepository.findAll();
		return convert(usuarios);
	}
	
	private boolean verificarSenhas(final String senha, final String confirmaSenha) {
		return (senha.equals(confirmaSenha));
	}
	
	private List<Usuario> convert(final List<UsuarioEntity> user) {
		List<Usuario> lst = new ArrayList<Usuario>();
		for(UsuarioEntity e: user) {
			Usuario u = new Usuario();
			u.setUsuario(e.getUsuario());
			u.setSenha(e.getSenha());
			u.setNome(e.getNome());
			u.setPapel(e.getPapel());
			u.setId(e.getId());
			u.setSituacao(e.getSituacao());
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getSituacao() {
		return situacao;
	}

	public void setSituacao(int situacao) {
		this.situacao = situacao;
	}
}
