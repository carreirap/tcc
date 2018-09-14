package br.com.fichasordens.util;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.Endereco;
import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.EnderecoEntity;

public class ConversorCliente {
	
	private ConversorCliente() {}
	
	public static Cliente converterClienteEntityParaCliente(ClienteEntity entity) {
		Cliente cliente = new Cliente();
		cliente.setId(entity.getId());
		cliente.setNome(entity.getNome());
		cliente.setCelular(entity.getCelular());
		cliente.setFone(entity.getFone());
		cliente.setCnpjCpf(entity.getCnpjCpf());
		if (entity.getEndereco() != null)
			cliente.setEndereco(converterEntityParaEndereco(entity.getEndereco()));
		return cliente;
	}
	
	private static Endereco converterEntityParaEndereco(EnderecoEntity ent) {
		Endereco end = new Endereco();
		end.setBairro(ent.getBairro());
		end.setCep(ent.getCep());
		end.setCidade(ent.getCidade());
		end.setComplemento(ent.getComplemento());
		end.setEstado(ent.getEstado());
		end.setId(ent.getId());
		end.setLogradouro(ent.getLogradouro());
		end.setNumero(ent.getNumero());
		
		return end;
	}

}
