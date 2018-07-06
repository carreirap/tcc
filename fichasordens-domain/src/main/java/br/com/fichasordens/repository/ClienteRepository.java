package br.com.fichasordens.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.ClienteEntity;

@Repository
@Transactional
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{
	
	ClienteEntity findByCnpjCpf(final String cnpjCpf);
	
//	public static Specification<ClienteEntity> configurarQueryPorCnpjCpf(String cnpjCpf) {
//		return new Specification<ClienteEntity>() {
//			@Override
//			public Predicate toPredicate(Root<ClienteEntity> root, CriteriaQuery<?> arg1, CriteriaBuilder cb) {
//				if(cnpjCpf != null && !cnpjCpf.equals("")) {
//					return cb.equal(root.get("cnpjCpf"), cnpjCpf);
//				}
//				return null;
//			}
//			
//		};
//	}
//	
//	public static Specification<ClienteEntity> configurarQueryPorNome(String nome) {
//		return new Specification<ClienteEntity>() {
//			@Override
//			public Predicate toPredicate(Root<ClienteEntity> root, CriteriaQuery<?> arg1, CriteriaBuilder cb) {
//				if(nome != null && !nome.equals("")) {
//					return cb.equal(root.get("nome"), nome);
//				}
//				return null;
//			}
//			
//		};
//	}
	
	public static Pageable createPageRequest(final int page) { 
	    return new PageRequest(page, 10, Sort.Direction.ASC, "nome");
	}

}
