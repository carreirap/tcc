package br.com.fichasordens.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrdemServicoLancDto {
	private int sequencia;
	private long idOrdem;
	private long idUsuario;
    private String situacao;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date data;
    private String observacao;
    
	
	public int getSequencia() {
		return sequencia;
	}
	public void setSequencia(int sequencia) {
		this.sequencia = sequencia;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public long getIdOrdem() {
		return idOrdem;
	}
	public void setIdOrdem(long idOrdem) {
		this.idOrdem = idOrdem;
	}
	public long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}
}
