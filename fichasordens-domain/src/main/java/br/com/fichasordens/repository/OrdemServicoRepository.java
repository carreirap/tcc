package br.com.fichasordens.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.OrdemServicoEntity;

@Repository
@Transactional
public interface OrdemServicoRepository extends JpaRepository<OrdemServicoEntity, Long> {

	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.ordemServicoLancs b where b.atualSituacao = true")
	public List<OrdemServicoEntity> FindAllOrdens();
	
	@Query("SELECT a FROM OrdemServicoEntity a LEFT JOIN a.ordemServicoLancs b where b.atualSituacao = true and b.situacao = ?1")
	public List<OrdemServicoEntity> FindAllOrdensByStatus(final String situacao);
}
