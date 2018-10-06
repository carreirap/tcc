package br.com.fichasordens.util;

import br.com.fichasordens.exception.ExcecaoRetorno;

public class ValidadorSituacao {
	
	private static String sitsAberto = "Aberto,Cancelado,Trabalhando";
	private static String sitTrab = "Trabalhando,Aguardando,Finalizado,Cancelado";
	private static String sitAguard = "Aguardando,Trabalhando";
	private static String sitFinalizado = "Finalizado,Faturado";
	private static String sitFaturado = "Faturado,Fechado";
	private static String message = "Não é possivel alterar a situaçao para o valor selecionado";
	
	private ValidadorSituacao() {}
	
	public static void validarTrocaSituacao(final String situacaoAtual, final String novaSituacao) throws ExcecaoRetorno {
		
		if (situacaoAtual.equals("Aberto") && !sitsAberto.contains(novaSituacao)) {
			throw new ExcecaoRetorno(message);
		}
	
		if (situacaoAtual.equals("Trabalhando") && !sitTrab.contains(novaSituacao)) {
			throw new ExcecaoRetorno(message);
		}
		 
		if (situacaoAtual.equals("Aguardando") && !sitAguard.contains(novaSituacao)) {
			throw new ExcecaoRetorno(message);
		}
		                          
		if (situacaoAtual.equals("Finalizado") && !sitFinalizado.contains(novaSituacao)) {
			throw new ExcecaoRetorno(message);
		}
		
		if (situacaoAtual.equals("Faturado") && !sitFaturado.contains(novaSituacao)) {
			throw new ExcecaoRetorno(message);
		}
		
	}
}
