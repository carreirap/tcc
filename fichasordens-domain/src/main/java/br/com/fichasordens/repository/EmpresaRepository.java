package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fichasordens.entities.EmpresaEntity;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long>{

}
