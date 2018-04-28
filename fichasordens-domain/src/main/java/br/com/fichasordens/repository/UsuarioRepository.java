package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.UsuarioEntity;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
	
	UsuarioEntity findByUsuario(final String usuario);
	
}
