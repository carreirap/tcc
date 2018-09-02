package br.com.fichasordens.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.OrdemServicoEntity;

@Repository
@Transactional
public interface OrdemServicoRepository extends JpaRepository<OrdemServicoEntity, Long> {

	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.ordemServicoLancs b where b.atualSituacao = true")
	public List<OrdemServicoEntity> findAllOrdens();
	
	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.ordemServicoLancs b where b.atualSituacao = true")
	public Page<OrdemServicoEntity> findAllOrdens(Pageable page);
	
	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.ordemServicoLancs b where b.atualSituacao = true and b.situacao = ?1")
	public List<OrdemServicoEntity> findAllOrdensByStatus(final String situacao);
	
	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.ordemServicoLancs b where b.atualSituacao = true and b.situacao = ?1")
	public Page<OrdemServicoEntity> findAllOrdensByStatus(final String situacao, Pageable page);
	
	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.cliente b where b.cnpjCpf = ?1 and b.id = a.cliente.id")
	public Page<OrdemServicoEntity> findAllOrdensByCnpfcpf(final String cnpjCpf, Pageable page);
	
	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.cliente b LEFT join a.ordemServicoLancs c where b.id = a.cliente.id and b.cnpjCpf = ?1 and c.situacao = ?2 and c.atualSituacao = true")
	public Page<OrdemServicoEntity> findAllOrdensByCnpfcpfAndSituacao(final String cnpjCpf, final String situacao, Pageable page);
	
	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.ordemServicoLancs b where b.atualSituacao = true and b.situacao = ?1 and b.data between ?2 and ?3")
	public List<OrdemServicoEntity> findAllOrdensByStatusAndDataBetween(final String situacao, final Date inicio, final Date fim);
}
