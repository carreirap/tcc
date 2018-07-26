package br.com.fichasordens.service;

import java.util.Map;

public interface DashboardService {
	
	Map<String,Integer> contarFichasAtendimento();
	
	Map<String,Integer> contarOrdensDeServicos();

}
