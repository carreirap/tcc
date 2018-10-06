package br.com.fichasordens.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fichasordens.Atendimento;
import br.com.fichasordens.Cliente;
import br.com.fichasordens.Empresa;
import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.Lancamento;
import br.com.fichasordens.Parametro;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.dto.AtendimentoDto;
import br.com.fichasordens.dto.ClienteDto;
import br.com.fichasordens.dto.FichaAtendimentoDto;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.ListagemDashboardDto;
import br.com.fichasordens.dto.PecaOutroServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.service.GeradorPdfService;
import br.com.fichasordens.util.Email;
import br.com.fichasordens.util.StatusServicoEnum;
import net.sf.jasperreports.engine.JRException;

public class FichaAtendimentoControllerTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private FichaAtendimentoController fichaAtendimentoController;
	
	@Mock 
	private FichaAtendimento fichaAtendimento;
	
	@Mock 
	private Atendimento Atendimento;
	
	@Mock
	private PecaOutroServico pecaOutroServico;
	
	@Mock
	private Parametro parametro;
	
	@Mock
	private Empresa empresa;
	
	@Mock
	private GeradorPdfService pdfService;
	
	@Mock
	private Email email;
	
	@Test
	public void test_salvarFichaAtendimento_success() throws ExcecaoRetorno {
		FichaAtendimentoDto ficha = createFichaAtendimentoDto();
		
		when(this.fichaAtendimento.salvarFicha(org.mockito.Mockito.any(FichaAtendimento.class))).thenReturn(createFichaAtendimento());
		
		ResponseEntity response = this.fichaAtendimentoController.salvarFichaAtendimento(ficha);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_salvarFichaAtendimento_fail() throws ExcecaoRetorno {
		FichaAtendimentoDto ficha = createFichaAtendimentoDto();
		
		when(this.fichaAtendimento.salvarFicha(org.mockito.Mockito.any(FichaAtendimento.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.fichaAtendimentoController.salvarFichaAtendimento(ficha);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_salvarFichaAtendimento_fail2() throws ExcecaoRetorno {
		FichaAtendimentoDto ficha = createFichaAtendimentoDto();
		ficha.getCliente().setId(0);
		
		when(this.fichaAtendimento.salvarFicha(org.mockito.Mockito.any(FichaAtendimento.class))).thenThrow(new ExcecaoRetorno());
		
		ResponseEntity response = this.fichaAtendimentoController.salvarFichaAtendimento(ficha);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void test_gravarPecaServicoFicha_success() {
		PecaOutroServicoDto dto = criarPecaOutroServicoDto();
		doNothing().when(this.pecaOutroServico).gravarPecaServicoFicha(org.mockito.Mockito.any(PecaOutroServico.class));
		ResponseEntity response = this.fichaAtendimentoController.gravarPecaServicoFicha(dto);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_buscarFicha_success() {
		when(this.fichaAtendimento.buscarFichaAtendimento(199)).thenReturn(createFichaAtendimento());
		FichaAtendimentoDto response = this.fichaAtendimentoController.buscarFicha(199);
		
		assertNotNull(response);
		assertEquals("Ficha de Atendimento", response.getTipoServico());
	}
	
	@Test
	public void test_buscarFichaNullsLists_success() {
		FichaAtendimento ficha = createFichaAtendimento();
		ficha.setPecaOutroServicoList(null);
		ficha.setAtendimentoList(null);
		ficha.setFichaAtendimentoLancList(null);
		when(this.fichaAtendimento.buscarFichaAtendimento(199)).thenReturn(ficha);
		FichaAtendimentoDto response = this.fichaAtendimentoController.buscarFicha(199);
		
		assertNotNull(response);
		assertEquals("Ficha de Atendimento", response.getTipoServico());
		assertEquals(0, response.getAtendimento().size());
	}
	
	@Test
	public void test_gravarAtendimento_success() {
		AtendimentoDto dto = createAtendimentoDto();
		doNothing().when(this.Atendimento).gravarAtendimento(org.mockito.Mockito.any(Atendimento.class));
		ResponseEntity response = this.fichaAtendimentoController.gravarAtendimento(dto);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_calcularValorAtendimento_success() {
		
		when(this.Atendimento.calcularValorAtendimento(10, 5)).thenReturn(new BigDecimal(50));
		ResponseEntity response = this.fichaAtendimentoController.calcularValorAtendimento(10, 5);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(new BigDecimal(50), (BigDecimal) response.getBody());
	}
	
	@Test
	public void test_excluirAtendimento_success() {
		doNothing().when(this.Atendimento).excluirAtendimento(199, 1);
		ResponseEntity response = this.fichaAtendimentoController.excluirAtendimento(199, 1);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_listaFichas_success() {
		FichaAtendimento ficha = createFichaAtendimento();
		when(this.fichaAtendimento.listarFichas(StatusServicoEnum.valueOf("Aberto".toUpperCase()))).thenReturn(Arrays.asList(ficha));
		when(this.parametro.buscarValorParametroAlerta()).thenReturn(createParametroAlerta());
		ResponseEntity response = this.fichaAtendimentoController.listaFichas("Aberto");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, ((List<ListagemDashboardDto>)response.getBody()).size());
		
	}
	
	@Test
	public void test_excluirPecaOutroServico_success() {
		doNothing().when(this.pecaOutroServico).excluirPecaOutroServicoFicha(199, 1);
		ResponseEntity response = this.fichaAtendimentoController.excluirPecaOutroServico(199, 1);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void test_gerarPdfDownload_success() throws IOException, JRException {
		FichaAtendimento ficha =  createFichaAtendimento();
		when(this.fichaAtendimento.buscarFichaAtendimento(199)).thenReturn(ficha);
		when(this.empresa.buscarEmpresa()).thenReturn(EmpresaControllerTest.carregarEmpresa());
		ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
		when(this.pdfService.gerarFichaAtendimentoPdf(ficha)).thenReturn(pdfReportStream);
		HttpServletResponse response = mock(HttpServletResponse.class);
		ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		this.fichaAtendimentoController.gerarPdf(199, response);
		Mockito.verify(response, Mockito.times(1)).getOutputStream();
		Mockito.verify(response, Mockito.timeout(1)).flushBuffer();
	}
	
	
	@Test
	public void test_enviarEmail_success() throws JRException, MessagingException, FileNotFoundException {
		final FichaAtendimento ordem = createFichaAtendimento();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		when(this.fichaAtendimento.buscarFichaAtendimento(199)).thenReturn(ordem);
		when(this.pdfService.gerarFichaAtendimentoPdf(ordem)).thenReturn(out);
		
		doNothing().when(this.email).enviarEmailComAnexo(ordem.getCliente().getEmail(), 
				"Ordem de Serviço - IslucNet", 
				"Segue anexo Ordem de Servico para acompanhamento <br><br> Situação atual da Ordem: <strong>" + ordem.getFichaAtendimentoLancList().get(0).getSituacao() + "<strong>", out, 
				"Ordem_de_Servico_" + ordem.getId());
		
		HttpServletResponse httpResponse = mock(HttpServletResponse.class);
		ResponseEntity response =  this.fichaAtendimentoController.enviarEmail(199, httpResponse);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	@Test
	public void test_enviarEmail_fail() throws JRException, MessagingException, FileNotFoundException {
		final FichaAtendimento ordem = createFichaAtendimento();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		when(this.fichaAtendimento.buscarFichaAtendimento(199)).thenReturn(ordem);
		when(this.pdfService.gerarFichaAtendimentoPdf(ordem)).thenThrow(new JRException("error"));
		doNothing().when(this.email).enviarEmailComAnexo(ordem.getCliente().getEmail(), 
				"Ordem de Serviço - IslucNet", 
				"Segue anexo Ordem de Servico para acompanhamento <br><br> Situação atual da Ordem: <strong>" + ordem.getFichaAtendimentoLancList().get(0).getSituacao() + "<strong>", out, 
				"Ordem_de_Servico_" + ordem.getId());
		
		HttpServletResponse httpResponse = mock(HttpServletResponse.class);
		ResponseEntity response =  this.fichaAtendimentoController.enviarEmail(199, httpResponse);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
	}
	
	@Test
	public void test_enviarEmail_fail2() throws JRException, MessagingException, FileNotFoundException {
		final FichaAtendimento ordem = createFichaAtendimento();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		when(this.fichaAtendimento.buscarFichaAtendimento(199)).thenReturn(ordem);
		when(this.pdfService.gerarFichaAtendimentoPdf(ordem)).thenReturn(out);
		doThrow(new MessagingException("error")).when(this.email).enviarEmailComAnexo(ordem.getCliente().getEmail(), 
				"Ficha de Atendimento - IslucNet", 
				"Segue anexo Ficha de Atendimento para acompanhamento do servico <br><br> Situação atual da ficha: <strong>" + ordem.getFichaAtendimentoLancList().get(ordem.getFichaAtendimentoLancList().size() - 1).getSituacao() + "<strong>", out, 
				"Ficha_de_Atendimento_" + ordem.getId());
		
		HttpServletResponse httpResponse = mock(HttpServletResponse.class);
		ResponseEntity response =  this.fichaAtendimentoController.enviarEmail(199, httpResponse);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
	}
	
	@Test
	public void test_enviarEmail_fail3() throws JRException, MessagingException, FileNotFoundException {
		final FichaAtendimento ordem = createFichaAtendimento();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		when(this.fichaAtendimento.buscarFichaAtendimento(199)).thenReturn(ordem);
		when(this.pdfService.gerarFichaAtendimentoPdf(ordem)).thenThrow(new FileNotFoundException("error"));
		doNothing().when(this.email).enviarEmailComAnexo(ordem.getCliente().getEmail(), 
				"Ordem de Serviço - IslucNet", 
				"Segue anexo Ordem de Servico para acompanhamento <br><br> Situação atual da Ordem: <strong>" + ordem.getFichaAtendimentoLancList().get(0).getSituacao() + "<strong>", out, 
				"Ordem_de_Servico_" + ordem.getId());
		
		HttpServletResponse httpResponse = mock(HttpServletResponse.class);
		ResponseEntity response =  this.fichaAtendimentoController.enviarEmail(199, httpResponse);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
	}
	
	private FichaAtendimentoDto createFichaAtendimentoDto() {
		FichaAtendimentoDto dto = new FichaAtendimentoDto();
		dto.setCliente(new ClienteDto());
		dto.getCliente().setId(144);
		dto.setLancamento(new LancamentoDto());
		return dto;
	}
	
	private Lancamento createLancamento() {
		Lancamento lanc = new Lancamento();
		lanc.setData(new Date());
		lanc.setFichaAtendimento(new FichaAtendimento());
		lanc.setObservacao("Test");
		lanc.setSequencia(0);
		lanc.setSituacao("Aberto");
		lanc.setUsuario(UsuarioControllerTest.carregarUsuario());;
		
		return lanc;
	}
	
	private AtendimentoDto createAtendimentoDto() {
		AtendimentoDto dto = new AtendimentoDto();
		dto.setDataAtendimento(new Date());
		dto.setDescricao("Test");
		dto.setDuracao(new BigDecimal(2));
		dto.setId(199);
		dto.setIdUsuario(1);
		dto.setSequencia(1);
		dto.setValor(new BigDecimal(200));
		
		return dto;
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
	
	private FichaAtendimento createFichaAtendimento() {
		FichaAtendimento ficha = new FichaAtendimento();
		ficha.setCliente(loadCliente());
		ficha.setId(199);
		ficha.setTipoServico("Ficha de Atendimento");
		ficha.setAtendimentoList(Arrays.asList(createAtendimento()));
		ficha.setPecaOutroServicoList(Arrays.asList(createPecaOutroServico()));
		Lancamento trab = createLancamento();
		trab.setSituacao("Trabalhando");
		ficha.setFichaAtendimentoLancList(Arrays.asList(createLancamento(), trab));
		return ficha;
	}
	
	private Atendimento createAtendimento() {
		Atendimento atend = new Atendimento();
		atend.setData(new Date());
		atend.setDescricao("Test");
		atend.setDuracao(new BigDecimal(10));
		atend.setSequencia(1);
		atend.setValor(new BigDecimal(200));
		
		return atend;
	}
	
	private PecaOutroServico createPecaOutroServico() {
		PecaOutroServico peca = new PecaOutroServico();
		peca.setDescricao("Test");
		peca.setId(2);
		peca.setQuantidade(1);
		peca.setValor(new BigDecimal(900));;
		peca.setFichaAtendimento(new FichaAtendimento());
		
		return peca;
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
	
	public static Parametro createParametroAlerta() {
		Parametro p = new Parametro();
		p.setId((byte)3);
		p.setValor(new BigDecimal(2));
		return p;
	}

}
