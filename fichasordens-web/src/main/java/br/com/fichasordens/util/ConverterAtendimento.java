package br.com.fichasordens.util;

import br.com.fichasordens.Atendimento;
import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.dto.AtendimentoDto;

public class ConverterAtendimento {
	
	private ConverterAtendimento() {
	}
	
	public static Atendimento converterAtendimentoDto(final AtendimentoDto dto) {
		final Atendimento atend = new Atendimento();
		atend.setData(dto.getDataAtendimento());
		atend.setDescricao(dto.getDescricao());
		atend.setDuracao(dto.getDuracao());
		atend.setFichaAtendimento(new FichaAtendimento());
		atend.getFichaAtendimento().setId(dto.getId());
		atend.setSequencia(dto.getSequencia());
		atend.setValor(dto.getValor());
		return atend;
	}

}
