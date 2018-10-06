package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fichasordens.entities.ParametroEntity;

public interface ParametroRepository extends JpaRepository<ParametroEntity, Byte> {
	
	static byte ID_DIAS_VALOR = 3;

}
