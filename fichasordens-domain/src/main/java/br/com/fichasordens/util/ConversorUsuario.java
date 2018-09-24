package br.com.fichasordens.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fichasordens.Usuario;
import br.com.fichasordens.entities.UsuarioEntity;

public class ConversorUsuario {
	
	private ConversorUsuario () {}
	
	public static UsuarioEntity converterParaEntity(final Usuario usuario) {
		final UsuarioEntity entity = new UsuarioEntity();
		entity.setNome(usuario.getNome());
		entity.setSenha(usuario.getSenha());
		entity.setUsuario(usuario.getNomeUsuario());
		entity.setSituacao(1);
		entity.setPapel(usuario.getPapel());
		entity.setDataCad(new Date());
		return entity;
	}
	
	public static List<Usuario> converter(final List<UsuarioEntity> user) {
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

}
