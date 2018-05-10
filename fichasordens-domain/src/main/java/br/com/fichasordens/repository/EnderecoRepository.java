package br.com.fichasordens.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.EnderecoEntity;

@Repository
@Transactional
public interface EnderecoRepository extends JpaRepository<EnderecoEntity, BigDecimal> {

}
