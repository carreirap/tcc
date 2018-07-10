package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.OrdemServicoEntity;

@Repository
@Transactional
public interface OrdemServicoRepository extends JpaRepository<OrdemServicoEntity, Long> {

}
