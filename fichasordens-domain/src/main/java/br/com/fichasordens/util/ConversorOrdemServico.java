package br.com.fichasordens.util;

import java.util.ArrayList;
import java.util.List;

import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.entities.OrdemServicoEntity;

public final class ConversorOrdemServico {
	
	private ConversorOrdemServico() {}
	
	
	public static List<PecaOutroServico> converterPecaServicoEntityParaPecaOutroServico(final OrdemServicoEntity entity, final OrdemServico ordemServico) {
		List<PecaOutroServico> lst = new ArrayList<>();
		entity.getPecaServicoOrdems().forEach(a -> {
			PecaOutroServico peca = new PecaOutroServico();
			peca.setDescricao(a.getDescricao());
			peca.setQuantidade(a.getQuantidade());
			peca.setValor(a.getValor());
			peca.setId(a.getId().getSequencia());
			peca.setOrdemServico(ordemServico);
			lst.add(peca);
		});
		return lst;
	}
}
