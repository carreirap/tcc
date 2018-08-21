package br.com.fichasordens.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.service.DashboardService;
import br.com.fichasordens.util.DashBoardDto;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	private FichaAtendimento fichaAtendimento;
	
	@Autowired
	private OrdemServico ordemServico;

	@Override
	public Map<String,DashBoardDto> contarFichasAtendimento() {
		return this.fichaAtendimento.contarFichasPorSituacao();
		
	}

	@Override
	public Map<String,DashBoardDto> contarOrdensDeServicos() {
		return this.ordemServico.contarOrdensPorSituacao();
	}

}
