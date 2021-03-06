package br.com.fichasordens.entities;
// Generated 03/03/2018 14:18:32 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AtendimentoFicha generated by hbm2java
 */
@Entity
@Table(name = "atendimento_ficha", schema = "public")
public class AtendimentoFichaEntity implements java.io.Serializable {

	private AtendimentoFichaId id;
	private FichaAtendimentoEntity fichaAtendimento;
	private Date date;
	private String descricao;
	private BigDecimal valor;
	private BigDecimal duracao;

	public AtendimentoFichaEntity() {
	}

	public AtendimentoFichaEntity(AtendimentoFichaId id, FichaAtendimentoEntity fichaAtendimento, Date date, String descricao,
			BigDecimal valor, BigDecimal duracao) {
		this.id = id;
		this.fichaAtendimento = fichaAtendimento;
		this.date = date;
		this.descricao = descricao;
		this.valor = valor;
		this.duracao = duracao;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "sequencia", column = @Column(name = "sequencia", nullable = false)),
			@AttributeOverride(name = "fichaAtendimentoId", column = @Column(name = "ficha_atendimento_id", nullable = false, precision = 10, scale = 0)) })
	public AtendimentoFichaId getId() {
		return this.id;
	}

	public void setId(AtendimentoFichaId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ficha_atendimento_id", nullable = false, insertable = false, updatable = false)
	public FichaAtendimentoEntity getFichaAtendimento() {
		return this.fichaAtendimento;
	}

	public void setFichaAtendimento(FichaAtendimentoEntity fichaAtendimento) {
		this.fichaAtendimento = fichaAtendimento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date", nullable = false, length = 13)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "descricao", nullable = false, length = 150)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "valor", nullable = false, precision = 8)
	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "duracao", nullable = false, precision = 5)
	public BigDecimal getDuracao() {
		return this.duracao;
	}

	public void setDuracao(BigDecimal duracao) {
		this.duracao = duracao;
	}

}
