package br.com.fichasordens.util;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.FichaAtendimentoLanc;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.OrdemServicoLanc;
import br.com.fichasordens.Usuario;
import br.com.fichasordens.dto.LancamentoDto;

public class ConverterLancamentoDto {
	
	private ConverterLancamentoDto() {}
	
	public static LancamentoDto converterLancamentoParaDto(FichaAtendimentoLanc lanc) {
		LancamentoDto lancDto = new LancamentoDto();
		lancDto.setData(lanc.getData());
		lancDto.setId(lanc.getFichaAtendimento().getId());
		lancDto.setIdUsuario(lanc.getUsuario().getId());
		lancDto.setNomeUsuario(lanc.getUsuario().getNome());
		lancDto.setObservacao(lanc.getObservacao());
		lancDto.setSequencia(lanc.getSequencia());
		lancDto.setSituacao(lanc.getSituacao());
		return lancDto;
	}
	
	public static LancamentoDto converterOrdemLancamentoParaDto(OrdemServicoLanc lanc) {
		LancamentoDto lancDto = new LancamentoDto();
		lancDto.setData(lanc.getData());
		lancDto.setId(lanc.getOrdemServico().getId());
		lancDto.setIdUsuario(lanc.getUsuario().getId());
		lancDto.setNomeUsuario(lanc.getUsuario().getNome());
		lancDto.setObservacao(lanc.getObservacao());
		lancDto.setSequencia(lanc.getSequencia());
		lancDto.setSituacao(lanc.getSituacao());
		return lancDto;
	}
	
	public static FichaAtendimentoLanc converterDtoParaFichaAtendimentoLanc(final LancamentoDto dto) {
		final FichaAtendimentoLanc lanc = new FichaAtendimentoLanc();
		lanc.setData(dto.getData());
		lanc.setObservacao(dto.getObservacao());
		lanc.setSituacao(dto.getSituacao());
		lanc.setUsuario(new Usuario());
		lanc.getUsuario().setId(dto.getIdUsuario());
		lanc.setSequencia(dto.getSequencia());
		lanc.setFichaAtendimento(new FichaAtendimento());
		lanc.getFichaAtendimento().setId(dto.getId());
		return lanc;
	}
	
	public static OrdemServicoLanc converterDtoParaOrdemServicoLanc(final LancamentoDto dto) {
		final OrdemServicoLanc lanc = new OrdemServicoLanc();
		lanc.setData(dto.getData());
		lanc.setObservacao(dto.getObservacao());
		lanc.setSituacao(dto.getSituacao());
		lanc.setUsuario(new Usuario());
		lanc.getUsuario().setId(dto.getIdUsuario());
		lanc.setSequencia(dto.getSequencia());
		lanc.setOrdemServico(new OrdemServico());
		lanc.getOrdemServico().setId(dto.getId());
		return lanc;
	}

}
