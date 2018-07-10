package br.com.fichasordens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.OrdemServicoRepository;

@Service
public class OrdemServicoServiceImpl implements OrdemServicoInterface {
	
	@Autowired
	OrdemServicoRepository ordemServicoRepository;

	@Override
	public OrdemServico gravarOrdem(final OrdemServico ordemServico) throws ExcecaoRetorno {
		OrdemServicoEntity ent = this.converterParaEntity(ordemServico);
		try {
			ent = this.ordemServicoRepository.save(ent);
			ordemServico.setId(ent.getId());
		} catch (Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar ordem de serviço");
		}
		
		return ordemServico;
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
	
}
