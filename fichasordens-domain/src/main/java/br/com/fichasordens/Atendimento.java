package br.com.fichasordens;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.AtendimentoFichaEntity;
import br.com.fichasordens.entities.AtendimentoFichaId;
import br.com.fichasordens.repository.AtendimentoFichaRepository;
import br.com.fichasordens.util.ConversorFichaAtendimento;

@Component
public class Atendimento {
	
	private int sequencia;
	private Date data;
	private String descricao;
	private BigDecimal valor;
	private BigDecimal duracao;
	private FichaAtendimento fichaAtendimento;
	
	@Autowired
	private AtendimentoFichaRepository atendimentoRepository;
	
	@Autowired
	private Parametro parametro;
	
	@Transactional
	public void gravarAtendimento(final Atendimento atendimento) {
		final AtendimentoFichaEntity ent = ConversorFichaAtendimento.converterAtendimentoParaEntity(atendimento);
		atendimentoRepository.save(ent);
	}
	
	@Transactional
	public void excluirAtendimento(final long idFicha, final int sequencia) {
		AtendimentoFichaEntity ent = new AtendimentoFichaEntity();
		ent.setId(new AtendimentoFichaId());
		ent.getId().setSequencia(sequencia);
		ent.getId().setFichaAtendimentoId(idFicha);
		this.atendimentoRepository.delete(ent);
	}
	
	public BigDecimal calcularValorAtendimento(final int horas, final int tipo) {
		Parametro param = this.parametro.recuperarParametros().get(tipo);
		return calcular(horas, param);
	}

	private BigDecimal calcular(final int horas, final Parametro param) {
		return new BigDecimal(horas).multiply(param.getValor());
	}
	
	public int getSequencia() {
		return sequencia;
	}
	public void setSequencia(int sequencia) {
		this.sequencia = sequencia;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public BigDecimal getDuracao() {
		return duracao;
	}
	public void setDuracao(BigDecimal duracao) {
		this.duracao = duracao;
	}
	public FichaAtendimento getFichaAtendimento() {
		return fichaAtendimento;
	}
	public void setFichaAtendimento(FichaAtendimento fichaAtendimento) {
		this.fichaAtendimento = fichaAtendimento;
	}
	
}