package br.com.fichasordens.util;

import java.util.ArrayList;
import java.util.List;

import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.entities.OrdemServicoEntity;

public final class Conversor {
	
	
	public static List<PecaOutroServico> converterPecaServicoEntityParaPecaOutroServico(final OrdemServicoEntity entity) {
		List<PecaOutroServico> lst = new ArrayList<PecaOutroServico>();
		entity.getPecaServicoOrdems().forEach(a -> {
			PecaOutroServico peca = new PecaOutroServico();
			peca.setDescricao(a.getDescricao());
			peca.setQuantidade(a.getQuantidade());
			peca.setValor(a.getValor());
			peca.setId(a.getId().getSequencia());
			lst.add(peca);
		});
		return lst;
	}
}
