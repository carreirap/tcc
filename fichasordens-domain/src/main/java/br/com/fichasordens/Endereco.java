package br.com.fichasordens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fichasordens.entities.EnderecoEntity;
import br.com.fichasordens.repository.EnderecoRepository;
import br.com.fichasordens.util.ConversorEndereco;

@Component
public class Endereco {
	
	private long id;
	private String logradouro;
	private String bairro;
	private String cidade;
	private String estado;
	private int numero;
	private String cep;
	private String complemento;
	
	@Autowired 
	private EnderecoRepository repositoryEndereco;
	
	public Endereco salvarEndereco(final Endereco endereco) {
		EnderecoEntity entity = ConversorEndereco.converteParaEntity(endereco);
		entity = repositoryEndereco.save(entity);
		endereco.setId(entity.getId().longValue());
		return endereco;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	
	
}
