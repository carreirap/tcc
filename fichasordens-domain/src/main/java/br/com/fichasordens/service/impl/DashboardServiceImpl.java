package br.com.fichasordens.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServicoServiceImpl;
import br.com.fichasordens.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	private FichaAtendimento fichaAtendimento;
	
	@Autowired
	private OrdemServicoServiceImpl ordemService;

	@Override
	public Map<String,Integer> contarFichasAtendimento() {
		return this.fichaAtendimento.contarFichasPorSituacao();
		
	}

	@Override
	public Map<String, Integer> contarOrdensDeServicos() {
		return this.ordemService.contarOrdensPorSituacao();
	}

}
