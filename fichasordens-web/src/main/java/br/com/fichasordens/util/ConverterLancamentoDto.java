package br.com.fichasordens.util;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.FichaAtendimentoLanc;
import br.com.fichasordens.Usuario;
import br.com.fichasordens.dto.LancamentoDto;

public class ConverterLancamentoDto {
	
	public static LancamentoDto converterLancamentoDto(FichaAtendimentoLanc lanc) {
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
	
	public static FichaAtendimentoLanc converterDtoLanc(final LancamentoDto dto) {
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

}
