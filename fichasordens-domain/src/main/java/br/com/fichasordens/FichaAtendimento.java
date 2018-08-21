package br.com.fichasordens;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.AtendimentoFichaEntity;
import br.com.fichasordens.entities.AtendimentoFichaId;
import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.FichaAtendLancEntity;
import br.com.fichasordens.entities.FichaAtendLancId;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoFichaIdEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.AtendimentoFichaRepository;
import br.com.fichasordens.repository.FichaAtendLancRepository;
import br.com.fichasordens.repository.FichaAtendimentoRepository;
import br.com.fichasordens.repository.PecaServicoFichaRepository;
import br.com.fichasordens.util.DashBoardDto;
import br.com.fichasordens.util.StatusServicoEnum;

@Component
public class FichaAtendimento {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FichaAtendimento.class);

	private Cliente cliente;
	private String tipoServico;
	private long id;
	
	private List<FichaAtendimentoLanc> fichaAtendimentoLancList;
	private List<PecaOutroServico> pecaOutroServicoList;
	private List<Atendimento> atendimentoList;
	
	@Autowired
	private FichaAtendimentoRepository repository;
	
	@Autowired 
	FichaAtendLancRepository fichaAtendLancRepository;
	
	@Autowired
	private PecaServicoFichaRepository pecaServicoFichaRepository;
	
	@Autowired
	private AtendimentoFichaRepository atendimentoRepository;
	
	@Autowired
	private Parametro parametro;
	
	@Transactional
	public FichaAtendimento salvarFicha(final FichaAtendimento fichaAtendimento) throws ExcecaoRetorno {
		try {
			FichaAtendimentoEntity ent = this.converterParaEntity(fichaAtendimento);
			ent = this.repository.save(ent);
			fichaAtendimento.setId(ent.getId());
			fichaAtendimento.getFichaAtendimentoLancList().get(0).setFichaAtendimento(fichaAtendimento);
			FichaAtendLancEntity lancEntity = this.converterFichaAtendLancParaEntity(fichaAtendimento.getFichaAtendimentoLancList().get(0));
			FichaAtendLancEntity lancEntityAnterior = this.fichaAtendLancRepository.findByFichaAtendimentoIdAndAtualSituacao(ent.getId(), true);
			if (lancEntityAnterior != null) {
				lancEntityAnterior.setAtualSituacao(false);
				this.fichaAtendLancRepository.save(lancEntityAnterior);
			}
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
	
	@Transactional
	public void gravarAtendimento(final Atendimento atendimento) {
		final AtendimentoFichaEntity ent = this.converterAtendimentoParaEntity(atendimento);
		atendimentoRepository.save(ent);
	}
	
	public Map<String,DashBoardDto> contarFichasPorSituacao( ) {
		List<FichaAtendimentoEntity> lst = this.repository.FindAllFichas();
		return calcularTotais(lst);
	}
	
	public BigDecimal calcularValorAtendimento(final int horas, final int tipo) {
		Parametro param = this.parametro.recuperarParametros().get(tipo);
		return new BigDecimal(horas).multiply(param.getValor());
	}

	private Map<String,DashBoardDto> calcularTotais(List<FichaAtendimentoEntity> lst) {
		Map<String,DashBoardDto> map = new HashMap<String,DashBoardDto>();
		int qtdAberto = 0;
		int qtdTrabalhando = 0;
		int qtdAguardando = 0;
		int qtdFechado = 0;
		int qtdFinalizado = 0;
		int qtdCancelado = 0;
		int qtdFaturado = 0;
		BigDecimal totalAberto = new BigDecimal(0);
		BigDecimal totalTrabalhando = new BigDecimal(0);
		BigDecimal totalFechado = new BigDecimal(0);
		BigDecimal totalFinalizado = new BigDecimal(0);
		BigDecimal totalFaturado = new BigDecimal(0);
		for (FichaAtendimentoEntity a : lst) {
			for (FichaAtendLancEntity lanc : a.getFichaAtendLancs()) {
				if (lanc.getSituacao().equals(StatusServicoEnum.ABERTO.getValue()) && lanc.getAtualSituacao()) {
					qtdAberto = qtdAberto + 1; 
					totalAberto = totalAberto.add(calcularTotalPecaServicos(a));
				}
				if (lanc.getSituacao().equals(StatusServicoEnum.TRABALHANDO.getValue()) && lanc.getAtualSituacao()) {
					qtdTrabalhando = qtdTrabalhando + 1; 
					totalTrabalhando = totalTrabalhando.add(calcularTotalPecaServicos(a));
				}
				if (lanc.getSituacao().equals(StatusServicoEnum.AGUARDANDO.getValue()) && lanc.getAtualSituacao()) {
					qtdAguardando = qtdAguardando + 1; 
					totalTrabalhando = totalTrabalhando.add(calcularTotalPecaServicos(a));
				}
				if (lanc.getSituacao().equals(StatusServicoEnum.FINALIZADO.getValue()) && lanc.getAtualSituacao()) {
					qtdFinalizado = qtdFinalizado + 1; 
					totalFinalizado = totalFinalizado.add(calcularTotalPecaServicos(a));
				}
				if (lanc.getSituacao().equals(StatusServicoEnum.FECHADO.getValue()) && lanc.getAtualSituacao()) {
					qtdFechado = qtdFechado + 1; 
					totalFechado = totalFechado.add(calcularTotalPecaServicos(a));
				}
				if (lanc.getSituacao().equals(StatusServicoEnum.CANCELADO.getValue()) && lanc.getAtualSituacao()) {
					qtdCancelado = qtdCancelado + 1; 
					totalFechado = totalFechado.add(calcularTotalPecaServicos(a));
				}
				if (lanc.getSituacao().equals(StatusServicoEnum.FATURADO.getValue()) && lanc.getAtualSituacao()) {
					qtdFaturado = qtdFaturado + 1;
					totalFaturado = totalFaturado.add(calcularTotalPecaServicos(a));
				}
			}
		}
		map.put("Aberto", new DashBoardDto(qtdAberto, totalAberto));
		map.put("Trabalhando", new DashBoardDto(qtdTrabalhando, totalTrabalhando));
		map.put("Aguardando", new DashBoardDto(qtdAguardando, totalTrabalhando));
		map.put("Fechado", new DashBoardDto(qtdFechado, totalFechado));
		map.put("Finalizado", new DashBoardDto(qtdFinalizado,totalFinalizado));
		map.put("Cancelado", new DashBoardDto(qtdCancelado, totalFechado));
		map.put("Faturado", new DashBoardDto(qtdFaturado, totalFaturado));
		return map;
	}
	
	private BigDecimal calcularTotalPecaServicos(FichaAtendimentoEntity ficha) {
		BigDecimal totalPecaServico = new BigDecimal(0);
		for (PecaServicoFichaEntity lanc : ficha.getPecaServicoFichas()) {
			BigDecimal resultado = totalPecaServico.add(lanc.getValor());
			totalPecaServico = resultado;
		}
		return totalPecaServico;
	}
	
	@Transactional
	public List<FichaAtendimento> listarFichas(final StatusServicoEnum situacao) {
		List<FichaAtendimentoEntity> lst = this.repository.FindAllFichaByStatus(situacao.getValue());
		
		List<FichaAtendimento> fichaList = new ArrayList<FichaAtendimento>();
		lst.forEach(a-> {
			FichaAtendLancEntity ent = this.fichaAtendLancRepository.findBySituacaoAndFichaAtendimentoIdAndAtualSituacao(situacao.getValue(), a.getId(), true);
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
	
	@Transactional
	public void deletarAtendimento(final long idFicha, final int sequencia) {
		AtendimentoFichaEntity ent = new AtendimentoFichaEntity();
		ent.setId(new AtendimentoFichaId());
		ent.getId().setSequencia(sequencia);
		ent.getId().setFichaAtendimentoId(idFicha);
		this.atendimentoRepository.delete(ent);
	}
	
	@Transactional
	public void deletarPecaOutroServico(final long idFicha, final int sequencia) {
		PecaServicoFichaEntity ent = new PecaServicoFichaEntity();
		ent.setId(new PecaServicoFichaIdEntity());
		ent.getId().setSequencia(sequencia);
		ent.getId().setFichaAtendId(idFicha);
		this.pecaServicoFichaRepository.delete(ent);
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
		if(entity.getAtendimentoFichas().size() > 0) {
			List<Atendimento> atendimentoList = new ArrayList<Atendimento>();
			for(AtendimentoFichaEntity p: entity.getAtendimentoFichas()) {
				Atendimento atend = new Atendimento();
				atend.setData(p.getDate());
				atend.setDescricao(p.getDescricao());
				atend.setDuracao(p.getDuracao().floatValue());
				atend.setSequencia(p.getId().getSequencia());
				atend.setFichaAtendimento(ficha);
				atendimentoList.add(atend);
			}
		}
		return ficha;
	}

	private List<Atendimento> converterEntityParaAtendimentoFicha(final FichaAtendimentoEntity entity, FichaAtendimento ficha) {
		List<Atendimento> list = new ArrayList<Atendimento>();
		entity.getAtendimentoFichas().forEach(a-> {
			Atendimento atend = new Atendimento();
			atend.setData(a.getDate());
			atend.setDescricao(a.getDescricao());
			atend.setDuracao(a.getDuracao().floatValue());
			atend.setFichaAtendimento(ficha);
			atend.setSequencia(a.getId().getSequencia());
			atend.setValor(a.getValor());
			list.add(atend);
		});
		return list;
	}

	private List<PecaOutroServico> converterEntityParaPecaOutroServico(final FichaAtendimentoEntity entity, final FichaAtendimento ficha) {
		List<PecaOutroServico> list = new ArrayList<PecaOutroServico>();
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
	
	private AtendimentoFichaEntity converterAtendimentoParaEntity(final Atendimento atendimento) {
		final AtendimentoFichaEntity ent = new AtendimentoFichaEntity();
		ent.setDescricao(atendimento.getDescricao());
		ent.setDuracao(new BigDecimal(atendimento.getDuracao()));
		ent.setDate(atendimento.getData());
		ent.setValor(atendimento.getValor());
		ent.setId(new AtendimentoFichaId());
		ent.getId().setSequencia(atendimento.getSequencia());
		ent.getId().setFichaAtendimentoId(atendimento.getFichaAtendimento().getId());
		
		return ent;
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
		ent.setId(fichaAtendimento.getId());
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
		ent.setAtualSituacao(true);
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

	public List<Atendimento> getAtendimentoList() {
		return atendimentoList;
	}

	public void setAtendimentoList(List<Atendimento> atendimentoList) {
		this.atendimentoList = atendimentoList;
	}
}
