package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.FichaAtendimentoEntity;

@Repository
@Transactional
public interface FichaAtendimentoRepository extends JpaRepository<FichaAtendimentoEntity, Long> {

}
