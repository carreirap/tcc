package br.com.fichasordens.util;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.entities.ClienteEntity;

public class ConversorCliente {
	
	private ConversorCliente() {}
	
	public static Cliente converterClienteEntityParaCliente(ClienteEntity entity) {
		Cliente cliente = new Cliente();
		cliente.setId(entity.getId());
		cliente.setNome(entity.getNome());
		cliente.setCelular(entity.getCelular());
		cliente.setFone(entity.getFone());
		cliente.setCnpjCpf(entity.getCnpjCpf());
		return cliente;
	}

}
