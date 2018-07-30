package br.com.fichasordens.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.FichaAtendimentoEntity;

@Repository
@Transactional
public interface FichaAtendimentoRepository extends JpaRepository<FichaAtendimentoEntity, Long> {
	
	@Query("SELECT a FROM FichaAtendimentoEntity a LEFT JOIN a.fichaAtendLancs b where b.atualSituacao = true")
	public List<FichaAtendimentoEntity> FindAllFichas();
	
	@Query("SELECT a FROM FichaAtendimentoEntity a LEFT JOIN a.fichaAtendLancs b where b.atualSituacao = true and b.situacao = ?1")
	public List<FichaAtendimentoEntity> FindAllFichaByStatus(final String situacao);

}

 