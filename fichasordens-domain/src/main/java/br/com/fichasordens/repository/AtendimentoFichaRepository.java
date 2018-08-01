package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.AtendimentoFichaEntity;
import br.com.fichasordens.entities.AtendimentoFichaId;

@Repository
@Transactional
public interface AtendimentoFichaRepository extends JpaRepository<AtendimentoFichaEntity, AtendimentoFichaId> {

}
