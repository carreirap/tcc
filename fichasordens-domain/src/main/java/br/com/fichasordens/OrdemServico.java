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
import br.com.fichasordens.util.ConversorOrdemServico;
import br.com.fichasordens.util.StatusServicoEnum;
import br.com.fichasordens.util.ConversorCliente;

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
	private String descServico;
	private Cliente cliente;
	
	private List<Lancamento> lancamento;
	private List<PecaOutroServico> pecaOutroServico;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private PecaServicoOrdemRepository pecaServicoOrdemRepository;
	
	@Autowired
	private OrdemServicoLancRepository ordemServicoLancRepository;
	
	@Autowired
	private Usuario usuario;
	
	@Transactional
	public OrdemServico salvarOrdem(final OrdemServico ordemServico) throws ExcecaoRetorno {
		OrdemServicoEntity ent = this.converterParaEntity(ordemServico);
		try {
			OrdemServicoLancEntity lancEntity = this.converterOrdemServicoLancParaEntity(ordemServico.getLancamento().get(0));
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
		ent.getOrdemServicoLancs();
		ent.getPecaServicoOrdems();
		ent.getCliente().getId();
		ent.getCliente().getEndereco().getId();
		return this.converterEntityParaOrdemServico(ent);
	}
	
	public void gravarPecaServicoOrdem(PecaOutroServico pecaServicoOrdem) throws ExcecaoRetorno {
		PecaServicoOrdemEntity entity = converterPecaServicoOrdemParaEntity(pecaServicoOrdem);
		try {
			this.pecaServicoOrdemRepository.save(entity);
		} catch (Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar peças/outro serviços");
		}
	}
	
	@Transactional
	public void deletarPecaOutroServico(final int id, final int sequencia) {
		final PecaServicoOrdemEntity ent = new PecaServicoOrdemEntity();
		ent.setId(new PecaServicoOrdemEntityId());
		ent.getId().setSequencia(sequencia);
		ent.getId().setOrdemServicoId(id);
		this.pecaServicoOrdemRepository.delete(ent);
	}
	
	@Transactional
	public List<OrdemServico> listarOrdens(final StatusServicoEnum situacao) {
		List<OrdemServicoEntity> entityList = this.buscarOrdensDeServicoPorSituacao(situacao);
		List<OrdemServico> ordemList = new ArrayList<>();
		entityList.forEach(a-> ordemList.add(converterEntityParaOrdemServico(a)) );
		return ordemList;
	}

	
	public List<OrdemServicoEntity> buscarOrdensDeServicoPorSituacao(final StatusServicoEnum situacao) {
		return  this.ordemServicoRepository.findAllOrdensByStatus(situacao.getValue());
	}
	
	public List<OrdemServicoEntity> buscarOrdensDeServicoPorSituacao(final StatusServicoEnum situacao, final Date inicio, final Date fim) {
		return  this.ordemServicoRepository.findAllOrdensByStatusAndDataBetween(situacao.getValue(), inicio, fim);
	}
	
	private OrdemServicoEntity converterParaEntity(final OrdemServico ordemServico) {
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
	
	private OrdemServico converterEntityParaOrdemServico(final OrdemServicoEntity entity) {
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

	private List<Lancamento> convertLancEntityParaOrdemServicoLanc(final OrdemServicoEntity entity, final OrdemServico ordemServico) {
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
	
	private PecaServicoOrdemEntity converterPecaServicoOrdemParaEntity(PecaOutroServico pecaServicoOrdem) {
		PecaServicoOrdemEntity ent = new PecaServicoOrdemEntity();
		ent.setDescricao(pecaServicoOrdem.getDescricao());
		ent.setQuantidade(pecaServicoOrdem.getQuantidade());
		ent.setValor(pecaServicoOrdem.getValor());
		ent.setId(new PecaServicoOrdemEntityId());
		ent.getId().setOrdemServicoId(pecaServicoOrdem.getOrdemServico().getId());
		ent.getId().setSequencia(pecaServicoOrdem.getId());
		return ent;
	}
	
	private OrdemServicoLancEntity converterOrdemServicoLancParaEntity(final Lancamento lancamento) {
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

	public String getDescServico() {
		return descServico;
	}

	public void setDescServico(String descServico) {
		this.descServico = descServico;
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
