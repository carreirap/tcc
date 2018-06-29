package br.com.fichasordens;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
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
	
	@Transactional
	public Page<ClienteEntity> pesquisarCliente(final Cliente cliente) {
		ClienteEntity ent = new ClienteEntity();
		if (cliente.getCnpjCpf() != null) {
			ent.setCnpjCpf(cliente.getCnpjCpf().trim());
		} else {
			ent.setNome(cliente.getNome());
		}
		
		
		ExampleMatcher matcher = ExampleMatcher.matching()
	            .withIgnoreNullValues()
	            .withIgnoreCase()
	            .withMatcher("nome", match -> match.startsWith());
		Example<ClienteEntity> example = Example.of(ent, matcher);
		Page<ClienteEntity> paged = this.clienteRepository.findAll(example, ClienteRepository.createPageRequest());
		paged.forEach(a -> a.getEndereco().getLogradouro());
		return paged;
	}

	private void converterEntityParaCliente(final List<Cliente> clienteList, final ClienteEntity e) {
		Cliente cli = new Cliente();
		cli.setNome(e.getNome());
		cli.setCnpjCpf(e.getCnpjCpf());
		cli.setFone(e.getFone());
		cli.setId(e.getId());
		clienteList.add(cli);
	}
	
	public boolean isClienteCadastrado(final String cnpjCpf) {
		boolean clienteEstaCadastrado = false;
		final ClienteEntity entity = this.clienteRepository.findByCnpjCpf(cnpjCpf);
		if (entity != null) {
			clienteEstaCadastrado =  true;
		} 
		return clienteEstaCadastrado;
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
