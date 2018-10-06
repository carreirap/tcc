package br.com.fichasordens;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoFichaIdEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntityId;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.PecaServicoFichaRepository;
import br.com.fichasordens.repository.PecaServicoOrdemRepository;
import br.com.fichasordens.util.ConversorFichaAtendimento;
import br.com.fichasordens.util.ConversorOrdemServico;

@Component
public class PecaOutroServico {
	private long id;
	private String descricao;
	private long quantidade;
	private BigDecimal valor;
	private OrdemServico ordemServico;
	private FichaAtendimento fichaAtendimento;
	
	@Autowired
	private PecaServicoFichaRepository pecaServicoFichaRepository;
	
	@Autowired
	private PecaServicoOrdemRepository pecaServicoOrdemRepository;
	
	@Transactional
	public void gravarPecaServicoFicha(final PecaOutroServico pecaOutroServico) {
		final PecaServicoFichaEntity ent = ConversorFichaAtendimento.converterPecaServicoFichaParaEntity(pecaOutroServico);
		pecaServicoFichaRepository.save(ent);
	}
	
	@Transactional
	public void excluirPecaOutroServicoFicha(final long idFicha, final int sequencia) {
		PecaServicoFichaEntity ent = new PecaServicoFichaEntity();
		ent.setId(new PecaServicoFichaIdEntity());
		ent.getId().setSequencia(sequencia);
		ent.getId().setFichaAtendId(idFicha);
		this.pecaServicoFichaRepository.delete(ent);
	}
	
	public void gravarPecaServicoOrdem(PecaOutroServico pecaServicoOrdem) throws ExcecaoRetorno {
		PecaServicoOrdemEntity entity = ConversorOrdemServico.converterPecaServicoOrdemParaEntity(pecaServicoOrdem);
		try {
			this.pecaServicoOrdemRepository.save(entity);
		} catch (Exception e) {
			throw new ExcecaoRetorno("Erro ao tentar cadastrar peças/outro serviços");
		}
	}
	
	@Transactional
	public void excluirPecaOutroServicoOrdem(final int id, final int sequencia) {
		final PecaServicoOrdemEntity ent = new PecaServicoOrdemEntity();
		ent.setId(new PecaServicoOrdemEntityId());
		ent.getId().setSequencia(sequencia);
		ent.getId().setOrdemServicoId(id);
		this.pecaServicoOrdemRepository.delete(ent);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public OrdemServico getOrdemServico() {
		return ordemServico;
	}
	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}
	public FichaAtendimento getFichaAtendimento() {
		return fichaAtendimento;
	}
	public void setFichaAtendimento(FichaAtendimento fichaAtendimento) {
		this.fichaAtendimento = fichaAtendimento;
	}
}
