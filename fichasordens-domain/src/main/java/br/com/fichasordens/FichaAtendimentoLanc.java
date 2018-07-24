package br.com.fichasordens;

import java.util.Date;

public class FichaAtendimentoLanc {
	private int sequencia;
	private Usuario usuario;
	private Date data;
	private String situacao;
	private String observacao;
	private FichaAtendimento FichaAtendimento;
	
	
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
		return FichaAtendimento;
	}
	public void setFichaAtendimento(FichaAtendimento fichaAtendimento) {
		FichaAtendimento = fichaAtendimento;
	}
}
