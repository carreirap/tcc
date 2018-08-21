package br.com.fichasordens.service;

import java.util.Map;

import br.com.fichasordens.util.DashBoardDto;

public interface DashboardService {
	
	Map<String,DashBoardDto> contarFichasAtendimento();
	
	Map<String,DashBoardDto> contarOrdensDeServicos();

}
