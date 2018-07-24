package br.com.fichasordens;

import java.math.BigDecimal;

public class PecaOutroServico {
	private long id;
	private String descricao;
	private long quantidade;
	private BigDecimal valor;
	private OrdemServico ordemServico;
	private FichaAtendimento fichaAtendimento;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public OrdemServico getOrdemServico() {
		return ordemServico;
	}
	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}
	public FichaAtendimento getFichaAtendimento() {
		return fichaAtendimento;
	}
	public void setFichaAtendimento(FichaAtendimento fichaAtendimento) {
		this.fichaAtendimento = fichaAtendimento;
	}
}
