package br.com.fichasordens.restcontroller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Atendimento;
import br.com.fichasordens.Cliente;
import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.Lancamento;
import br.com.fichasordens.Parametro;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.dto.AtendimentoDto;
import br.com.fichasordens.dto.FichaAtendimentoDto;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.ListagemDashboardDto;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.dto.PecaOutroServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.service.GeradorPdfService;
import br.com.fichasordens.util.ConverterAtendimento;
import br.com.fichasordens.util.ConverterCliente;
import br.com.fichasordens.util.ConverterLancamentoDto;
import br.com.fichasordens.util.ConverterPecaOutroServico;
import br.com.fichasordens.util.DataUtil;
import br.com.fichasordens.util.Email;
import br.com.fichasordens.util.StatusServicoEnum;
import br.com.fichasordens.util.TipoServicoEnum;

@RestController
@RequestMapping("/ficha")
@EnableResourceServer
public class FichaAtendimentoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FichaAtendimentoController.class);
	@Autowired
	private FichaAtendimento fichaAtendimento;
	
	@Autowired
	private Atendimento atendimento;
	
	@Autowired 
	private PecaOutroServico pecaOutroServico;
	
	@Autowired
	private GeradorPdfService pdfService;
	
	@Autowired
	private Email email;
	
	@Autowired
	private Parametro parametro;

	
	@PostMapping
	public ResponseEntity salvarFichaAtendimento(@RequestBody final FichaAtendimentoDto dto) {
		try {
			LOGGER.info("Salvar ficha de atendimento");
			FichaAtendimento ficha = this.converterDto(dto);
			ficha = this.fichaAtendimento.salvarFicha(ficha);
			dto.setNumeroFicha(ficha.getId());
			dto.getLancamento().setId(ficha.getId());
			return new ResponseEntity<FichaAtendimentoDto>(dto, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			LOGGER.error("Erro salvando ficha de atendimento", e);
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(path="/pecaServico")
	public ResponseEntity gravarPecaServicoFicha(@RequestBody final PecaOutroServicoDto dto) {
		LOGGER.info("Gravar peças / outro servico");
		final PecaOutroServico peca = ConverterPecaOutroServico.converterDtoPecaServico(dto, TipoServicoEnum.FICHA_ATENDIMENTO);
		this.pecaOutroServico.gravarPecaServicoFicha(peca);
		return new ResponseEntity( HttpStatus.OK);
	}
	
	@PostMapping(path="/atendimento")
	public ResponseEntity gravarAtendimento(@RequestBody final AtendimentoDto dto) {
			final Atendimento atend = ConverterAtendimento.converterAtendimentoDto(dto);
			this.atendimento.gravarAtendimento(atend);
			return new ResponseEntity( HttpStatus.OK);
	}

	@GetMapping(path = "/pdf")
	public void gerarPdf(@RequestParam final long id, HttpServletResponse response) throws Exception {

		final FichaAtendimento ficha = this.fichaAtendimento.buscarFichaAtendimento(id);
		ByteArrayOutputStream out = pdfService.gerarFichaAtendimentoPdf(ficha);

		response.addHeader("Content-disposition", "attachment;filename=ordem-" + id + ".pdf");
		response.setHeader("Content-Length", String.valueOf(out.size()));
		response.setContentType("application/pdf");

		OutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(out.toByteArray());
		responseOutputStream.close();
		out.close();
		response.flushBuffer();
	}
	
	@GetMapping(path = "/email")
	public ResponseEntity enviarEmail(@RequestParam final long id, HttpServletResponse response) throws Exception {

		final FichaAtendimento ficha = this.fichaAtendimento.buscarFichaAtendimento(id);
		try(ByteArrayOutputStream out = pdfService.gerarFichaAtendimentoPdf(ficha)) {
			final String situacao = ficha.getFichaAtendimentoLancList().get(ficha.getFichaAtendimentoLancList().size() - 1).getSituacao();
		
			this.email.enviarEmailComAnexo(ficha.getCliente().getEmail(),"Ficha de Atendimento - IslucNet", 
				"Segue anexo Ficha de Atendimento para acompanhamento do servico <br><br> Situação atual da ficha: <strong>" + situacao + "<strong>", out,
				"Ficha_de_Atendimento_" + ficha.getId());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Erro ao enviar email para {}" + ficha.getCliente().getNome(), e);
			return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
		}
	}
		
	@GetMapping
	public ResponseEntity listaFichas(@RequestParam final String situacao) {
		List<FichaAtendimento> fichaList = this.fichaAtendimento.listarFichas(StatusServicoEnum.valueOf(situacao.toUpperCase()));
		List<ListagemDashboardDto> dtoList = new ArrayList<>();
		Parametro param = parametro.buscarValorParametroAlerta();
		final int qtdDiasAlerta = param.getValor().intValue();
		fichaList.forEach(a->{
			ListagemDashboardDto dto = new ListagemDashboardDto();
			dto.setId(a.getId());
			dto.setNomeCliente(a.getCliente().getNome());
			dto.setSituacao(situacao);
			dto.setTipoServico("Ficha de Atendimento");
			for(Lancamento lanc:  a.getFichaAtendimentoLancList()) {
				if (lanc.getSituacao().equals(situacao)) {
					dto.setResponsavel(lanc.getUsuario().getNome());
					dto.setDias((int) DataUtil.calcularDiferencaDiasEntreUmaDataEAgora(lanc.getData()));
					if (dto.getDias() > qtdDiasAlerta) 
						dto.setAlerta("S");
					else
						dto.setAlerta("N");
				}
				
				if (lanc.getSituacao().equals("Aberto")) {
					dto.setDataAbertura(lanc.getData());
				}
			}
			dtoList.add(dto);
		});
		
		return new ResponseEntity<>(dtoList,HttpStatus.OK);
		
	}
	
	@GetMapping(path="/buscar")
	public FichaAtendimentoDto buscarFicha(@RequestParam final long id) {
		FichaAtendimento ficha = this.fichaAtendimento.buscarFichaAtendimento(id);
		return this.converterParaDto(ficha);
	}
	
	@GetMapping(path="/calcAtendimento")
	public ResponseEntity calcularValorAtendimento(@RequestParam final int horas, @RequestParam final int tipo) {
		return new ResponseEntity(this.atendimento.calcularValorAtendimento(horas, tipo), HttpStatus.OK);
	}
	
	
	@DeleteMapping(path="/atendimento")
	public ResponseEntity excluirAtendimento(@RequestParam final int idFicha, @RequestParam final int sequencia) {
		this.atendimento.excluirAtendimento(idFicha, sequencia);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@DeleteMapping(path="/pecaServico")
	public ResponseEntity excluirPecaOutroServico(@RequestParam final int idFicha, @RequestParam final int sequencia) {
		this.pecaOutroServico.excluirPecaOutroServicoFicha(idFicha, sequencia);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	private FichaAtendimentoDto converterParaDto(final FichaAtendimento ficha) {
		final FichaAtendimentoDto dto = new FichaAtendimentoDto();
		dto.setTipoServico(ficha.getTipoServico());
		dto.setNumeroFicha(ficha.getId());
		dto.setCliente(ConverterCliente.converterClienteParaDto(ficha.getCliente()));

		dto.setLancamentoLst(new ArrayList<LancamentoDto>());
		if (ficha.getFichaAtendimentoLancList() != null) {
			for(Lancamento lanc: ficha.getFichaAtendimentoLancList()) {
				final LancamentoDto lancDto = ConverterLancamentoDto.converterLancamentoParaDto(lanc);
				dto.getLancamentoLst().add(lancDto);
			}
		}
		dto.setPecaOutroServicoDto(new ArrayList<PecaOutroServicoDto>());
		if (ficha.getPecaOutroServicoList() != null) {
			for(PecaOutroServico p: ficha.getPecaOutroServicoList()) {
				dto.getPecaOutroServicoDto().add(ConverterPecaOutroServico.converterPecaOutroServicoParaDto(p));
			}
		}
		dto.setAtendimento(new ArrayList<AtendimentoDto>());
		if (ficha.getAtendimentoList() != null) {
			for(Atendimento p : ficha.getAtendimentoList() ) {
				dto.getAtendimento().add(converterAtendimentoParaAtendimentoDto(ficha, p));
			}
		}
		return dto;
	}

	private AtendimentoDto converterAtendimentoParaAtendimentoDto(final FichaAtendimento ficha, Atendimento p) {
		AtendimentoDto atendDto = new AtendimentoDto();
		atendDto.setDataAtendimento(p.getData());
		atendDto.setDescricao(p.getDescricao());
		atendDto.setDuracao(p.getDuracao());
		atendDto.setSequencia(p.getSequencia());
		atendDto.setValor(p.getValor());
		atendDto.setId(ficha.getId());
		return atendDto;
	}
	
	
	private FichaAtendimento converterDto(final FichaAtendimentoDto dto) throws ExcecaoRetorno {
		final FichaAtendimento ficha = new FichaAtendimento();
		ficha.setTipoServico(dto.getTipoServico());
		ficha.setId(dto.getNumeroFicha());
		final Cliente cliente = new Cliente();
		if (dto.getCliente().getId() == 0) {
			throw new ExcecaoRetorno("O Cliente não informado");
		}
		cliente.setId(dto.getCliente().getId());
		ficha.setCliente(cliente);
		ficha.setFichaAtendimentoLancList(new ArrayList<Lancamento>());
		Lancamento lanc = ConverterLancamentoDto.converterDtoParaFichaAtendimentoLanc(dto.getLancamento());
		ficha.getFichaAtendimentoLancList().add(lanc);
		return ficha;
	}
}
