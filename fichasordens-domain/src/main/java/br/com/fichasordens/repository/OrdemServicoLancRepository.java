package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fichasordens.entities.OrdemServicoLancEntity;
import br.com.fichasordens.entities.OrdemServicoLancId;

@Repository
public interface OrdemServicoLancRepository extends JpaRepository<OrdemServicoLancEntity, OrdemServicoLancId> {

}
