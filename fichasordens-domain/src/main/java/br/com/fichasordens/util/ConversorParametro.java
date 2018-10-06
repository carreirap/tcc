package br.com.fichasordens.util;

import br.com.fichasordens.Parametro;
import br.com.fichasordens.entities.ParametroEntity;

public class ConversorParametro {
	
	private ConversorParametro() {}
	
	public static Parametro converterEntity( ParametroEntity p) {
		Parametro param = new Parametro();
		param.setDescricao(p.getDescricao());
		param.setId(p.getId());
		param.setValor(p.getValor());
		return param;
	}

}
