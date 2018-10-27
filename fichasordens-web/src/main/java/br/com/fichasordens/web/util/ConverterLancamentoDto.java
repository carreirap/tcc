package br.com.fichasordens.web.util;

import java.util.List;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.Lancamento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.Usuario;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.ListagemDashboardDto;
import br.com.fichasordens.util.DataUtil;

public class ConverterLancamentoDto {
	
	private ConverterLancamentoDto() {}
	
	public static LancamentoDto converterLancamentoParaDto(Lancamento lanc) {
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
	
	public static void converterLancamentoParaDto(final String situacao, final int qtdDiasAlerta, List<Lancamento> lancLst,
			ListagemDashboardDto dto) {
		for(Lancamento lanc: lancLst) {
			if (lanc.getSituacao().equals(situacao)) {
				dto.setResponsavel(lanc.getUsuario().getNome());
				dto.setDias((int)DataUtil.calcularDiferencaDiasEntreUmaDataEAgora(lanc.getData()));
				if (dto.getDias() > qtdDiasAlerta) 
					dto.setAlerta("S");
				else
					dto.setAlerta("N");
			}
			if (lanc.getSituacao().equals("Aberto")) {
				dto.setDataAbertura(lanc.getData());
			}
		}
	}
	
	public static LancamentoDto converterOrdemLancamentoParaDto(Lancamento lanc) {
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
	
	public static Lancamento converterDtoParaFichaAtendimentoLanc(final LancamentoDto dto) {
		final Lancamento lanc = new Lancamento();
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
	
	public static Lancamento converterDtoParaOrdemServicoLanc(final LancamentoDto dto) {
		final Lancamento lanc = new Lancamento();
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
