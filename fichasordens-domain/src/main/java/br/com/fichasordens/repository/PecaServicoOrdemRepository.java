package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntityId;

@Repository
public interface PecaServicoOrdemRepository extends JpaRepository<PecaServicoOrdemEntity, PecaServicoOrdemEntityId> {

}
