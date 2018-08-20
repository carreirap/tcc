package br.com.fichasordens.util;

public enum StatusServicoEnum {
	
	ABERTO("Aberto"),
	TRABALHANDO("Trabalhando"),
	AGUARDANDO("Aguardando"),
	FECHADO("Fechado"),
	CANCELADO("Cancelado"),
	FATURADO("Faturado"),
	FINALIZADO("Finalizado");
	
	private String value;
	

	private StatusServicoEnum(String value) {
		this.value = value;
		
	}

	public String getValue() {
		return value;
	}
	
}
