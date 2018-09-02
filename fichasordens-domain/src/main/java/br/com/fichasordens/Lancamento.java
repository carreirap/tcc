package br.com.fichasordens;

import java.util.Date;

public class Lancamento {
	private int sequencia;
	private Usuario usuario;
	private Date data;
	private String situacao;
	private String observacao;
	private FichaAtendimento fichaAtendimento;
	private OrdemServico ordemServico;
	
	
	public int getSequencia() {
		return sequencia;
	}
	public void setSequencia(int sequencia) {
		this.sequencia = sequencia;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public FichaAtendimento getFichaAtendimento() {
		return fichaAtendimento;
	}
	public void setFichaAtendimento(FichaAtendimento fichaAtendimento) {
		this.fichaAtendimento = fichaAtendimento;
	}
	public OrdemServico getOrdemServico() {
		return ordemServico;
	}
	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}
}
