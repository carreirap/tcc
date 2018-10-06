package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.Empresa;
import br.com.fichasordens.Lancamento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.Parametro;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.dto.ClienteDto;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.ListagemDashboardDto;
import br.com.fichasordens.dto.OrdemServicoDto;
import br.com.fichasordens.dto.PecaOutroServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.service.GeradorPdfService;
import br.com.fichasordens.util.Email;
import br.com.fichasordens.util.StatusServicoEnum;
import net.sf.jasperreports.engine.JRException;

public class OrdemServicoControllerTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private OrdemServicoController ordemServicoController;
	
	@Mock 
	OrdemServico ordemServico;
	
	@Mock
	private Parametro parametro;
	
	@Mock
	private PecaOutroServico pecaOutroServico;
	
	@Mock
	private Empresa empresa;
	
	@Mock
	private GeradorPdfService pdfService;

	@Mock
	private Email email;
	
	@Before
	public void init() {
		when(this.parametro.buscarValorParametroAlerta()).thenReturn(FichaAtendimentoControllerTest.createParametroAlerta());
	}
	
	@Test
	public void test_salvarOrdemServico_success() throws ExcecaoRetorno {
		OrdemServicoDto ordem = criarDto();
		OrdemServico ord = new OrdemServico();
		ord.setId(10L);
		when(this.ordemServico.salvarOrdem(org.mockito.Mockito.any(OrdemServico.class))).thenReturn(ord);
		
		ResponseEntity response = this.ordemServicoController.salvarOrdemServico(ordem);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void test_salvarOrdemServico_fail() throws ExcecaoRetorno {
		OrdemServicoDto ordem = criarDto();
		when(this.ordemServico.salvarOrdem(org.mockito.Mockito.any(OrdemServico.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.ordemServicoController.salvarOrdemServico(ordem);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_salvarOrdemServicoDefeito_fail() throws ExcecaoRetorno {
		OrdemServicoDto ordem = criarDto();
		ordem.setDescDefeito("");
		when(this.ordemServico.salvarOrdem(org.mockito.Mockito.any(OrdemServico.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.ordemServicoController.salvarOrdemServico(ordem);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_salvarOrdemServicoDescEquip_fail() throws ExcecaoRetorno {
		OrdemServicoDto ordem = criarDto();
		ordem.setDescEquip("");
		when(this.ordemServico.salvarOrdem(org.mockito.Mockito.any(OrdemServico.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.ordemServicoController.salvarOrdemServico(ordem);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_salvarOrdemServicoFabricante_fail() throws ExcecaoRetorno {
		OrdemServicoDto ordem = criarDto();
		ordem.setFabricante("");
		when(this.ordemServico.salvarOrdem(org.mockito.Mockito.any(OrdemServico.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.ordemServicoController.salvarOrdemServico(ordem);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_salvarOrdemServicoCliente_fail() throws ExcecaoRetorno {
		OrdemServicoDto ordem = criarDto();
		ordem.getCliente().setId(0);
		when(this.ordemServico.salvarOrdem(org.mockito.Mockito.any(OrdemServico.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.ordemServicoController.salvarOrdemServico(ordem);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_salvarItemOrdemServico_sucess() throws ExcecaoRetorno {
		PecaOutroServicoDto dto = criarPecaOutroServicoDto();
		doNothing().when(this.pecaOutroServico).gravarPecaServicoOrdem(org.mockito.Mockito.any(PecaOutroServico.class));
		
		ResponseEntity response = this.ordemServicoController.salvarItemOrdemServico(dto);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	
	@Test
	public void test_salvarItemOrdemServico_fail() throws ExcecaoRetorno {
		PecaOutroServicoDto dto = criarPecaOutroServicoDto();
		doThrow(new ExcecaoRetorno()).when(this.pecaOutroServico).gravarPecaServicoOrdem(org.mockito.Mockito.any(PecaOutroServico.class));
		ResponseEntity response = this.ordemServicoController.salvarItemOrdemServico(dto);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_buscarOrdem_success() {
		final OrdemServico ordem = criarOrdemServico();
		when(this.ordemServico.buscarOrdem(199)).thenReturn(ordem);

		OrdemServicoDto dto = this.ordemServicoController.buscarOrdem(199);
		
		assertNotNull(dto);
		assertEquals("mouse pad não funciona", dto.getEstadoItensAcomp());
		assertEquals("Test", dto.getDescEquip());
		
	}
	
	@Test
	public void test_buscarOrdemNullList_success() {
		final OrdemServico ordem = criarOrdemServico();
		ordem.setLancamento(null);
		ordem.setPecaOutroServico(null);
		when(this.ordemServico.buscarOrdem(199)).thenReturn(ordem);

		OrdemServicoDto dto = this.ordemServicoController.buscarOrdem(199);
		
		assertNotNull(dto);
		assertEquals("mouse pad não funciona", dto.getEstadoItensAcomp());
		assertEquals("Test", dto.getDescEquip());
		
	}
	
	@Test
	public void test_deletarPecaOutroServico_success() {
		final OrdemServico ordem = criarOrdemServico();
		doNothing().when(this.pecaOutroServico).excluirPecaOutroServicoOrdem(199,1);

		ResponseEntity response = this.ordemServicoController.deletarPecaServicoOrdem(199, 1);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_listaFichas_success() {
		final OrdemServico ordem = criarOrdemServico();
		when(this.ordemServico.listarOrdens(StatusServicoEnum.TRABALHANDO)).thenReturn(Arrays.asList(ordem));
		ResponseEntity response = this.ordemServicoController.listaFichas(StatusServicoEnum.TRABALHANDO.getValue());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("N", ((List<ListagemDashboardDto>) response.getBody()).get(0).getAlerta());
	}
	
	@Test
	public void test_listaFichasAlerta_success() {
		final OrdemServico ordem = criarOrdemServico();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -3);
		ordem.getLancamento().get(0).setData(c.getTime());
		when(this.ordemServico.listarOrdens(StatusServicoEnum.TRABALHANDO)).thenReturn(Arrays.asList(ordem));
		ResponseEntity response = this.ordemServicoController.listaFichas(StatusServicoEnum.TRABALHANDO.getValue());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("S", ((List<ListagemDashboardDto>) response.getBody()).get(0).getAlerta());
	}
	
	@Test
	public void test_listaFichasAberta_success() {
		final OrdemServico ordem = criarOrdemServico();
		
		when(this.ordemServico.listarOrdens(StatusServicoEnum.ABERTO)).thenReturn(Arrays.asList(ordem));
		ResponseEntity response = this.ordemServicoController.listaFichas(StatusServicoEnum.ABERTO.getValue());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(StatusServicoEnum.ABERTO.getValue(), ((List<ListagemDashboardDto>) response.getBody()).get(0).getSituacao());
	}
	
	@Test
	public void test_gerarPdfDownload_success() throws IOException, JRException {
		OrdemServico ordem = criarOrdemServico();
		when(this.ordemServico.buscarOrdem(199)).thenReturn(ordem);
		when(this.empresa.buscarEmpresa()).thenReturn(EmpresaControllerTest.carregarEmpresa());
		ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
		when(this.pdfService.gerarOrdemServicoPdf(ordem)).thenReturn(pdfReportStream);
		HttpServletResponse response = mock(HttpServletResponse.class);
		ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		this.ordemServicoController.gerarPdf(199, response);
		Mockito.verify(response, Mockito.times(1)).getOutputStream();
		Mockito.verify(response, Mockito.timeout(1)).flushBuffer();
	}
	
	@Test
	public void test_enviarEmail_success() throws JRException, MessagingException {
		final OrdemServico ordem = criarOrdemServico();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		when(this.ordemServico.buscarOrdem(199)).thenReturn(ordem);
		when(this.pdfService.gerarOrdemServicoPdf(ordem)).thenReturn(out);
		
		doNothing().when(this.email).enviarEmailComAnexo(ordem.getCliente().getEmail(), 
				"Ordem de Serviço - IslucNet", 
				"Segue anexo Ordem de Servico para acompanhamento <br><br> Situação atual da Ordem: <strong>" + ordem.getLancamento().get(0).getSituacao() + "<strong>", out, 
				"Ordem_de_Servico_" + ordem.getId());
		
		HttpServletResponse httpResponse = mock(HttpServletResponse.class);
		ResponseEntity response =  this.ordemServicoController.enviarEmail(199, httpResponse);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	@Test
	public void test_enviarEmail_fail() throws JRException, MessagingException {
		final OrdemServico ordem = criarOrdemServico();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		when(this.ordemServico.buscarOrdem(199)).thenReturn(ordem);
		when(this.pdfService.gerarOrdemServicoPdf(ordem)).thenThrow(new JRException("error"));
		doNothing().when(this.email).enviarEmailComAnexo(ordem.getCliente().getEmail(), 
				"Ordem de Serviço - IslucNet", 
				"Segue anexo Ordem de Servico para acompanhamento <br><br> Situação atual da Ordem: <strong>" + ordem.getLancamento().get(0).getSituacao() + "<strong>", out, 
				"Ordem_de_Servico_" + ordem.getId());
		
		HttpServletResponse httpResponse = mock(HttpServletResponse.class);
		ResponseEntity response =  this.ordemServicoController.enviarEmail(199, httpResponse);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
	}
		
	private PecaOutroServicoDto criarPecaOutroServicoDto() {
		PecaOutroServicoDto dto = new PecaOutroServicoDto();
		dto.setDescricao("Test");
		dto.setIdOrdem(199);
		dto.setQtde(1);
		dto.setSequencia(0);
		dto.setTotal(new BigDecimal(200.10));
		dto.setValor(new BigDecimal(100.05));
		return dto;
	}
	
	private OrdemServico criarOrdemServico() {
		OrdemServico ordem = new OrdemServico();
		ordem.setCliente(loadCliente());
		ordem.setDescDefeito("Test");
		ordem.setDescEquip("Test");
		ordem.setEstadoItensAcomp("mouse pad não funciona");
		ordem.setFabricante("Samsung");
		ordem.setId(199);
		Lancamento lanc = createLancamento();
		ordem.setLancamento(Arrays.asList(createLancamento(), lanc));
		ordem.getLancamento().get(1).setSituacao("Aberto");
		ordem.setPecaOutroServico(Arrays.asList(createPecaOutroServico()));
		
		return ordem;
	}
	
	private Lancamento createLancamento() {
		Lancamento lanc = new Lancamento();
		lanc.setData(new Date());
		lanc.setOrdemServico(new OrdemServico());
		lanc.setObservacao("Test");
		lanc.setSequencia(0);
		lanc.setSituacao("Trabalhando");
		lanc.setUsuario(UsuarioControllerTest.carregarUsuario());;
		
		return lanc;
	}
	
	private PecaOutroServico createPecaOutroServico() {
		PecaOutroServico peca = new PecaOutroServico();
		peca.setDescricao("Test");
		peca.setId(199);
		peca.setOrdemServico(new OrdemServico());
		peca.setQuantidade(1);
		peca.setValor(new BigDecimal(10));
		
		return peca;
	}
	
	private OrdemServicoDto criarDto() {
		OrdemServicoDto ordem = new OrdemServicoDto();
		ordem.setTipoServico("true");
		ordem.setCliente(new ClienteDto());
		ordem.getCliente().setId(10);
		ordem.setDescDefeito("test");
		ordem.setDescEquip("test");
		ordem.setEstadoItensAcomp("test");
		ordem.setLancamento(new LancamentoDto());
		return ordem;
	}
	
	public static Cliente loadCliente() {
		Cliente cli = new Cliente();
		cli.setCelular("99999999999999");
		cli.setCnpjCpf("cnpjCpf");
		cli.setEmail("test");
		cli.setFone("988888888");
		cli.setNome("test");
		return cli;
	}

}
