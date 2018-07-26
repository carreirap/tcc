package br.com.fichasordens;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.FichaAtendLancEntity;
import br.com.fichasordens.entities.FichaAtendLancId;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoFichaIdEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.FichaAtendLancRepository;
import br.com.fichasordens.repository.FichaAtendimentoRepository;
import br.com.fichasordens.repository.PecaServicoFichaRepository;

@Component
public class FichaAtendimento {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FichaAtendimento.class);

	private Cliente cliente;
	private String tipoServico;
	private long id;
	
	private List<FichaAtendimentoLanc> fichaAtendimentoLanc;
	
	@Autowired
	private FichaAtendimentoRepository repository;
	
	@Autowired 
	FichaAtendLancRepository fichaAtendLancRepository;
	
	@Autowired
	private PecaServicoFichaRepository pecaServicoFichaRepository;
	
	@Transactional
	public FichaAtendimento salvarFicha(final FichaAtendimento fichaAtendimento) throws ExcecaoRetorno {
		try {
			FichaAtendimentoEntity ent = this.converterParaEntity(fichaAtendimento);
			ent = this.repository.save(ent);
			fichaAtendimento.setId(ent.getId());
			fichaAtendimento.getFichaAtendimentoLanc().get(0).setFichaAtendimento(fichaAtendimento);
			FichaAtendLancEntity lancEntity = this.converterFichaAtendLancParaEntity(fichaAtendimento.getFichaAtendimentoLanc().get(0));
			
			fichaAtendLancRepository.save(lancEntity);
			LOGGER.info("Ficha de Atendimento cadastrada id {}", ent.getId());
			return fichaAtendimento;
		} catch(Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar Ficha de Atendimento");
		}
	}
	
	@Transactional
	public void gravarPecaServicoFicha(final PecaOutroServico pecaOutroServico) {
		final PecaServicoFichaEntity ent = this.converterPecaServicoOrdemParaEntity(pecaOutroServico);
		pecaServicoFichaRepository.save(ent);
	}
	
	public Map<String,Integer> contarFichasPorSituacao( ) {
		List<FichaAtendimentoEntity> lst = this.repository.FindAllFichas();
		Map<String,Integer> map = new HashMap<String,Integer>();
		int qtdAberto = 0;
		int qtdTrabalhando = 0;
		int qtdAguardando = 0;
		int qtdFechado = 0;
		int qtdCancelado = 0;
				
		for (FichaAtendimentoEntity a : lst) {
			
			for (FichaAtendLancEntity lanc : a.getFichaAtendLancs()) {
				if (lanc.getSituacao().equals("Aberto")) {
					qtdAberto = qtdAberto + 1; 
				}
				if (lanc.getSituacao().equals("Trabalhando")) {
					qtdTrabalhando = qtdTrabalhando + 1; 
				}
				if (lanc.getSituacao().equals("Aguardando")) {
					qtdAguardando = qtdAguardando + 1; 
				}
				if (lanc.getSituacao().equals("Fechado")) {
					qtdFechado = qtdFechado + 1; 
				}
				if (lanc.getSituacao().equals("Cancelado")) {
					qtdCancelado = qtdCancelado + 1; 
				}
			}
		}
		map.put("Aberto", qtdAberto);
		map.put("Trabalhando", qtdTrabalhando);
		map.put("Aguardando", qtdAguardando);
		map.put("Fechado", qtdFechado);
		map.put("Cancelado", qtdCancelado);
		return map;
	}
	
	private PecaServicoFichaEntity converterPecaServicoOrdemParaEntity(PecaOutroServico pecaServicoFicha) {
		PecaServicoFichaEntity ent = new PecaServicoFichaEntity();
		ent.setDescricao(pecaServicoFicha.getDescricao());
		ent.setQuantidade(pecaServicoFicha.getQuantidade());
		ent.setValor(pecaServicoFicha.getValor());
		ent.setId(new PecaServicoFichaIdEntity());
		ent.getId().setFichaAtendId(pecaServicoFicha.getFichaAtendimento().getId());
		ent.getId().setSequencia(pecaServicoFicha.getId());
		return ent;
	}
	
	public FichaAtendimentoEntity converterParaEntity(final FichaAtendimento fichaAtendimento) {
		final FichaAtendimentoEntity ent = new FichaAtendimentoEntity();
		ent.setTipoServico(fichaAtendimento.getTipoServico());
		ent.setId(fichaAtendimento.getId());
		ClienteEntity cliente = new ClienteEntity();
		cliente.setId(fichaAtendimento.getCliente().getId());
		ent.setCliente(cliente);
		return ent;
	}
	
	private FichaAtendLancEntity converterFichaAtendLancParaEntity(final FichaAtendimentoLanc fichaAtendimentoLanc) {
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
		return ent;
		
	}
	
	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public List<FichaAtendimentoLanc> getFichaAtendimentoLanc() {
		return fichaAtendimentoLanc;
	}

	public void setFichaAtendimentoLanc(List<FichaAtendimentoLanc> fichaAtendimentoLanc) {
		this.fichaAtendimentoLanc = fichaAtendimentoLanc;
	}
	
}
