package br.com.fichasordens.dto;

import java.math.BigDecimal;

public class ParametroDto {
	private byte id;
	private String descricao;
	private BigDecimal valor;
	
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
