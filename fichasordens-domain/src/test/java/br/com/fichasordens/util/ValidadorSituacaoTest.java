package br.com.fichasordens.util;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.exception.ExcecaoRetorno;

public class ValidadorSituacaoTest {
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void test_validarsiuacao() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Aberto", "Trabalhando");
	}
	
	@Test
	public void test_validarsiuacaoAbertoAberto() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Aberto", "Aberto");
	}

	@Test
	public void test_validarsiuacaoAbertoCancelado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Aberto", "Cancelado");
	}
	
	@Test(expected=ExcecaoRetorno.class)
	public void test_validarsiuacaoAbertoFechado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Aberto", "Fechado");
	}
	
	@Test
	public void test_validarsiuacaoTrabalhandoCancelado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Trabalhando", "Cancelado");
	}
	
	@Test
	public void test_validarsiuacaoTrabalhandoAguardando() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Trabalhando", "Aguardando");
	}
	
	@Test
	public void test_validarsiuacaoTrabalhandoFinalizado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Trabalhando", "Finalizado");
	}
	
	@Test
	public void test_validarsiuacaoTrabalhandoTrabalhando() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Trabalhando", "Trabalhando");
	}
	
	@Test(expected=ExcecaoRetorno.class)
	public void test_validarsiuacaoTrabalhandoFechado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Trabalhando", "Fechado");
	}
	
	@Test
	public void test_validarsiuacaoAguardandoAguardando() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Aguardando", "Aguardando");
	}
	
	@Test(expected=ExcecaoRetorno.class)
	public void test_validarsiuacaoAguardandoFechado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Aguardando", "Fechado");
	}
	
	@Test
	public void test_validarsiuacaoAguardandoTrabalhando() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Aguardando", "Trabalhando");
	}
	
	@Test
	public void test_validarsiuacaoFinalizadoFinalizado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Finalizado", "Finalizado");
	}
	
	@Test
	public void test_validarsiuacaoFinalizadoFaturado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Finalizado", "Faturado");
	}
	
	@Test
	public void test_validarsiuacaoFaturadoFaturado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Faturado", "Faturado");
	}
	
	@Test
	public void test_validarsiuacaoFaturadoFechado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Faturado", "Fechado");
	}
	
	@Test(expected=ExcecaoRetorno.class)
	public void test_validarsiuacaoFechado() throws ExcecaoRetorno {
		ValidadorSituacao.validarTrocaSituacao("Faturado", "Aberto");
	}
	
}
