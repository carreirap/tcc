package br.com.fichasordens;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.entities.AtendimentoFichaEntity;
import br.com.fichasordens.repository.AtendimentoFichaRepository;

public class AtendimentoTest {
	
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private Atendimento atendimento;
	
	@Mock 
	private AtendimentoFichaRepository atendimentoRepository;
	
	@Mock 
	private Parametro parametro;
	
	@Test
	public void test_gravarAtendimento_success() {
		Atendimento atend = this.createAtendimento();
		when(this.atendimentoRepository.save(org.mockito.Mockito.any(AtendimentoFichaEntity.class))).thenReturn(new AtendimentoFichaEntity());
		this.atendimento.gravarAtendimento(atend);
		
		Mockito.verify(this.atendimentoRepository, Mockito.times(1)).save(org.mockito.Mockito.any(AtendimentoFichaEntity.class));
		
	}
	
	@Test
	public void excluirAtendimento_success() {
		
		doNothing().when(atendimentoRepository).delete(org.mockito.Mockito.any(AtendimentoFichaEntity.class));
		this.atendimento.excluirAtendimento(199, 0);
		
		Mockito.verify(this.atendimentoRepository, Mockito.times(1)).delete(org.mockito.Mockito.any(AtendimentoFichaEntity.class));
	}
	
	@Test
	public void test_calcularParametros_success() {
		when(this.parametro.recuperarParametros()).thenReturn(this.loadParametros());
		
		BigDecimal r = this.atendimento.calcularValorAtendimento(2, 0);
		assertEquals(20, r.intValue());
		
	}
	
	private Atendimento createAtendimento() {
		Atendimento atend = new Atendimento();
		atend.setData(new Date());
		atend.setDescricao("Test");
		atend.setDuracao(new BigDecimal(10));
		atend.setFichaAtendimento(FichaAtendimentoTest.createFichaAtendimento());
		atend.setSequencia(1);
		atend.setValor(new BigDecimal(10));
		return atend;
	}
	
	private List<Parametro> loadParametros() {
		Parametro p = new Parametro();
		p.setId((byte)1);
		p.setValor(new BigDecimal(10));
		List<Parametro> list = Arrays.asList(p);
		return list;
	}

}
