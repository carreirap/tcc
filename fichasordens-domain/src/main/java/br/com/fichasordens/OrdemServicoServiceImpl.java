package br.com.fichasordens;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
public class OrdemServicoServiceImpl implements OrdemServicoInterface {
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private PecaServicoOrdemRepository pecaServicoOrdemRepository;
	
	@Autowired
	private OrdemServicoLancRepository ordemServicoLancRepository;
		
	@Autowired
	private Usuario usuario;

	@Transactional
	@Override
	public OrdemServico gravarOrdem(final OrdemServico ordemServico) throws ExcecaoRetorno {
		OrdemServicoEntity ent = this.converterParaEntity(ordemServico);
		try {
			OrdemServicoLancEntity lancEntity = this.converterOrdemServicoLancParaEntity(ordemServico.getOrdemServicoLanc().get(0));
			ent = this.ordemServicoRepository.save(ent);
			lancEntity.setOrdemServico(new OrdemServicoEntity());
			lancEntity.getId().setOrdemServicoId(ent.getId());
			lancEntity.getOrdemServico().setId(ent.getId());
			ordemServico.setId(ent.getId());
			this.ordemServicoLancRepository.save(lancEntity);
		} catch (Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar ordem de serviço");
		}
		
		return ordemServico;
	}
	
	@Transactional
	@Override
	public OrdemServico buscarOrdem(final long id) {
		OrdemServicoEntity ent = this.ordemServicoRepository.findOne(id);
		ent.getOrdemServicoLancs();
		ent.getPecaServicoOrdems();
		ent.getCliente().getId();
		return this.converterEntityParaOrdemServico(ent);
	}
	
	@Override
	public PecaOutroServico gravarPecaServicoOrdem(PecaOutroServico pecaServicoOrdem) throws ExcecaoRetorno {
		PecaServicoOrdemEntity entity = converterPecaServicoOrdemParaEntity(pecaServicoOrdem);
		try {
			entity = this.pecaServicoOrdemRepository.save(entity);
		} catch (Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar ordem de serviço");
		}
		return null;
	}
	
	@Transactional
	@Override
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

	
}
