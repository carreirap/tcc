package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.ClienteEntity;

@Repository
@Transactional
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{
	
	ClienteEntity findByCnpjCpf(final String cnpjCpf);

}
