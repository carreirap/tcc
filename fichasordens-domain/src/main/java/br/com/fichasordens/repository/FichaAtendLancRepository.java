package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fichasordens.entities.FichaAtendLancEntity;
import br.com.fichasordens.entities.FichaAtendLancId;

@Repository
public interface FichaAtendLancRepository extends JpaRepository<FichaAtendLancEntity, FichaAtendLancId> {
	
	FichaAtendLancEntity findBySituacaoAndFichaAtendimentoIdAndAtualSituacao(final String situacao, final long idFicha, final boolean atualSituacao);
	
	FichaAtendLancEntity findByFichaAtendimentoIdAndAtualSituacao(final long id, final boolean atualSitucao);

}
