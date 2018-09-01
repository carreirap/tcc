package br.com.fichasordens.util;

import java.util.ArrayList;
import java.util.List;

import br.com.fichasordens.Atendimento;
import br.com.fichasordens.Cliente;
import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.FichaAtendimentoLanc;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.Usuario;
import br.com.fichasordens.entities.AtendimentoFichaEntity;
import br.com.fichasordens.entities.AtendimentoFichaId;
import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.FichaAtendLancEntity;
import br.com.fichasordens.entities.FichaAtendLancId;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoFichaIdEntity;

public class ConversorFichaAtendimento {
	
	private ConversorFichaAtendimento() {}
	
	public static PecaServicoFichaEntity converterPecaServicoFichaParaEntity(PecaOutroServico pecaServicoFicha) {
		PecaServicoFichaEntity ent = new PecaServicoFichaEntity();
		ent.setDescricao(pecaServicoFicha.getDescricao());
		ent.setQuantidade(pecaServicoFicha.getQuantidade());
		ent.setValor(pecaServicoFicha.getValor());
		ent.setId(new PecaServicoFichaIdEntity());
		ent.getId().setFichaAtendId(pecaServicoFicha.getFichaAtendimento().getId());
		ent.getId().setSequencia(pecaServicoFicha.getId());
		return ent;
	}
	
	public static FichaAtendimentoEntity converterParaEntity(final FichaAtendimento fichaAtendimento) {
		final FichaAtendimentoEntity ent = new FichaAtendimentoEntity();
		ent.setId(fichaAtendimento.getId());
		ent.setTipoServico(fichaAtendimento.getTipoServico());
		ent.setId(fichaAtendimento.getId());
		ClienteEntity cliente = new ClienteEntity();
		cliente.setId(fichaAtendimento.getCliente().getId());
		ent.setCliente(cliente);
		return ent;
	}
	
	public static FichaAtendLancEntity converterFichaAtendLancParaEntity(final FichaAtendimentoLanc fichaAtendimentoLanc) {
		FichaAtendLancEntity ent = new FichaAtendLancEntity();
		ent.setData(fichaAtendimentoLanc.getData());
		ent.setObservacao(fichaAtendimentoLanc.getObservacao());
		ent.setSituacao(fichaAtendimentoLanc.getSituacao());
		ent.setFichaAtendimento(new FichaAtendimentoEntity());
		ent.setId(new FichaAtendLancId());
		ent.getId().setUsuarioId(fichaAtendimentoLanc.getUsuario().getId());
		ent.getId().setSequencia(fichaAtendimentoLanc.getSequencia());
		ent.getId().setFichaAtendimentoId(fichaAtendimentoLanc.getFichaAtendimento().getId());
		ent.getFichaAtendimento().setId(fichaAtendimentoLanc.getFichaAtendimento().getId());
		ent.setAtualSituacao(true);
		return ent;
		
	}
	
	public static FichaAtendimento converterEntityParaFichaAtendimento(final FichaAtendimentoEntity entity) {
		FichaAtendimento ficha = new FichaAtendimento();
		ficha.setId(entity.getId());
		ficha.setTipoServico(entity.getTipoServico());
		Cliente cliente = new Cliente();
		cliente.setId(entity.getCliente().getId());
		cliente.setNome(entity.getCliente().getNome());
		cliente.setCelular(entity.getCliente().getCelular());
		cliente.setFone(entity.getCliente().getFone());
		cliente.setCnpjCpf(entity.getCliente().getCnpjCpf());
		ficha.setCliente(cliente);
		if (entity.getFichaAtendLancs().size() > 0) {
			ficha.setFichaAtendimentoLancList(new ArrayList<FichaAtendimentoLanc>());
			entity.getFichaAtendLancs().forEach(a-> {
				FichaAtendimentoLanc lanc = new FichaAtendimentoLanc();
				lanc.setSequencia(a.getId().getSequencia());
				lanc.setSituacao(a.getSituacao());
				lanc.setData(a.getData());
				Usuario user = new Usuario();
				user.setNome(a.getUsuario().getNome());
				user.setId(a.getUsuario().getId());
				lanc.setUsuario(user);
				lanc.setObservacao(a.getObservacao());
				lanc.setFichaAtendimento(ficha);
				ficha.getFichaAtendimentoLancList().add(lanc);
			});
		}
		if (entity.getPecaServicoFichas().size() > 0) {
			ficha.setPecaOutroServicoList(converterEntityParaPecaOutroServico(entity, ficha));
		}
		
		if (entity.getAtendimentoFichas().size() > 0) {
			ficha.setAtendimentoList(converterEntityParaAtendimentoFicha(entity, ficha));
		}
		/*if(entity.getAtendimentoFichas().size() > 0) {
			List<Atendimento> atendimentoList = new ArrayList<Atendimento>();
			for(AtendimentoFichaEntity p: entity.getAtendimentoFichas()) {
				Atendimento atend = new Atendimento();
				atend.setData(p.getDate());
				atend.setDescricao(p.getDescricao());
				atend.setDuracao(p.getDuracao());
				atend.setSequencia(p.getId().getSequencia());
				atend.setFichaAtendimento(ficha);
				atendimentoList.add(atend);
			}
		}*/
		return ficha;
	}

	public static List<Atendimento> converterEntityParaAtendimentoFicha(final FichaAtendimentoEntity entity, FichaAtendimento ficha) {
		List<Atendimento> list = new ArrayList<Atendimento>();
		entity.getAtendimentoFichas().forEach(a-> {
			Atendimento atend = new Atendimento();
			atend.setData(a.getDate());
			atend.setDescricao(a.getDescricao());
			atend.setDuracao(a.getDuracao());
			atend.setFichaAtendimento(ficha);
			atend.setSequencia(a.getId().getSequencia());
			atend.setValor(a.getValor());
			list.add(atend);
		});
		return list;
	}

	public static List<PecaOutroServico> converterEntityParaPecaOutroServico(final FichaAtendimentoEntity entity, final FichaAtendimento ficha) {
		List<PecaOutroServico> list = new ArrayList<>();
		for (PecaServicoFichaEntity a: entity.getPecaServicoFichas()) {
			PecaOutroServico servico = new PecaOutroServico();
			servico.setId(a.getId().getSequencia());
			servico.setDescricao(a.getDescricao());
			servico.setQuantidade(a.getQuantidade());
			servico.setValor(a.getValor());
			servico.setFichaAtendimento(ficha);
			list.add(servico);
		}
		return list;
	}
	
	public static AtendimentoFichaEntity converterAtendimentoParaEntity(final Atendimento atendimento) {
		final AtendimentoFichaEntity ent = new AtendimentoFichaEntity();
		ent.setDescricao(atendimento.getDescricao());
		ent.setDuracao(atendimento.getDuracao());
		ent.setDate(atendimento.getData());
		ent.setValor(atendimento.getValor());
		ent.setId(new AtendimentoFichaId());
		ent.getId().setSequencia(atendimento.getSequencia());
		ent.getId().setFichaAtendimentoId(atendimento.getFichaAtendimento().getId());
		
		return ent;
	}

}
