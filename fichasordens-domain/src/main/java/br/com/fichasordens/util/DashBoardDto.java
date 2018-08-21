package br.com.fichasordens.util;

import java.math.BigDecimal;

public class DashBoardDto {

	private int qtd;
	private BigDecimal valor;
	
	public DashBoardDto(int qtd, BigDecimal valor) {
		super();
		this.qtd = qtd;
		this.valor = valor;
	}
	public int getQtd() {
		return qtd;
	}
	public void setQtd(int qtd) {
		this.qtd = qtd;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
