package br.com.fichasordens.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.FichaAtendimentoTest;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.OrdemServicoTest;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.util.DashBoardDto;
import br.com.fichasordens.util.StatusServicoEnum;

public class DashboardServiceImplTest {
	
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	DashboardServiceImpl dash;
	
	@Mock
	private FichaAtendimento fichaAtendimento;
	
	@Mock
	private OrdemServico ordemServico;
	
	@Before
	public void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = DashboardServiceImpl.class.getDeclaredField( "qtdDiasAlerta" );
		field.setAccessible(true);
		field.set(dash, "2");
	}
	
	@Test
	public void test_contarFichasAtendimento_success() {
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.ABERTO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.AGUARDANDO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.TRABALHANDO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FATURADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.CANCELADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FINALIZADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FECHADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		
		Map<String,DashBoardDto> map = this.dash.contarFichasAtendimento();
		
		assertEquals(7, map.size());
		assertEquals('N', map.get(StatusServicoEnum.ABERTO.getValue()).getAlerta());
		
	}
	
	@Test
	public void test_contarFichasAtendimentoComAlerta_success() {
		FichaAtendimentoEntity aberto = FichaAtendimentoTest.createFichaAtendimentoEntity();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, -3);
		
		aberto.getFichaAtendLancs().forEach(a -> a.setData(c.getTime()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.ABERTO)).thenReturn(Arrays.asList(aberto));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.AGUARDANDO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.TRABALHANDO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FATURADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.CANCELADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FINALIZADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FECHADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		
		Map<String,DashBoardDto> map = this.dash.contarFichasAtendimento();
		
		assertEquals(7, map.size());
		assertEquals(1, map.get(StatusServicoEnum.ABERTO.getValue()).getQtd());
		assertEquals('S', map.get(StatusServicoEnum.ABERTO.getValue()).getAlerta());
		assertEquals(1, map.get(StatusServicoEnum.AGUARDANDO.getValue()).getQtd());
		
	}
	
	@Test
	public void test_contarFichasAtendimentoAtualSituacaoFalse_Fail() {
		FichaAtendimentoEntity aberto = FichaAtendimentoTest.createFichaAtendimentoEntity();
		
		
		aberto.getFichaAtendLancs().forEach(a -> a.setAtualSituacao(false));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.ABERTO)).thenReturn(Arrays.asList(aberto));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.AGUARDANDO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.TRABALHANDO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FATURADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.CANCELADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FINALIZADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		when(this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.FECHADO)).thenReturn(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()));
		
		Map<String,DashBoardDto> map = this.dash.contarFichasAtendimento();
		
		assertEquals(7, map.size());
		assertEquals(0, map.get(StatusServicoEnum.ABERTO.getValue()).getQtd());
		
	}
	
	@Test
	public void test_contarOrdemServicoAtualSituacaoFalse_success() {
		OrdemServicoEntity ent = OrdemServicoTest.createOrdemServicoEntity();
		ent.getOrdemServicoLancs().forEach(a -> a.setAtualSituacao(false));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.ABERTO)).thenReturn(Arrays.asList(ent));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.AGUARDANDO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.TRABALHANDO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FATURADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.CANCELADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FINALIZADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FECHADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		
		Map<String,DashBoardDto> map = this.dash.contarOrdensDeServicos();
		
		assertEquals(7, map.size());
		assertEquals(0, map.get(StatusServicoEnum.ABERTO.getValue()).getQtd());
		
	}
	
	@Test
	public void test_contarOrdemServicoAlertaAndTotal_success() {
		OrdemServicoEntity ent = OrdemServicoTest.createOrdemServicoEntity();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, -3);
		ent.getOrdemServicoLancs().forEach(a -> a.setData(c.getTime()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.ABERTO)).thenReturn(Arrays.asList(ent));
		
		OrdemServicoEntity ent1 = OrdemServicoTest.createOrdemServicoEntity();
		ent1.getPecaServicoOrdems().forEach(a-> a.setValor(new BigDecimal(225.60)));
		
		OrdemServicoEntity ent2 = OrdemServicoTest.createOrdemServicoEntity();
		ent2.getPecaServicoOrdems().forEach(a-> a.setValor(new BigDecimal(5.00)));
		
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.AGUARDANDO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.TRABALHANDO)).thenReturn(Arrays.asList(ent1, ent2));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FATURADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.CANCELADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FINALIZADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FECHADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		
		Map<String,DashBoardDto> map = this.dash.contarOrdensDeServicos();
		
		assertEquals(7, map.size());
		assertEquals(1, map.get(StatusServicoEnum.ABERTO.getValue()).getQtd());
		assertEquals('S', map.get(StatusServicoEnum.ABERTO.getValue()).getAlerta());
		assertEquals(1, map.get(StatusServicoEnum.AGUARDANDO.getValue()).getQtd());
		assertEquals(new BigDecimal(230.6), map.get(StatusServicoEnum.TRABALHANDO.getValue()).getValor());
		
	}
	
	@Test
	public void test_contarOrdemServico_success() {
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.ABERTO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.AGUARDANDO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.TRABALHANDO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FATURADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.CANCELADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FINALIZADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		when(this.ordemServico.buscarOrdensDeServicoPorSituacao(StatusServicoEnum.FECHADO)).thenReturn(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()));
		
		Map<String,DashBoardDto> map = this.dash.contarOrdensDeServicos();
		
		assertEquals(7, map.size());
		assertEquals('N', map.get(StatusServicoEnum.ABERTO.getValue()).getAlerta());
		
	}

}
