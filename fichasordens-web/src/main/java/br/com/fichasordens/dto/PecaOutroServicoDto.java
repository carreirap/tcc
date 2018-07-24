package br.com.fichasordens.dto;

import java.math.BigDecimal;

public class PecaOutroServicoDto {
	private long idOrdem;
	private int sequencia;
	private int qtde;
	private String descricao;
	private BigDecimal valor;
	
	public int getQtde() {
		return qtde;
	}
	public void setQtde(int qtde) {
		this.qtde = qtde;
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
	public long getIdOrdem() {
		return idOrdem;
	}
	public void setIdOrdem(long idOrdem) {
		this.idOrdem = idOrdem;
	}
	public int getSequencia() {
		return sequencia;
	}
	public void setSequencia(int sequencia) {
		this.sequencia = sequencia;
	}
}
