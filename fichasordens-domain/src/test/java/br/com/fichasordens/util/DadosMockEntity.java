package br.com.fichasordens.util;

import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.EnderecoEntity;
import br.com.fichasordens.entities.UsuarioEntity;

public class DadosMockEntity {
	
	public static ClienteEntity createClienteEntity() {
		ClienteEntity cliente = new ClienteEntity();
		cliente.setId(12L);
		cliente.setNome("teste");
		cliente.setCelular("1111111");
		cliente.setFone("1111111");
		cliente.setCnpjCpf("55555555");
		cliente.setEndereco(createEnderecoEntity());
		return cliente;
	}
	
	private static EnderecoEntity createEnderecoEntity() {
		EnderecoEntity ent = new EnderecoEntity();
		ent.setBairro("Test");
		ent.setId(2L);
		ent.setLogradouro("Test");
		return ent;
	}

	public static UsuarioEntity createUsuarioEntity() {
		UsuarioEntity user = new UsuarioEntity();
		user.setId(1);
		user.setNome("Test");
		user.setUsuario("Paulo");
		return user;
	}
}
