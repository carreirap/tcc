package br.com.fichasordens.dto;

public class MensagemRetornoDto {
	private String mensagem;
	
	public MensagemRetornoDto(String mensagem) {
		super();
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
