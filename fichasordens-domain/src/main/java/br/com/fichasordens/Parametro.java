package br.com.fichasordens;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.ParametroEntity;
import br.com.fichasordens.repository.ParametroRepository;
import br.com.fichasordens.util.ConversorParametro;

@Component
public class Parametro {
	
	private byte id;
	private String descricao;
	private BigDecimal valor;
	
	@Autowired
	private ParametroRepository parametroRepository;
	
	@Transactional
	public List<Parametro> recuperarParametros() {
		List<ParametroEntity> list = parametroRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
		List<Parametro> lstParametro = new ArrayList<>();
		list.forEach(p-> lstParametro.add(ConversorParametro.converterEntity(p)) );
		
		return lstParametro;
		
	}
	
	public Parametro buscarValorParametroAlerta() {
		ParametroEntity ent = this.parametroRepository.findOne(ParametroRepository.ID_DIAS_VALOR);
		return ConversorParametro.converterEntity(ent);
	}
	
	public void salvarParametro(List<Parametro> parametrosList) {
		for (Parametro p : parametrosList) {
			ParametroEntity ent = this.parametroRepository.findOne(p.getId());
			ent.setValor(p.getValor());
			this.parametroRepository.save(ent);
		}
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
