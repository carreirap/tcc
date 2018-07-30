package br.com.fichasordens.util;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.dto.PecaOutroServicoDto;

public class ConverterPecaOutroServico {
	
	public static PecaOutroServicoDto converterPecaOutroServicoParaDto(final PecaOutroServico p) {
		final PecaOutroServicoDto pecaDto = new PecaOutroServicoDto();
		pecaDto.setDescricao(p.getDescricao());
		pecaDto.setQtde((int)p.getQuantidade());
		pecaDto.setSequencia((int)p.getId());
		pecaDto.setValor(p.getValor());
		return pecaDto;
	}
	
	public static PecaOutroServico converterDtoPecaServico(final PecaOutroServicoDto dto) {
		final PecaOutroServico pecaServico = new PecaOutroServico();
		//		pecaServico.setId(dto.getIdOrdem()...);
		pecaServico.setDescricao(dto.getDescricao());
		pecaServico.setQuantidade(dto.getQtde());
		pecaServico.setValor(dto.getValor());
		pecaServico.setId(dto.getSequencia());
		pecaServico.setFichaAtendimento(new FichaAtendimento());
		pecaServico.getFichaAtendimento().setId(dto.getIdOrdem());
		return pecaServico;
	}
}
