package br.com.fichasordens;



import java.util.ArrayList;
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
	
	private List<FichaAtendimentoLanc> fichaAtendimentoLancList;
	private List<PecaOutroServico> pecaOutroServicoList;
	
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
			fichaAtendimento.getFichaAtendimentoLancList().get(0).setFichaAtendimento(fichaAtendimento);
			FichaAtendLancEntity lancEntity = this.converterFichaAtendLancParaEntity(fichaAtendimento.getFichaAtendimentoLancList().get(0));
			
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
	
	@Transactional
	public List<FichaAtendimento> listarFichas(final String situacao) {
		List<FichaAtendimentoEntity> lst = this.repository.FindAllFichaByStatus(situacao);
		if (!situacao.equals("Aberto")) {
			
		}
		List<FichaAtendimento> fichaList = new ArrayList<FichaAtendimento>();
		lst.forEach(a-> {
			FichaAtendLancEntity ent = this.fichaAtendLancRepository.findBySituacaoAndFichaAtendimentoId("Aberto", a.getId());
			ent.getUsuario().getId();
			a.getFichaAtendLancs().add(ent);
			fichaList.add(this.converterEntityParaFichaAtendimento(a));
		});
		return fichaList;
	}
	
	@Transactional
	public FichaAtendimento buscarFicha(final long id) {
		final FichaAtendimentoEntity ent = this.repository.findOne(id);
		FichaAtendimento ficha = this.converterEntityParaFichaAtendimento(ent);
		return ficha;
	}
	
	
	private FichaAtendimento converterEntityParaFichaAtendimento(final FichaAtendimentoEntity entity) {
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
			ficha.setPecaOutroServicoList(new ArrayList<PecaOutroServico>());
			for (PecaServicoFichaEntity a: entity.getPecaServicoFichas()) {
				PecaOutroServico servico = new PecaOutroServico();
				servico.setId(a.getId().getSequencia());
				servico.setDescricao(a.getDescricao());
				servico.setQuantidade(a.getQuantidade());
				servico.setValor(a.getValor());
				servico.setFichaAtendimento(ficha);
				ficha.getPecaOutroServicoList().add(servico);
			}
		}
		return ficha;
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
	
	public List<FichaAtendimentoLanc> getFichaAtendimentoLancList() {
		return fichaAtendimentoLancList;
	}

	public void setFichaAtendimentoLancList(List<FichaAtendimentoLanc> fichaAtendimentoLanc) {
		this.fichaAtendimentoLancList = fichaAtendimentoLanc;
	}

	public List<PecaOutroServico> getPecaOutroServicoList() {
		return pecaOutroServicoList;
	}

	public void setPecaOutroServicoList(List<PecaOutroServico> pecaOutroServicoList) {
		this.pecaOutroServicoList = pecaOutroServicoList;
	}
}
