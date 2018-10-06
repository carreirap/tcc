package br.com.fichasordens;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoFichaIdEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.PecaServicoFichaRepository;
import br.com.fichasordens.repository.PecaServicoOrdemRepository;

public class PecaOutroServicoTest {
	
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private PecaOutroServico pecaOutroServico;
	
	@Mock
	private PecaServicoFichaRepository pecaServicoFichaRepository;
	
	@Mock
	private PecaServicoOrdemRepository pecaServicoOrdemRepository;
	
	
	@Test
	public void test_gravarPecaServico_success() throws ExcecaoRetorno {
		PecaOutroServico peca = createPecaServico();
		
		when(this.pecaServicoFichaRepository.save(org.mockito.Mockito.any(PecaServicoFichaEntity.class))).thenReturn(new PecaServicoFichaEntity());
		pecaOutroServico.gravarPecaServicoFicha(peca);
		
		Mockito.verify(this.pecaServicoFichaRepository, Mockito.times(1)).save(org.mockito.Mockito.any(PecaServicoFichaEntity.class));
		
	}
	
	@Test
	public void excluirPecaOutroServico_success() {
		
		doNothing().when(pecaServicoFichaRepository).delete(org.mockito.Mockito.any(PecaServicoFichaEntity.class));
		this.pecaOutroServico.excluirPecaOutroServicoFicha(199, 0);
		
		Mockito.verify(this.pecaServicoFichaRepository, Mockito.times(1)).delete(org.mockito.Mockito.any(PecaServicoFichaEntity.class));
	}
	
	@Test
	public void teste_gravarPecaServicoOrdem_success() throws ExcecaoRetorno {
		PecaOutroServico peca = createPecaOutroServico();
		when(pecaServicoOrdemRepository.save(org.mockito.Mockito.any(PecaServicoOrdemEntity.class))).thenReturn(null);
		this.pecaOutroServico.gravarPecaServicoOrdem(peca);
		
		Mockito.verify(this.pecaServicoOrdemRepository, Mockito.times(1)).save(org.mockito.Mockito.any(PecaServicoOrdemEntity.class));
		
	}
	
	@Test(expected=ExcecaoRetorno.class)
	public void teste_gravarPecaServicoOrdem_Exception() throws ExcecaoRetorno {
		PecaOutroServico peca = createPecaOutroServico();
		when(pecaServicoOrdemRepository.save(org.mockito.Mockito.any(PecaServicoOrdemEntity.class))).thenThrow(new RuntimeException("DB exception"));
		this.pecaOutroServico.gravarPecaServicoOrdem(peca);
	}
	
	@Test
	public void teste_deletarPecaOutroServico_success() {
		doNothing().when(pecaServicoOrdemRepository).delete(org.mockito.Mockito.any(PecaServicoOrdemEntity.class));
		this.pecaOutroServico.excluirPecaOutroServicoOrdem(199, 1);
		
		Mockito.verify(this.pecaServicoOrdemRepository, Mockito.times(1)).delete(org.mockito.Mockito.any(PecaServicoOrdemEntity.class));
	}
	
	public PecaOutroServico createPecaServico() {
		final PecaOutroServico pecaServico = new PecaOutroServico();
		pecaServico.setDescricao("Test");
		pecaServico.setQuantidade(1);
		pecaServico.setValor(new BigDecimal(10));
		pecaServico.setId(1);
		
		pecaServico.setFichaAtendimento(new FichaAtendimento());
		pecaServico.getFichaAtendimento().setId(199);
		
		return pecaServico;
	}
	
	public static PecaServicoFichaEntity createPecaServicoFichaEntity() {
		PecaServicoFichaEntity pecaEnt = new PecaServicoFichaEntity();
		pecaEnt.setId(new PecaServicoFichaIdEntity());
		pecaEnt.getId().setFichaAtendId(19);
		pecaEnt.getId().setSequencia(1);
		pecaEnt.setQuantidade(10);
		pecaEnt.setValor(new BigDecimal(10));
		pecaEnt.setDescricao("Teste");
		return pecaEnt;
	}
	
	private PecaOutroServico createPecaOutroServico() {
		PecaOutroServico peca = new PecaOutroServico();
		peca.setDescricao("Teste");
		peca.setId(1);
		peca.setQuantidade(10);
		peca.setValor(new BigDecimal(10));
		peca.setOrdemServico(new OrdemServico());
		peca.getOrdemServico().setId(199);
		
		return peca;
	}

}
