package br.com.fichasordens.dto;

public class ClienteDto extends PessoaDto {
	private String celular;
	
	public ClienteDto() {
		
	}
	
	public ClienteDto(final long id, final String nome, final String cnpcpf, final String email, final String logradouro, final String bairro,
			final String cidade, final String estado, final String fone, final String celular ) {
		this.setId(id);
		this.setNome(nome);
		this.setCnpj(cnpcpf);
		this.setEmail(email);
		this.setLogradouro(logradouro);
		this.setBairro(bairro);
		this.setCidade(cidade);
		this.setEstado(estado);
		this.setFone(fone);
		this.setCelular(celular);
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	
}
