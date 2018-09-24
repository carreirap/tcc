package br.com.fichasordens;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.AtendimentoFichaEntity;
import br.com.fichasordens.entities.AtendimentoFichaId;
import br.com.fichasordens.entities.FichaAtendLancEntity;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoFichaIdEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.AtendimentoFichaRepository;
import br.com.fichasordens.repository.FichaAtendLancRepository;
import br.com.fichasordens.repository.FichaAtendimentoRepository;
import br.com.fichasordens.repository.PecaServicoFichaRepository;
import br.com.fichasordens.util.ConversorFichaAtendimento;
import br.com.fichasordens.util.StatusServicoEnum;

@Component
public class FichaAtendimento {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FichaAtendimento.class);

	private Cliente cliente;
	private String tipoServico;
	private long id;
	
	private List<Lancamento> fichaAtendimentoLancList;
	private List<PecaOutroServico> pecaOutroServicoList;
	private List<Atendimento> atendimentoList;
	
	@Autowired
	private FichaAtendimentoRepository repository;
	
	@Autowired 
	FichaAtendLancRepository fichaAtendLancRepository;
	
	@Transactional
	public FichaAtendimento salvarFicha(final FichaAtendimento fichaAtendimento) throws ExcecaoRetorno {
		try {
			FichaAtendimentoEntity ent = ConversorFichaAtendimento.converterParaEntity(fichaAtendimento);
			ent = this.repository.save(ent);
			fichaAtendimento.setId(ent.getId());
			fichaAtendimento.getFichaAtendimentoLancList().get(0).setFichaAtendimento(fichaAtendimento);
			FichaAtendLancEntity lancEntity = ConversorFichaAtendimento.converterFichaAtendLancParaEntity(fichaAtendimento.getFichaAtendimentoLancList().get(0));
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
	
	/*public List<FichaAtendimentoEntity> buscarFichasDeAtendimento() {
		return this.repository.findAllFichas();
	}*/
	
	
	
	@Transactional
	public List<FichaAtendimento> listarFichas(final StatusServicoEnum situacao) {
		List<FichaAtendimentoEntity> lst = this.buscarFichaAtendimentoPorSituacao(situacao);
		
		List<FichaAtendimento> fichaList = new ArrayList<>();
		lst.forEach(a-> {
			FichaAtendLancEntity ent = this.fichaAtendLancRepository.findBySituacaoAndFichaAtendimentoIdAndAtualSituacao(situacao.getValue(), a.getId(), true);
			ent.getUsuario().getId();
			a.getFichaAtendLancs().add(ent);
			fichaList.add(ConversorFichaAtendimento.converterEntityParaFichaAtendimento(a));
		});
		return fichaList;
	}
	
	public List<FichaAtendimentoEntity> buscarFichaAtendimentoPorSituacao(final StatusServicoEnum situacao) {
		return this.repository.findAllFichaByStatus(situacao.getValue());
	}
	
	public List<FichaAtendimentoEntity> buscarFichaAtendimentoPorSituacao(final StatusServicoEnum situacao, final Date inicio, final Date fim) {
		return this.repository.findAllFichaByStatusAndDataInicioAndDataFim(situacao.getValue(), inicio, fim);
	}
	
	@Transactional
	public FichaAtendimento buscarFichaAtendimento(final long id) {
		final FichaAtendimentoEntity ent = this.repository.findOne(id);
		return ConversorFichaAtendimento.converterEntityParaFichaAtendimento(ent);
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
	
	public List<Lancamento> getFichaAtendimentoLancList() {
		return fichaAtendimentoLancList;
	}

	public void setFichaAtendimentoLancList(List<Lancamento> fichaAtendimentoLanc) {
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
