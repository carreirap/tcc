package br.com.fichasordens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private List<OrdemServicoLanc> ordemServicoLanc;
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
	public OrdemServico gravarOrdem(final OrdemServico ordemServico) throws ExcecaoRetorno {
		OrdemServicoEntity ent = this.converterParaEntity(ordemServico);
		try {
			OrdemServicoLancEntity lancEntity = this.converterOrdemServicoLancParaEntity(ordemServico.getOrdemServicoLanc().get(0));
			ent = this.ordemServicoRepository.save(ent);
			lancEntity.setOrdemServico(new OrdemServicoEntity());
			lancEntity.getId().setOrdemServicoId(ent.getId());
			lancEntity.getOrdemServico().setId(ent.getId());
			ordemServico.setId(ent.getId());
			lancEntity.setAtualSituacao(true);
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
		return this.converterEntityParaOrdemServico(ent);
	}
	
	public PecaOutroServico gravarPecaServicoOrdem(PecaOutroServico pecaServicoOrdem) throws ExcecaoRetorno {
		PecaServicoOrdemEntity entity = converterPecaServicoOrdemParaEntity(pecaServicoOrdem);
		try {
			entity = this.pecaServicoOrdemRepository.save(entity);
		} catch (Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar peças/outro serviços");
		}
		return null;
	}
	
	@Transactional
	public OrdemServicoLanc gravarOrdemServicoLanc(final OrdemServicoLanc ordemServicoLanc) throws ExcecaoRetorno {
		OrdemServicoLancEntity entity = converterOrdemServicoLancParaEntity(ordemServicoLanc);
		try {
			List<Usuario> usuarioLst = this.usuario.listarUsuario(entity.getUsuario().getUsuario());
			entity.getUsuario().setId(usuarioLst.get(0).getId());
			entity = this.ordemServicoLancRepository.save(entity);
		} catch (Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar ordem de serviço");
		}
		return null;
	}
	
	@Transactional
	public void deletarPecaOutroServico(final int id, final int sequencia) {
		PecaServicoOrdemEntity ent = new PecaServicoOrdemEntity();
		ent.setId(new PecaServicoOrdemEntityId());
		ent.getId().setSequencia(sequencia);
		ent.getId().setOrdemServicoId(id);;
		this.pecaServicoOrdemRepository.delete(ent);
	}

	public Map<String,Integer> contarOrdensPorSituacao() {
		List<OrdemServicoEntity> lst = this.ordemServicoRepository.FindAllOrdens();
		return calcularTotais(lst);
	}

	private Map<String,Integer> calcularTotais(List<OrdemServicoEntity> lst) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		int qtdAberto = 0;
		int qtdTrabalhando = 0;
		int qtdAguardando = 0;
		int qtdFechado = 0;
		int qtdCancelado = 0;
		for (OrdemServicoEntity a : lst) {
			for (OrdemServicoLancEntity lanc : a.getOrdemServicoLancs()) {
				if (lanc.getSituacao().equals("Aberto") && lanc.getAtualSituacao()) {
					qtdAberto = qtdAberto + 1; 
				}
				if (lanc.getSituacao().equals("Trabalhando") && lanc.getAtualSituacao()) {
					qtdTrabalhando = qtdTrabalhando + 1; 
				}
				if (lanc.getSituacao().equals("Aguardando") && lanc.getAtualSituacao()) {
					qtdAguardando = qtdAguardando + 1; 
				}
				if (lanc.getSituacao().equals("Fechado") && lanc.getAtualSituacao()) {
					qtdFechado = qtdFechado + 1; 
				}
				if (lanc.getSituacao().equals("Cancelado") && lanc.getAtualSituacao()) {
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
	
	private OrdemServicoEntity converterParaEntity(final OrdemServico ordemServico) {
		final OrdemServicoEntity ent = new OrdemServicoEntity();
		ent.setFrabricante(ordemServico.getFabricante());
		ent.setDescDefeito(ordemServico.getDescDefeito());
		ent.setDescEquip(ordemServico.getDescEquip());
		//îent.setDescServico(ordemServico.getDescServico());
		ent.setEstadoItensAcomp(ordemServico.getEstadoItensAcomp());
		ent.setModelo(ordemServico.getModelo());
		ent.setSerie(ordemServico.getSerie());
		ent.setTipoServico(ordemServico.getTipoServico());
		final ClienteEntity cliente = new ClienteEntity();
		cliente.setId(ordemServico.getCliente().getId());
		ent.setCliente(cliente);
		
		return ent;
	}
	
	private OrdemServico converterEntityParaOrdemServico(final OrdemServicoEntity entity) {
		final OrdemServico ordem = new OrdemServico();
		ordem.setFabricante(entity.getFrabricante());
		ordem.setDescDefeito(entity.getDescDefeito());
		ordem.setDescEquip(entity.getDescEquip());
		//îent.setDescServico(ordemServico.getDescServico());
		ordem.setEstadoItensAcomp(entity.getEstadoItensAcomp());
		ordem.setModelo(entity.getModelo());
		ordem.setSerie(entity.getSerie());
		ordem.setTipoServico(entity.getTipoServico());
		final Cliente cliente = new Cliente();
		cliente.setId(entity.getCliente().getId());
		cliente.setNome(entity.getCliente().getNome());
		cliente.setCnpjCpf(entity.getCliente().getCnpjCpf());
		cliente.setCelular(entity.getCliente().getCelular());
		cliente.setFone(entity.getCliente().getFone());
		ordem.setCliente(cliente);
		ordem.setOrdemServicoLanc(convertLancEntityParaOrdemServicoLanc(entity));
		ordem.setPecaOutroServico(converterPecaServicoEntityParaPecaOutroServico(entity));
		return ordem;
	}

	private List<PecaOutroServico> converterPecaServicoEntityParaPecaOutroServico(final OrdemServicoEntity entity) {
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

	private List<OrdemServicoLanc> convertLancEntityParaOrdemServicoLanc(final OrdemServicoEntity entity) {
		List<OrdemServicoLanc> lst = new ArrayList<OrdemServicoLanc>();
		entity.getOrdemServicoLancs().forEach(a ->
		{
			OrdemServicoLanc lanc = new OrdemServicoLanc();
			lanc.setData(a.getData());
			lanc.setObservacao(a.getObservacao());
			lanc.setSequencia((int)a.getId().getSequencia());
			lanc.setSituacao(a.getSituacao());
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
	
	private OrdemServicoLancEntity converterOrdemServicoLancParaEntity(final OrdemServicoLanc ordemServicoLanc) {
		OrdemServicoLancEntity ent = new OrdemServicoLancEntity();
		ent.setData(ordemServicoLanc.getData());
		ent.setObservacao(ordemServicoLanc.getObservacao());
		ent.setSituacao(ordemServicoLanc.getSituacao());
		ent.setOrdemServico(new OrdemServicoEntity());
		ent.setId(new OrdemServicoLancId());
		ent.getId().setUsuarioId(ordemServicoLanc.getUsuario().getId());
		ent.getId().setSequencia(ordemServicoLanc.getSequencia());
		ent.getOrdemServico().setId(ordemServicoLanc.getOrdemServico().getId());
//		ent.setUsuario(new UsuarioEntity());
//		ent.getUsuario().setUsuario(ordemServicoLanc.getUsuario().getId());
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

	public List<OrdemServicoLanc> getOrdemServicoLanc() {
		return ordemServicoLanc;
	}

	public void setOrdemServicoLanc(List<OrdemServicoLanc> ordemServicoLanc) {
		this.ordemServicoLanc = ordemServicoLanc;
	}

}
