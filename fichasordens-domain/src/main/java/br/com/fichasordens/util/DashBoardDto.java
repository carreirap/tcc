package br.com.fichasordens.util;

import java.math.BigDecimal;

public class DashBoardDto {

	private int qtd;
	private BigDecimal valor;
	private char alerta;
	
	public DashBoardDto(final int qtd, final BigDecimal valor, final char alerta) {
		super();
		this.qtd = qtd;
		this.valor = valor;
		this.alerta = alerta;
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
	public char getAlerta() {
		return alerta;
	}
	public void setAlerta(char alerta) {
		this.alerta = alerta;
	}
	
}
