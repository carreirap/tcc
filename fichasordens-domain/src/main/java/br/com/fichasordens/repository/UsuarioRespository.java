package br.com.fichasordens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.Usuario;

@Repository
@Transactional
public interface UsuarioRespository extends JpaRepository<Usuario, String> {
	
}
