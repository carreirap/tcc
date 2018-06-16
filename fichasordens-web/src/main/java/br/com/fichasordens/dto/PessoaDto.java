package br.com.fichasordens.dto;

public class PessoaDto {
	private long id;
	private String nome;
	private String cnpj;
	private String fone;
	private String email;
	private String site;
	private long idEndereco;
	private String logradouro;
	private int numero;
	private String cidade;
	private String bairro;
	private String estado;
	private String complemento;
	private String cep;
	
	public long getId() {
		return id;
	}
	public void setId(final long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(final String nome) {
		this.nome = nome;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(final String cnpj) {
		this.cnpj = cnpj;
	}
	public String getFone() {
		return fone;
	}
	public void setFone(final String fone) {
		this.fone = fone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}
	public String getSite() {
		return site;
	}
	public void setSite(final String site) {
		this.site = site;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(final String logradouro) {
		this.logradouro = logradouro;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(final int numero) {
		this.numero = numero;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(final String cidade) {
		this.cidade = cidade;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(final String bairro) {
		this.bairro = bairro;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(final String estado) {
		this.estado = estado;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(final String complemento) {
		this.complemento = complemento;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(final String cep) {
		this.cep = cep;
	}
	public long getIdEndereco() {
		return idEndereco;
	}
	public void setIdEndereco(final long idEndereco) {
		this.idEndereco = idEndereco;
	}
}
