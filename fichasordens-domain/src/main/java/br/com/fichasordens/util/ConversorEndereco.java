package br.com.fichasordens.util;

import br.com.fichasordens.Endereco;
import br.com.fichasordens.entities.EnderecoEntity;

public class ConversorEndereco {
	
	private ConversorEndereco() {}
	
	public static EnderecoEntity converteParaEntity(final Endereco endereco) {
		final EnderecoEntity entity = new EnderecoEntity();
		entity.setBairro(endereco.getBairro());
		entity.setCep(endereco.getCep());
		entity.setCidade(endereco.getCidade());
		entity.setEstado(endereco.getEstado());
		entity.setLogradouro(endereco.getLogradouro());
		entity.setNumero(endereco.getNumero());
		entity.setComplemento(endereco.getComplemento());
		if ( endereco.getId() != 0 ) {
			entity.setId(endereco.getId());
		}
		return entity;
	}

}
