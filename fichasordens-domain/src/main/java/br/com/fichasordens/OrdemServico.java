package br.com.fichasordens;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.entities.OrdemServicoLancEntity;
import br.com.fichasordens.entities.OrdemServicoLancId;
import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntityId;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.OrdemServicoLancRepository;
import br.com.fichasordens.repository.OrdemServicoRepository;
import br.com.fichasordens.repository.PecaServicoOrdemRepository;
import br.com.fichasordens.util.ConversorCliente;
import br.com.fichasordens.util.ConversorOrdemServico;
import br.com.fichasordens.util.StatusServicoEnum;

@Component
public class OrdemServico {
	private long id;
	private String tipoServico;
	private String fabricante;
	private String modelo;
	private String serie;
	private String descEquip;
	private String descDefeito;
	private String estadoItensAcomp;
	
	private Cliente cliente;
	
	private List<Lancamento> lancamento;
	private List<PecaOutroServico> pecaOutroServico;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private OrdemServicoLancRepository ordemServicoLancRepository;
			
	@Transactional
	public OrdemServico salvarOrdem(final OrdemServico ordemServico) throws ExcecaoRetorno {
		OrdemServicoEntity ent = ConversorOrdemServico.converterParaEntity(ordemServico);
		try {
			OrdemServicoLancEntity lancEntity = ConversorOrdemServico.converterOrdemServicoLancParaEntity(ordemServico.getLancamento().get(0));
			ent = this.ordemServicoRepository.save(ent);
			lancEntity.setOrdemServico(new OrdemServicoEntity());
			lancEntity.getId().setOrdemServicoId(ent.getId());
			lancEntity.getOrdemServico().setId(ent.getId());
			ordemServico.setId(ent.getId());
			lancEntity.setAtualSituacao(true);
			OrdemServicoLancEntity lancEntityAnterior = this.ordemServicoLancRepository.findByOrdemServicoIdAndAtualSituacao(ent.getId(),  true);
			if (lancEntityAnterior != null) {
				lancEntityAnterior.setAtualSituacao(false);
				this.ordemServicoLancRepository.save(lancEntityAnterior);
			}
			this.ordemServicoLancRepository.save(lancEntity);
		} catch (Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar ordem de serviço");
		}
		
		return ordemServico;
	}
	
	@Transactional
	public OrdemServico buscarOrdem(final long id) {
		OrdemServicoEntity ent = this.ordemServicoRepository.findOne(id);
		return ConversorOrdemServico.converterEntityParaOrdemServico(ent);
	}
	
	
	
	@Transactional
	public List<OrdemServico> listarOrdens(final StatusServicoEnum situacao) {
		List<OrdemServicoEntity> entityList = this.buscarOrdensDeServicoPorSituacao(situacao);
		List<OrdemServico> ordemList = new ArrayList<>();
		entityList.forEach(a-> ordemList.add(ConversorOrdemServico.converterEntityParaOrdemServico(a)) );
		return ordemList;
	}

	
	public List<OrdemServicoEntity> buscarOrdensDeServicoPorSituacao(final StatusServicoEnum situacao) {
		return  this.ordemServicoRepository.findAllOrdensByStatus(situacao.getValue());
	}
	
	public List<OrdemServicoEntity> buscarOrdensDeServicoPorSituacao(final StatusServicoEnum situacao, final Date inicio, final Date fim) {
		return  this.ordemServicoRepository.findAllOrdensByStatusAndDataBetween(situacao.getValue(), inicio, fim);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	public String getDescEquip() {
		return descEquip;
	}

	public void setDescEquip(String descEquip) {
		this.descEquip = descEquip;
	}

	public String getDescDefeito() {
		return descDefeito;
	}

	public void setDescDefeito(String descDefeito) {
		this.descDefeito = descDefeito;
	}

	public String getEstadoItensAcomp() {
		return estadoItensAcomp;
	}

	public void setEstadoItensAcomp(String estadoItensAcomp) {
		this.estadoItensAcomp = estadoItensAcomp;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<PecaOutroServico> getPecaOutroServico() {
		return pecaOutroServico;
	}

	public void setPecaOutroServico(List<PecaOutroServico> pecaOutroServico) {
		this.pecaOutroServico = pecaOutroServico;
	}

	public List<Lancamento> getLancamento() {
		return lancamento;
	}

	public void setLancamento(List<Lancamento> lancamento) {
		this.lancamento = lancamento;
	}
	
}
