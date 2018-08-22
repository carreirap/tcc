package br.com.fichasordens;

import java.math.BigDecimal;
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
import br.com.fichasordens.util.DashBoardDto;
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
	public OrdemServico salvarOrdem(final OrdemServico ordemServico) throws ExcecaoRetorno {
		OrdemServicoEntity ent = this.converterParaEntity(ordemServico);
		try {
			OrdemServicoLancEntity lancEntity = this.converterOrdemServicoLancParaEntity(ordemServico.getOrdemServicoLanc().get(0));
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
		final PecaServicoOrdemEntity ent = new PecaServicoOrdemEntity();
		ent.setId(new PecaServicoOrdemEntityId());
		ent.getId().setSequencia(sequencia);
		ent.getId().setOrdemServicoId(id);;
		this.pecaServicoOrdemRepository.delete(ent);
	}
	
	@Transactional
	public List<OrdemServico> listarOrdens(final String situacao) {
		List<OrdemServicoEntity> entityList = this.ordemServicoRepository.FindAllOrdensByStatus(situacao);
		List<OrdemServico> ordemList = new ArrayList<OrdemServico>();
		entityList.forEach(a-> {
			ordemList.add(converterEntityParaOrdemServico(a));
		});
		return ordemList;
	}

	public Map<String,DashBoardDto> contarOrdensPorSituacao() {
		final List<OrdemServicoEntity> lst = this.ordemServicoRepository.FindAllOrdens();
		return calcularTotais(lst);
	}

	private Map<String,DashBoardDto> calcularTotais(List<OrdemServicoEntity> lst) {
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
		for (OrdemServicoEntity a : lst) {
			for (OrdemServicoLancEntity lanc : a.getOrdemServicoLancs()) {
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
				if (lanc.getSituacao().equals(StatusServicoEnum.FECHADO.getValue()) && lanc.getAtualSituacao()) {
					qtdFechado = qtdFechado + 1;
					totalFechado = totalFechado.add(calcularTotalPecaServicos(a));
				}
				if (lanc.getSituacao().equals(StatusServicoEnum.FINALIZADO.getValue()) && lanc.getAtualSituacao()) {
					qtdFinalizado = qtdFinalizado + 1; 
					totalFinalizado = totalFinalizado.add(calcularTotalPecaServicos(a));
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
	
	private BigDecimal calcularTotalPecaServicos(OrdemServicoEntity ordem) {
		BigDecimal totalPecaServico = new BigDecimal(0);
		for (PecaServicoOrdemEntity lanc : ordem.getPecaServicoOrdems()) {
			BigDecimal resultado = totalPecaServico.add(lanc.getValor());
			totalPecaServico = resultado;
		}
		return totalPecaServico;
	}
	
	private OrdemServicoEntity converterParaEntity(final OrdemServico ordemServico) {
		final OrdemServicoEntity ent = new OrdemServicoEntity();
		ent.setId(ordemServico.getId());
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
		ordem.setId(entity.getId());
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
		ordem.setOrdemServicoLanc(convertLancEntityParaOrdemServicoLanc(entity, ordem));
		ordem.setPecaOutroServico(converterPecaServicoEntityParaPecaOutroServico(entity, ordem));
		return ordem;
	}

	private List<PecaOutroServico> converterPecaServicoEntityParaPecaOutroServico(final OrdemServicoEntity entity, final OrdemServico ordemServico) {
		List<PecaOutroServico> lst = new ArrayList<PecaOutroServico>();
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

	private List<OrdemServicoLanc> convertLancEntityParaOrdemServicoLanc(final OrdemServicoEntity entity, final OrdemServico ordemServico) {
		List<OrdemServicoLanc> lst = new ArrayList<OrdemServicoLanc>();
		entity.getOrdemServicoLancs()
		        
			.forEach(a ->
			{
				OrdemServicoLanc lanc = new OrdemServicoLanc();
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
