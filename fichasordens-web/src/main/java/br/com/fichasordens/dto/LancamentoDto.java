package br.com.fichasordens.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LancamentoDto {
	private int sequencia;
	private long id;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}
}
