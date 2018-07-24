package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoFichaIdEntity;

@Repository
@Transactional
public interface PecaServicoFichaRepository extends JpaRepository<PecaServicoFichaEntity, PecaServicoFichaIdEntity> {

}
