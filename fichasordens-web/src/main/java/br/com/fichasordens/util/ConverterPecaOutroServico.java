package br.com.fichasordens.util;

import java.math.BigDecimal;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.dto.PecaOutroServicoDto;

public class ConverterPecaOutroServico {
	
	private ConverterPecaOutroServico() {}
	
	public static PecaOutroServicoDto converterPecaOutroServicoParaDto(final PecaOutroServico p) {
		final PecaOutroServicoDto pecaDto = new PecaOutroServicoDto();
		pecaDto.setDescricao(p.getDescricao());
		pecaDto.setQtde((int)p.getQuantidade());
		pecaDto.setSequencia((int)p.getId());
		pecaDto.setValor(p.getValor());
		pecaDto.setTotal(pecaDto.getValor().multiply(new BigDecimal(pecaDto.getQtde())));
		if (p.getFichaAtendimento() != null)
			pecaDto.setIdOrdem(p.getFichaAtendimento().getId());
		else
			pecaDto.setIdOrdem(p.getOrdemServico().getId());
		return pecaDto;
	}
	
	public static PecaOutroServico converterDtoPecaServico(final PecaOutroServicoDto dto, final TipoServicoEnum type) {
		final PecaOutroServico pecaServico = new PecaOutroServico();
		pecaServico.setDescricao(dto.getDescricao());
		pecaServico.setQuantidade(dto.getQtde());
		pecaServico.setValor(dto.getValor());
		pecaServico.setId(dto.getSequencia());
		if (type == TipoServicoEnum.FICHA_ATENDIMENTO) {
			pecaServico.setFichaAtendimento(new FichaAtendimento());
			pecaServico.getFichaAtendimento().setId(dto.getIdOrdem());
		} else {
			pecaServico.setOrdemServico(new OrdemServico());
			pecaServico.getOrdemServico().setId(dto.getIdOrdem());
		}
		return pecaServico;
	}
}
