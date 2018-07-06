package br.com.fichasordens.dto;

public class ClienteDto extends PessoaDto {
	private String celular;
	
	public ClienteDto() {
		
	}
	
	public ClienteDto(final long id, final String nome, final String cnpcpf, final String email, final long idEndereco, final String logradouro, final String bairro,
			final String cidade, final String estado, final String fone, final String celular, final int numero, final String cep,
			final String complemento) {
		this.setId(id);
		this.setNome(nome);
		this.setCnpj(cnpcpf);
		this.setEmail(email);
		this.setIdEndereco(idEndereco);
		this.setLogradouro(logradouro);
		this.setBairro(bairro);
		this.setCidade(cidade);
		this.setEstado(estado);
		this.setFone(fone);
		this.setCelular(celular);
		this.setNumero(numero);
		this.setCep(cep);
		this.setComplemento(complemento);
		
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	
}
