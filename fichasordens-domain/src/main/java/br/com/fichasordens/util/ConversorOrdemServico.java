package br.com.fichasordens.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.Lancamento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.Usuario;
import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.entities.OrdemServicoLancEntity;
import br.com.fichasordens.entities.OrdemServicoLancId;
import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntityId;

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
	
	public static OrdemServicoEntity converterParaEntity(final OrdemServico ordemServico) {
		final OrdemServicoEntity ent = new OrdemServicoEntity();
		ent.setId(ordemServico.getId());
		ent.setFrabricante(ordemServico.getFabricante());
		ent.setDescDefeito(ordemServico.getDescDefeito());
		ent.setDescEquip(ordemServico.getDescEquip());
		ent.setEstadoItensAcomp(ordemServico.getEstadoItensAcomp());
		ent.setModelo(ordemServico.getModelo());
		ent.setSerie(ordemServico.getSerie());
		ent.setTipoServico(ordemServico.getTipoServico());
		final ClienteEntity cli = new ClienteEntity();
		cli.setId(ordemServico.getCliente().getId());
		ent.setCliente(cli);
		
		return ent;
	}
	
	public static OrdemServico converterEntityParaOrdemServico(final OrdemServicoEntity entity) {
		final OrdemServico ordem = new OrdemServico();
		ordem.setFabricante(entity.getFrabricante());
		ordem.setDescDefeito(entity.getDescDefeito());
		ordem.setDescEquip(entity.getDescEquip());
		ordem.setId(entity.getId());
		ordem.setEstadoItensAcomp(entity.getEstadoItensAcomp());
		ordem.setModelo(entity.getModelo());
		ordem.setSerie(entity.getSerie());
		ordem.setTipoServico(entity.getTipoServico());
		final Cliente cli = ConversorCliente.converterClienteEntityParaCliente(entity.getCliente());
		ordem.setCliente(cli);
		ordem.setLancamento(convertLancEntityParaOrdemServicoLanc(entity, ordem));
		ordem.setPecaOutroServico(ConversorOrdemServico.converterPecaServicoEntityParaPecaOutroServico(entity, ordem));
		return ordem;
	}

	public static List<Lancamento> convertLancEntityParaOrdemServicoLanc(final OrdemServicoEntity entity, final OrdemServico ordemServico) {
		List<Lancamento> lst = new ArrayList<>();
		entity.getOrdemServicoLancs()		        
			.forEach(a ->
			{
				Lancamento lanc = new Lancamento();
				lanc.setData(a.getData());
				lanc.setObservacao(a.getObservacao());
				lanc.setSequencia((int)a.getId().getSequencia());
				lanc.setSituacao(a.getSituacao());
				lanc.setOrdemServico(ordemServico);
				lanc.setUsuario(new Usuario());
				lanc.getUsuario().setId(a.getUsuario().getId());
				lanc.getUsuario().setNome(a.getUsuario().getNome());
				lst.add(lanc);
		});
		return lst;
	}
	
	public static PecaServicoOrdemEntity converterPecaServicoOrdemParaEntity(PecaOutroServico pecaServicoOrdem) {
		PecaServicoOrdemEntity ent = new PecaServicoOrdemEntity();
		ent.setDescricao(pecaServicoOrdem.getDescricao());
		ent.setQuantidade(pecaServicoOrdem.getQuantidade());
		ent.setValor(pecaServicoOrdem.getValor());
		ent.setId(new PecaServicoOrdemEntityId());
		ent.getId().setOrdemServicoId(pecaServicoOrdem.getOrdemServico().getId());
		ent.getId().setSequencia(pecaServicoOrdem.getId());
		ent.setData(new Date());
		return ent;
	}
	
	public static OrdemServicoLancEntity converterOrdemServicoLancParaEntity(final Lancamento lancamento) {
		OrdemServicoLancEntity ent = new OrdemServicoLancEntity();
		ent.setData(lancamento.getData());
		ent.setObservacao(lancamento.getObservacao());
		ent.setSituacao(lancamento.getSituacao());
		ent.setOrdemServico(new OrdemServicoEntity());
		ent.setId(new OrdemServicoLancId());
		ent.getId().setUsuarioId(lancamento.getUsuario().getId());
		ent.getId().setSequencia(lancamento.getSequencia());
		ent.getOrdemServico().setId(lancamento.getOrdemServico().getId());
		return ent;
		
	}
}
