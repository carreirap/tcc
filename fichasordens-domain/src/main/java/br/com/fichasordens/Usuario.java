package br.com.fichasordens;

import java.util.ArrayList;
import java.util.Arrays;
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
	private String nomeUsuario;
	private String nome;
	private String senha;
	private String novaSenha;
	private String confirmaSenha;
	private String papel;
	private int situacao;
	
	public Usuario adicionarUsuario(final Usuario usuario) throws ExcecaoRetorno {

		if (!usuario.confirmaSenha.equals("")
				&& this.verificarSenhas(usuario.getNovaSenha(), usuario.getConfirmaSenha())) {
			usuario.setSenha(usuario.getNovaSenha());

			final UsuarioEntity entity = converterParaEntity(usuario);
			try {
				usuarioRepository.save(entity);
				usuario.setId(entity.getId());
			} catch (Exception ex) {
				throw new ExcecaoRetorno("Erro ao tentar cadastrar o usuario");
			}
		}

		return usuario;
	}

	private UsuarioEntity converterParaEntity(final Usuario usuario) {
		final UsuarioEntity entity = new UsuarioEntity();
		entity.setNome(usuario.nome);
		entity.setSenha(usuario.senha);
		entity.setUsuario(usuario.getNomeUsuario());
		entity.setSituacao(1);
		entity.setPapel(usuario.getPapel());
		entity.setDataCad(new Date());
		return entity;
	}
	
	public Usuario alterarUsuario (final Usuario usuario) throws ExcecaoRetorno {
		
		UsuarioEntity entity = usuarioRepository.findOne(usuario.getId());
		if (usuario.getSenha() != null && entity.getSenha().equals(usuario.getSenha())) {
			setNovaSenhaSeNovaSenhaEConfirmeSenhaBatem(usuario, entity);
		}
		
		entity.setNome(usuario.nome);
		entity.setUsuario(usuario.getNomeUsuario());
		entity.setSituacao(usuario.getSituacao());
		entity.setPapel(usuario.getPapel());
		
		
		try {
			usuarioRepository.save(entity);
		} catch (Exception ex) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar o usuario");
		}
		
		
		return usuario;
	}

	private void setNovaSenhaSeNovaSenhaEConfirmeSenhaBatem(final Usuario usuario, UsuarioEntity entity) {
		if (!usuario.confirmaSenha.equals("") && this.verificarSenhas(usuario.getNovaSenha(), usuario.getConfirmaSenha())) {
				entity.setSenha(usuario.getNovaSenha());
		}
	}
	
	public List<Usuario> listarUsuario(String... usuario) {
		List<UsuarioEntity> usuarios = null;
		if (usuario.length == 0 || usuario.length == 1 && usuario[0] == null) {
			usuarios = usuarioRepository.findAll();
		} else {
			usuarios = Arrays.asList(usuarioRepository.findByUsuario(usuario[0]));
		}
		return convert(usuarios);
	}
	
	private boolean verificarSenhas(final String senha, final String confirmaSenha) {
		return (senha.equals(confirmaSenha));
	}
	
	private List<Usuario> convert(final List<UsuarioEntity> user) {
		List<Usuario> lst = new ArrayList<>();
		for(UsuarioEntity e: user) {
			Usuario u = new Usuario();
			u.setNomeUsuario(e.getUsuario());
			u.setSenha(e.getSenha());
			u.setNome(e.getNome());
			u.setPapel(e.getPapel());
			u.setId(e.getId());
			u.setSituacao(e.getSituacao());
			lst.add(u);
		}
		return lst;
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
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
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
