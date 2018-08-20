package br.com.fichasordens.util;

public enum TipoServicoEnum {
	
	FICHA_ATENDIMENTO("Ficha de Atendimento"),
	ORDEM_SERVICO("Ordem de Serviço");
	
	private String value;

	private TipoServicoEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	

}
