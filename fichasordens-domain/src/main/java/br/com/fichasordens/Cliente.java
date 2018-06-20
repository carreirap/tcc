package br.com.fichasordens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.EnderecoEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.ClienteRepository;

@Component
public class Cliente extends Pessoa {
	
	private long id;
	private String celular;
	private String cnpjCpf;
	
	@Autowired 
	private ClienteRepository clienteRepository;
	
	@Transactional
	public void salvarCliente(final Cliente cliente) throws ExcecaoRetorno {
		
		if (this.isClienteCadastrado(cliente.getCnpjCpf())) {
			throw new ExcecaoRetorno("Cliente ja cadastrado com CNPJ/CPF informado");
		}
		
		ClienteEntity entity = this.convertParaEntity(cliente);
		this.getEndereco().salvarEndereco(cliente.getEndereco());
		entity.getEndereco().setId(cliente.getEndereco().getId());
		this.clienteRepository.save(entity);
	}
	
	public boolean isClienteCadastrado(final String cnpjCpf) {
		final ClienteEntity entity = this.clienteRepository.findByCnpjCpf(cnpjCpf);
		if (entity != null) {
			return true;
		} else {
			return false;
		}
	}
	
	private ClienteEntity convertParaEntity(final Cliente cliente) {
		final ClienteEntity entity = new ClienteEntity();
		entity.setCelular(cliente.getCelular());
		entity.setCnpjCpf(cliente.getCnpjCpf());
		entity.setNome(cliente.getNome());
		entity.setEndereco(new EnderecoEntity());
		return entity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCnpjCpf() {
		return cnpjCpf;
	}

	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}
	
}
