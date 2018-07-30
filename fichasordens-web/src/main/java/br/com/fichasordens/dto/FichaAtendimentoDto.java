package br.com.fichasordens.dto;

import java.util.List;

public class FichaAtendimentoDto {
	private long numeroFicha;
    private ClienteDto cliente;
    private String tipoServico;
    
    private LancamentoDto lancamento;
    private List<LancamentoDto> lancamentoLst;
    private List<PecaOutroServicoDto> pecaOutroServicoDto;
    
    public long getNumeroFicha() {
		return numeroFicha;
	}
	public void setNumeroFicha(long numeroFicha) {
		this.numeroFicha = numeroFicha;
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
