package br.com.fichasordens.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.fichasordens.FichaAtendimentoTest;
import br.com.fichasordens.OrdemServicoTest;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.repository.FichaAtendimentoRepository;
import br.com.fichasordens.repository.OrdemServicoRepository;
import br.com.fichasordens.util.ResultadoPesquisaDto;

public class HistoricoServiceImplTest {
	
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private HistoricoServiceImpl historicoServiceImpl;
	
	@Mock
	private FichaAtendimentoRepository repository;
	@Mock
	private OrdemServicoRepository ordemRepository;
	
	Pageable pageable = new PageRequest(0, 10);
	
	private Date inicio = new Date();
	private Date fim = new Date();
	
	@Before
	public void init() {
		//Page<FichaAtendimentoEntity> paged = Mockito.mock(Page.class);
		Page<FichaAtendimentoEntity> paged = new PageImpl<FichaAtendimentoEntity>(Arrays.asList(FichaAtendimentoTest.createFichaAtendimentoEntity()), pageable, 10);
		Page<OrdemServicoEntity> paged2 = new PageImpl<OrdemServicoEntity>(Arrays.asList(OrdemServicoTest.createOrdemServicoEntity()), pageable, 10);
		//Arrays.asList()
		when(this.repository.findById(10, pageable)).thenReturn(paged);
		when(this.repository.findAllFichaByCnpfcpf("123456789", pageable)).thenReturn(paged);
		when(this.repository.findAllFichaByCnpfcpfAndDatas("123456789", inicio, fim, pageable)).thenReturn(paged);
		when(this.repository.findAllFichaByCnpfcpfAndSituacao("123456789", "Aberto", pageable)).thenReturn(paged);
		when(this.repository.findAllFichaByCnpfcpfAndSituacaoAndDatas("123456789", "Aberto", inicio, fim, pageable)).thenReturn(paged);
		when(this.repository.findAllFichaByStatus("Aberto", pageable)).thenReturn(paged);
		when(this.repository.findAllFichas(pageable)).thenReturn(paged);
		when(this.repository.findAllFichaByDatas(inicio, fim, pageable)).thenReturn(paged);
		
		when(this.ordemRepository.findOne(10L)).thenReturn(paged2.getContent().get(0));
		when(this.ordemRepository.findAllOrdensByCnpfcpf("123456789", pageable)).thenReturn(paged2);
		when(this.ordemRepository.findAllOrdensByCnpfcpfAndDatas("123456789", inicio, fim, pageable)).thenReturn(paged2);
		when(this.ordemRepository.findAllOrdensByCnpfcpfAndSituacao("123456789", "Aberto", pageable)).thenReturn(paged2);
		when(this.ordemRepository.findAllOrdensByCnpfcpfAndSituacaoAndDatas("123456789", "Aberto", inicio, fim, pageable)).thenReturn(paged2);
		when(this.ordemRepository.findAllOrdensByStatus("Aberto", pageable)).thenReturn(paged2);
		when(this.ordemRepository.findAllOrdens(pageable)).thenReturn(paged2);
		when(this.ordemRepository.findAllOrdensByDatas(inicio, fim, pageable)).thenReturn(paged2);
		//findAllOrdensByCnpfcpfAndSituacaoAndDatas
	}
	
	@Test
	public void test_pesquisaNumeroFicha_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ficha", 10, null, "Todas", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaCnpjFicha_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ficha", 0, "123456789", "Todas", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaCnpjDataTodasFicha_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ficha", 0, "123456789", "Todas", inicio, fim, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaCnpjAbertoFicha_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ficha", 0, "123456789", "Aberto", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaCnpjDatasAbertoFicha_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ficha", 0, "123456789", "Aberto", inicio, fim, pageable);
		assertNotNull(page);
	}

	@Test
	public void test_pesquisaPorSituacao_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ficha", 0, "", "Aberto", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaAllFichas_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ficha", 0, "", "Todas", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaTodasFichasPorData_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ficha", 0, "", "Todas", inicio, fim, pageable);
		assertNotNull(page);
	}
	
	
	@Test
	public void test_pesquisaOrdemNumero_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ordem", 10, null, "Todas", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaOrdemPorCnpj_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ordem", 0, "123456789", "Todas", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaOrdemCnpjDataTodas_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ordem", 0, "123456789", "Todas", inicio, fim, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaOrdemCnpjAberto_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ordem", 0, "123456789", "Aberto", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaOrdemCnpjDatasAberto_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ordem", 0, "123456789", "Aberto", inicio, fim, pageable);
		assertNotNull(page);
	}

	@Test
	public void test_pesquisaOrdemPorSituacao_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ordem", 0, "", "Aberto", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaTodasOrdens_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ordem", 0, "", "Todas", null, null, pageable);
		assertNotNull(page);
	}
	
	@Test
	public void test_pesquisaOrdemSituacaoTodasPorData_success() {
		Page<ResultadoPesquisaDto> page = this.historicoServiceImpl.pesquisar("Ordem", 0, "", "Todas", inicio, fim, pageable);
		assertNotNull(page);
	}
	

}
