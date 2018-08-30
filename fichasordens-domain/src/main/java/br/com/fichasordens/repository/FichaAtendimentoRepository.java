package br.com.fichasordens.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Query("SELECT a FROM FichaAtendimentoEntity a LEFT JOIN a.fichaAtendLancs b where b.atualSituacao = true and b.situacao = ?1")
	public Page<FichaAtendimentoEntity> FindAllFichaByStatus(final String situacao, Pageable page);
	
	@Query("SELECT a FROM FichaAtendimentoEntity a LEFT JOIN a.cliente b where b.cnpjCpf = ?1 and b.id = a.cliente.id")
	public Page<FichaAtendimentoEntity> FindAllFichaByCnpfcpf(final String cnpjCpf, Pageable page);
	
	@Query("SELECT a FROM FichaAtendimentoEntity a LEFT JOIN a.cliente b LEFT join a.fichaAtendLancs c where b.id = a.cliente.id and b.cnpjCpf = ?1 and c.situacao = ?2 and c.atualSituacao = true")
	public Page<FichaAtendimentoEntity> FindAllFichaByCnpfcpfAndSituacao(final String cnpjCpf, final String situacao, Pageable page);
	
	@Query("SELECT a FROM FichaAtendimentoEntity a LEFT JOIN a.fichaAtendLancs b where b.atualSituacao = true and b.situacao = ?1 and b.data between ?2 and ?3")
	public List<FichaAtendimentoEntity> FindAllFichaByStatusAndDataInicioAndDataFim(final String situacao, final Date inicio, final Date fim);

}

 