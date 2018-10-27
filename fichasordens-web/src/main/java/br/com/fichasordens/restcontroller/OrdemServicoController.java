package br.com.fichasordens.restcontroller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.Lancamento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.Parametro;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.ListagemDashboardDto;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.dto.OrdemServicoDto;
import br.com.fichasordens.dto.PecaOutroServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.service.GeradorPdfService;
import br.com.fichasordens.util.Email;
import br.com.fichasordens.util.StatusServicoEnum;
import br.com.fichasordens.web.util.ConverterCliente;
import br.com.fichasordens.web.util.ConverterLancamentoDto;
import br.com.fichasordens.web.util.ConverterPecaOutroServico;
import br.com.fichasordens.web.util.Downloader;
import br.com.fichasordens.web.util.TipoServicoEnum;
import net.sf.jasperreports.engine.JRException;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/ordem")
@EnableResourceServer
public class OrdemServicoController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrdemServicoController.class);
	
	private static final String ORDEM_DE_SERVICO = "Ordem de Servico";
	@Autowired
	private OrdemServico ordemServico;
	
	@Autowired
	private GeradorPdfService pdfService;
	
	@Autowired
	private Parametro parametro;
	
	@Autowired
	private PecaOutroServico pecaOutroServico;
	
	@Autowired
	private Email email;

	
	@PostMapping
	public ResponseEntity salvarOrdemServico(@RequestBody final OrdemServicoDto dto) {
		try {
			OrdemServico ordem = this.converterDtoParaOrdemServico(dto);
			ordem = this.ordemServico.salvarOrdem(ordem);
			dto.setNumeroOrdem(ordem.getId());
			LOGGER.info("Ordem de Serviço {} salva com sucesso", ordem.getId());
			return new ResponseEntity<OrdemServicoDto>(dto, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(path="/pecaServico")
	public ResponseEntity salvarItemOrdemServico(@RequestBody final PecaOutroServicoDto dto) {
		try {
			final PecaOutroServico peca = ConverterPecaOutroServico.converterDtoPecaServico(dto, TipoServicoEnum.ORDEM_SERVICO);
			this.pecaOutroServico.gravarPecaServicoOrdem(peca);
			return new ResponseEntity( HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(path="/pecaServico")
	public ResponseEntity deletarPecaServicoOrdem(@RequestParam final int id, @RequestParam final int sequencia) {
		this.pecaOutroServico.excluirPecaOutroServicoOrdem(id, sequencia);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	
	@GetMapping
	public ResponseEntity listaFichas(@RequestParam final String situacao) {
		List<OrdemServico> ordemList = this.ordemServico.listarOrdens(StatusServicoEnum.valueOf(situacao.toUpperCase()));
		List<ListagemDashboardDto> dtoList = new ArrayList<>();
		Parametro param = parametro.buscarValorParametroAlerta();
		final int qtdDiasAlerta = param.getValor().intValue();
		ordemList.forEach(a->{
			ListagemDashboardDto dto = new ListagemDashboardDto();
			dto.setId(a.getId());
			dto.setNomeCliente(a.getCliente().getNome());
			dto.setSituacao(situacao);
			dto.setTipoServico(ORDEM_DE_SERVICO);
			ConverterLancamentoDto.converterLancamentoParaDto(situacao, qtdDiasAlerta, a.getLancamento(), dto);
			dtoList.add(dto);
		});
		
		return new ResponseEntity<>(dtoList,HttpStatus.OK);
	}

	
	
	@GetMapping(path="/buscar")
	public OrdemServicoDto buscarOrdem(@RequestParam final long id) {
		final OrdemServico ordem = this.ordemServico.buscarOrdem(id);
		return this.converterOrdemServicoParaDto(ordem);
	}
	
	@GetMapping(path = "/pdf")
	public void gerarPdf(@RequestParam final long id, HttpServletResponse response) throws IOException, JRException {

		final OrdemServico ordem = this.ordemServico.buscarOrdem(id);
		ByteArrayOutputStream out = pdfService.gerarOrdemServicoPdf(ordem);
		
		Downloader.retornarArquivoParaDownload(response, id, out);
	}
	
	@GetMapping(path = "/email")
	public ResponseEntity enviarEmail(@RequestParam final long id, HttpServletResponse response) {

		final OrdemServico ordem = this.ordemServico.buscarOrdem(id);
		try(ByteArrayOutputStream out = pdfService.gerarOrdemServicoPdf(ordem)) {
			final String situacao = ordem.getLancamento().get(ordem.getLancamento().size() - 1).getSituacao();
		
			this.email.enviarEmailComAnexo(ordem.getCliente().getEmail(),"Ordem de Serviço - IslucNet", 
				"Segue anexo Ordem de Servico para acompanhamento <br><br> Situação atual da Ordem: <strong>" + situacao + "<strong>", out,
				"Ordem_de_Servico_" + ordem.getId());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Erro ao enviar email para {}" + ordem.getCliente().getNome(), e);
			return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
		}
	}
	
	private OrdemServico converterDtoParaOrdemServico(final OrdemServicoDto dto) throws ExcecaoRetorno {
		final OrdemServico ent = new OrdemServico();
		ent.setId(dto.getNumeroOrdem());
		ent.setFabricante(dto.getFabricante());
		if (StringUtils.isEmpty(dto.getDescDefeito())) {
			throw new ExcecaoRetorno("O campo desc. defeito é obrigatório");
		}
		ent.setDescDefeito(dto.getDescDefeito());
		if (StringUtils.isEmpty(dto.getDescEquip())) {
			throw new ExcecaoRetorno("O campo desc. equipamento é obrigatório");
		}
		ent.setDescEquip(dto.getDescEquip());
		if (StringUtils.isEmpty(dto.getEstadoItensAcomp())) {
			throw new ExcecaoRetorno("O estado e os itens que acompanham são obrigatórios");
		}
		ent.setEstadoItensAcomp(dto.getEstadoItensAcomp());
		ent.setModelo(dto.getModelo());
		ent.setSerie(dto.getSerie());
		ent.setTipoServico(dto.getTipoServico());
		final Cliente cliente = new Cliente();
		cliente.setId(dto.getCliente().getId());
		if (dto.getCliente().getId() == 0) {
			throw new ExcecaoRetorno("O Cliente não foi informado");
		}
		ent.setCliente(cliente);
		ent.setLancamento(new ArrayList<Lancamento>());
		Lancamento lanc = ConverterLancamentoDto.converterDtoParaOrdemServicoLanc(dto.getLancamento());
		ent.getLancamento().add(lanc);
		return ent;
	}
	
	private OrdemServicoDto converterOrdemServicoParaDto(final OrdemServico ordem) {
		final OrdemServicoDto dto = new OrdemServicoDto();
		dto.setNumeroOrdem(ordem.getId());
		dto.setFabricante(ordem.getFabricante());
		dto.setDescDefeito(ordem.getDescDefeito());
		dto.setDescEquip(ordem.getDescEquip());
		dto.setEstadoItensAcomp(ordem.getEstadoItensAcomp());
		dto.setModelo(ordem.getModelo());
		dto.setSerie(ordem.getSerie());
		dto.setTipoServico(ordem.getTipoServico());
		dto.setCliente(ConverterCliente.converterClienteParaDto(ordem.getCliente()));
	
		dto.setLancamentoLst(new ArrayList<LancamentoDto>());
		if (ordem.getLancamento() != null) {
			for (Lancamento lanc : ordem.getLancamento()) {
				final LancamentoDto lancDto = ConverterLancamentoDto.converterOrdemLancamentoParaDto(lanc);
				dto.getLancamentoLst().add(lancDto);
			}
		}
		
		
		dto.setPecaOutroServicoDto(new ArrayList<PecaOutroServicoDto>());
		if (ordem.getPecaOutroServico() != null) {
			for(PecaOutroServico p: ordem.getPecaOutroServico()) {
				dto.getPecaOutroServicoDto().add(ConverterPecaOutroServico.converterPecaOutroServicoParaDto(p));
			}
		}
		return dto;
	}

}
