package br.com.fichasordens.restcontroller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.Lancamento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.ListagemDashboardDto;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.dto.OrdemServicoDto;
import br.com.fichasordens.dto.PecaOutroServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.service.GeradorPdfService;
import br.com.fichasordens.util.ConverterCliente;
import br.com.fichasordens.util.ConverterLancamentoDto;
import br.com.fichasordens.util.ConverterPecaOutroServico;
import br.com.fichasordens.util.DataUtil;
import br.com.fichasordens.util.StatusServicoEnum;
import br.com.fichasordens.util.TipoServicoEnum;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/ordem")
@EnableResourceServer
public class OrdemServicoController {
	
	private static final String ORDEM_DE_SERVICO = "Ordem de Servico";
	@Autowired
	private OrdemServico ordemServico;
	
	@Value("${alerta.servico.parado.situacao}")
	private int qtdDiasAlerta;
	
	@Autowired
	private GeradorPdfService pdfService;
	

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvarOrdemServico(@RequestBody final OrdemServicoDto dto) {
		try {
			OrdemServico ordemServico = this.converterDtoParaOrdemServico(dto);
			ordemServico = this.ordemServico.salvarOrdem(ordemServico);
			dto.setNumeroOrdem(ordemServico.getId());
			return new ResponseEntity<OrdemServicoDto>(dto, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST,path="/pecaServico")
	public ResponseEntity salvarItemOrdemServico(@RequestBody final PecaOutroServicoDto dto) {
		try {
			final PecaOutroServico peca = ConverterPecaOutroServico.converterDtoPecaServico(dto, TipoServicoEnum.ORDEM_SERVICO);
			this.ordemServico.gravarPecaServicoOrdem(peca);
			return new ResponseEntity( HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE,path="/pecaServico")
	public ResponseEntity deletarPecaServicoOrdem(@RequestParam final int id, @RequestParam final int sequencia) {
		this.ordemServico.deletarPecaOutroServico(id, sequencia);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity listaFichas(@RequestParam final String situacao) {
		List<OrdemServico> ordemList = this.ordemServico.listarOrdens(StatusServicoEnum.valueOf(situacao.toUpperCase()));
		List<ListagemDashboardDto> dtoList = new ArrayList<ListagemDashboardDto>();
		ordemList.forEach(a->{
			ListagemDashboardDto dto = new ListagemDashboardDto();
			dto.setId(a.getId());
			dto.setNomeCliente(a.getCliente().getNome());
			dto.setSituacao(situacao);
			dto.setTipoServico(ORDEM_DE_SERVICO);
			for(Lancamento lanc: a.getLancamento()) {
				if (lanc.getSituacao().equals(situacao)) {
					dto.setResponsavel(lanc.getUsuario().getNome());
					dto.setDias((int)DataUtil.calcularDiferencaDiasEntreUmaDataEAgora(lanc.getData()));
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
	public OrdemServicoDto buscarOrdem(@RequestParam final long id) {
		final OrdemServico ordem = this.ordemServico.buscarOrdem(id);
		return this.converterOrdemServicoParaDto(ordem);
	}
	
	@GetMapping(path="/pdf")
	public void buscarPdfOrdem(@RequestParam final long id, HttpServletResponse response) throws Exception {
		
		final OrdemServico ordem = this.ordemServico.buscarOrdem(id);
		ByteArrayOutputStream out = pdfService.generateOrdemServicoPdf(ordem);

		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=ordem-" + id + ".pdf");
		 response.setHeader("Content-Length", String.valueOf(out.size()));
		response.setContentType("application/pdf");

		// Copy the stream to the response's output stream.
		OutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(out.toByteArray());
        responseOutputStream.close();
        out.close();
		response.flushBuffer();
	}
	
	
	private OrdemServico converterDtoParaOrdemServico(final OrdemServicoDto dto) {
		final OrdemServico ent = new OrdemServico();
		ent.setId(dto.getNumeroOrdem());
		ent.setFabricante(dto.getFabricante());
		ent.setDescDefeito(dto.getDescDefeito());
		ent.setDescEquip(dto.getDescEquip());
		//ent.setDescServico(dto.getDescServico());
		ent.setEstadoItensAcomp(dto.getEstadoItensAcomp());
		ent.setModelo(dto.getModelo());
		ent.setSerie(dto.getSerie());
		ent.setTipoServico(dto.getTipoServico());
		final Cliente cliente = new Cliente();
		cliente.setId(dto.getCliente().getId());
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
		//ent.setDescServico(dto.getDescServico());
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
