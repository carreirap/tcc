package br.com.fichasordens.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Atendimento;
import br.com.fichasordens.Cliente;
import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.FichaAtendimentoLanc;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.dto.AtendimentoDto;
import br.com.fichasordens.dto.FichaAtendimentoDto;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.ListagemDashboardDto;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.dto.PecaOutroServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.util.ConverterAtendimento;
import br.com.fichasordens.util.ConverterCliente;
import br.com.fichasordens.util.ConverterLancamentoDto;
import br.com.fichasordens.util.ConverterPecaOutroServico;

@RestController
@RequestMapping("/ficha")
@EnableResourceServer
public class FichaAtendimentoController {
	
	@Autowired
	FichaAtendimento fichaAtendimento;

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvarFichaAtendimento(@RequestBody final FichaAtendimentoDto dto) {
		try {
			FichaAtendimento ficha = this.converterDto(dto);
			ficha = this.fichaAtendimento.salvarFicha(ficha);
			dto.setNumeroFicha(ficha.getId());
			dto.getLancamento().setId(ficha.getId());
			return new ResponseEntity<FichaAtendimentoDto>(dto, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST,path="/pecaServico")
	public ResponseEntity gravarPecaServicosalvarItemOrdemServico(@RequestBody final PecaOutroServicoDto dto) {
		//try {
			final PecaOutroServico peca = ConverterPecaOutroServico.converterDtoPecaServico(dto);
			this.fichaAtendimento.gravarPecaServicoFicha(peca);
			//this.ordemServicoService.gravarPecaServicoOrdem(peca);
			return new ResponseEntity( HttpStatus.OK);
//		} catch (ExcecaoRetorno e) {
//			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
//		}
	}
	
	@RequestMapping(method = RequestMethod.POST,path="/atendimento")
	public ResponseEntity gravarPecaServicosalvarItemOrdemServico(@RequestBody final AtendimentoDto dto) {
		//try {
			final Atendimento atend = ConverterAtendimento.converterAtendimentoDto(dto);
			this.fichaAtendimento.gravarAtendimento(atend);
			//this.ordemServicoService.gravarPecaServicoOrdem(peca);
			return new ResponseEntity( HttpStatus.OK);
//		} catch (ExcecaoRetorno e) {
//			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
//		}
	}
		
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity listaFichas(@RequestParam final String situacao) {
		List<FichaAtendimento> fichaList = this.fichaAtendimento.listarFichas(situacao);
		List<ListagemDashboardDto> dtoList = new ArrayList<ListagemDashboardDto>();
		fichaList.forEach(a->{
			ListagemDashboardDto dto = new ListagemDashboardDto();
			dto.setId(a.getId());
			dto.setNomeCliente(a.getCliente().getNome());
			dto.setSituacao(situacao);
			dto.setTipoServico("Ficha de Atendimento");
			for(FichaAtendimentoLanc lanc:  a.getFichaAtendimentoLancList()) {
				if (lanc.getSituacao().equals(situacao)) {
					dto.setResponsavel(lanc.getUsuario().getNome());
				}
				if (lanc.getSituacao().equals("Aberto")) {
					dto.setDataAbertura(lanc.getData());
				}
			}
			dtoList.add(dto);
		});
		
		return new ResponseEntity<>(dtoList,HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.GET,path="/buscar")
	public FichaAtendimentoDto buscarFicha(@RequestParam final long id) {
		FichaAtendimento ficha = this.fichaAtendimento.buscarFicha(id);
		return this.converterParaDto(ficha);
	}
	
	
	
//	@RequestMapping(method = RequestMethod.POST,path="/lancamento")
//	public ResponseEntity salvarLancamentoTecnico(@RequestBody final OrdemServicoLancDto dto) {
//		try {
//			OrdemServicoLanc peca = this.converterDtoOrdemServicoLanc(dto);
//			//this.ordemServicoService.gravarOrdemServicoLanc(peca);
//			return new ResponseEntity( HttpStatus.OK);
//		} catch (ExcecaoRetorno e) {
//			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
//		}
//	}
	
	private FichaAtendimentoDto converterParaDto(final FichaAtendimento ficha) {
		final FichaAtendimentoDto dto = new FichaAtendimentoDto();
		dto.setTipoServico(ficha.getTipoServico());
		dto.setNumeroFicha(ficha.getId());
		dto.setCliente(ConverterCliente.converterClienteParaDto(ficha.getCliente()));

		dto.setLancamentoLst(new ArrayList<LancamentoDto>());
		for(FichaAtendimentoLanc lanc: ficha.getFichaAtendimentoLancList()) {
			final LancamentoDto lancDto = ConverterLancamentoDto.converterLancamentoDto(lanc);
			dto.getLancamentoLst().add(lancDto);
		}
		
		dto.setPecaOutroServicoDto(new ArrayList<PecaOutroServicoDto>());
		for(PecaOutroServico p: ficha.getPecaOutroServicoList()) {
			dto.getPecaOutroServicoDto().add(ConverterPecaOutroServico.converterPecaOutroServicoParaDto(p));
		}
		return dto;
	}
	
	
	private FichaAtendimento converterDto(final FichaAtendimentoDto dto) {
		final FichaAtendimento ficha = new FichaAtendimento();
		ficha.setTipoServico(dto.getTipoServico());
		final Cliente cliente = new Cliente();
		cliente.setId(dto.getCliente().getId());
		ficha.setCliente(cliente);
		ficha.setFichaAtendimentoLancList(new ArrayList<FichaAtendimentoLanc>());
		FichaAtendimentoLanc lanc = ConverterLancamentoDto.converterDtoLanc(dto.getLancamento());
		ficha.getFichaAtendimentoLancList().add(lanc);
		return ficha;
	}
}
