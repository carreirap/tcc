package br.com.fichasordens.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public class OrdemServicoDto {

	private long numeroOrdem;
    private ClienteDto cliente;
    private String tipoServico;
    private String descDefeito;
    private String descEquip;
    private String estadoItensAcomp;
    //itemTables: Array<ItemTable>;
    private String fabricante;
    private String modelo;
    private String serie;
    private LancamentoDto lancamento;
    
    private List<LancamentoDto> lancamentoLst;
    private List<PecaOutroServicoDto> pecaOutroServicoDto;
    
	public long getNumeroOrdem() {
		return numeroOrdem;
	}
	public void setNumeroOrdem(long numeroOrdem) {
		this.numeroOrdem = numeroOrdem;
	}
	public ClienteDto getCliente() {
		return cliente;
	}
	public void setCliente(ClienteDto cliente) {
		this.cliente = cliente;
	}
	public String getTipoServico() {
		return tipoServico;
	}
	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}
	public String getDescDefeito() {
		return descDefeito;
	}
	public void setDescDefeito(String descDefeito) {
		this.descDefeito = descDefeito;
	}
	public String getDescEquip() {
		return descEquip;
	}
	public void setDescEquip(String descEquip) {
		this.descEquip = descEquip;
	}
	public String getEstadoItensAcomp() {
		return estadoItensAcomp;
	}
	public void setEstadoItensAcomp(String estadoItensAcomp) {
		this.estadoItensAcomp = estadoItensAcomp;
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
	public LancamentoDto getLancamento() {
		return lancamento;
	}
	public void setLancamento(LancamentoDto lancamento) {
		this.lancamento = lancamento;
	}
	public List<LancamentoDto> getLancamentoLst() {
		return lancamentoLst;
	}
	public void setLancamentoLst(List<LancamentoDto> lancamentoLst) {
		this.lancamentoLst = lancamentoLst;
	}
	public List<PecaOutroServicoDto> getPecaOutroServicoDto() {
		return pecaOutroServicoDto;
	}
	public void setPecaOutroServicoDto(List<PecaOutroServicoDto> pecaOutroServicoDto) {
		this.pecaOutroServicoDto = pecaOutroServicoDto;
	}
}

