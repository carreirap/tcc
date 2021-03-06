package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.EmpresaEntity;

@Repository
@Transactional
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long>{

}
