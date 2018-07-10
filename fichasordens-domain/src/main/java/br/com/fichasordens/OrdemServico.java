package br.com.fichasordens;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class OrdemServico {
	private long id;
	private String tipoServico;
	private String fabricante;
	private String modelo;
	private String serie;
	private String descEquip;
	private String descDefeito;
	private String estadoItensAcomp;
	private String descServico;
	private Cliente cliente;
	
	private List<OrdemServicoLanc> statusLancamentos;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	public String getDescEquip() {
		return descEquip;
	}

	public void setDescEquip(String descEquip) {
		this.descEquip = descEquip;
	}

	public String getDescDefeito() {
		return descDefeito;
	}

	public void setDescDefeito(String descDefeito) {
		this.descDefeito = descDefeito;
	}

	public String getEstadoItensAcomp() {
		return estadoItensAcomp;
	}

	public void setEstadoItensAcomp(String estadoItensAcomp) {
		this.estadoItensAcomp = estadoItensAcomp;
	}

	public String getDescServico() {
		return descServico;
	}

	public void setDescServico(String descServico) {
		this.descServico = descServico;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<OrdemServicoLanc> getStatusLancamentos() {
		return statusLancamentos;
	}

	public void setStatusLancamentos(List<OrdemServicoLanc> statusLancamentos) {
		this.statusLancamentos = statusLancamentos;
	}

}
