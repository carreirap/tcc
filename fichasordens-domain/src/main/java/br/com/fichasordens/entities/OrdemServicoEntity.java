package br.com.fichasordens.entities;
// Generated 03/03/2018 14:18:32 by Hibernate Tools 4.3.5.Final

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

/**
 * OrdemServico generated by hbm2java
 */
@Entity
@Table(name = "ordem_servico", schema = "public")
public class OrdemServicoEntity implements java.io.Serializable {

	private long id;
	private ClienteEntity cliente;
	private String tipoServico;
	private String frabricante;
	private String modelo;
	private String serie;
	private String descEquip;
	private String descDefeito;
	private String estadoItensAcomp;;
	private Set<OrdemServicoLancEntity> ordemServicoLancs = new HashSet<OrdemServicoLancEntity>(0);
	private Set<PecaServicoOrdemEntity> pecaServicoOrdems = new HashSet<PecaServicoOrdemEntity>(0);

	public OrdemServicoEntity() {
	}

	public OrdemServicoEntity(long id, String tipoServico) {
		this.id = id;
		this.tipoServico = tipoServico;
	}

	public OrdemServicoEntity(long id, ClienteEntity cliente, String tipoServico, String frabricante, String modelo, String serie,
			String descEquip, String descDefeito, String estadoItensAcomp) {
		this.id = id;
		this.cliente = cliente;
		this.tipoServico = tipoServico;
		this.frabricante = frabricante;
		this.modelo = modelo;
		this.serie = serie;
		this.descEquip = descEquip;
		this.descDefeito = descDefeito;
		this.estadoItensAcomp = estadoItensAcomp;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ordem_sequencia")
	@SequenceGenerator(name="ordem_sequencia", sequenceName="ordem_sequencia", allocationSize=1)
	@Column(name = "id", unique = true, nullable = false, precision = 10, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", nullable = false)
	public ClienteEntity getCliente() {
		return this.cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}

	@Column(name = "tipo_servico", nullable = false, length = 20)
	public String getTipoServico() {
		return this.tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	@Column(name = "frabricante", length = 40)
	public String getFrabricante() {
		return this.frabricante;
	}

	public void setFrabricante(String frabricante) {
		this.frabricante = frabricante;
	}

	@Column(name = "modelo", length = 40)
	public String getModelo() {
		return this.modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@Column(name = "serie", length = 50)
	public String getSerie() {
		return this.serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	@Column(name = "desc_equip", nullable = false)
	public String getDescEquip() {
		return this.descEquip;
	}

	public void setDescEquip(String descEquip) {
		this.descEquip = descEquip;
	}

	@Column(name = "desc_defeito", nullable = false)
	public String getDescDefeito() {
		return this.descDefeito;
	}

	public void setDescDefeito(String descDefeito) {
		this.descDefeito = descDefeito;
	}

	@Column(name = "estado_itens_acomp", nullable = false)
	public String getEstadoItensAcomp() {
		return this.estadoItensAcomp;
	}

	public void setEstadoItensAcomp(String estadoItensAcomp) {
		this.estadoItensAcomp = estadoItensAcomp;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ordemServico")
	@OrderBy(clause="sequencia asc")
	public Set<OrdemServicoLancEntity> getOrdemServicoLancs() {
		return ordemServicoLancs;
	}

	public void setOrdemServicoLancs(Set<OrdemServicoLancEntity> ordemServicoLancs) {
		this.ordemServicoLancs = ordemServicoLancs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ordemServico")
	@OrderBy(clause="sequencia asc")
	public Set<PecaServicoOrdemEntity> getPecaServicoOrdems() {
		return pecaServicoOrdems;
	}

	public void setPecaServicoOrdems(Set<PecaServicoOrdemEntity> pecaServicoOrdems) {
		this.pecaServicoOrdems = pecaServicoOrdems;
	}
	

}
