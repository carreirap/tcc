package br.com.fichasordens.restcontroller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.FichaAtendimentoLanc;
import br.com.fichasordens.Usuario;
import br.com.fichasordens.dto.FichaAtendimentoDto;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;

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
	
//	@RequestMapping(method = RequestMethod.POST,path="/pecaServico")
//	public ResponseEntity salvarItemOrdemServico(@RequestBody final PecaServicoOrdemDto dto) {
//		try {
//			final PecaOutroServico peca = this.converterDtoPecaServico(dto);
//			this.ordemServicoService.gravarPecaServicoOrdem(peca);
//			return new ResponseEntity( HttpStatus.OK);
//		} catch (ExcecaoRetorno e) {
//			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
//		}
//	}
	
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
	
	private FichaAtendimento converterDto(final FichaAtendimentoDto dto) {
		final FichaAtendimento ficha = new FichaAtendimento();
		ficha.setTipoServico(dto.getTipoServico());
		final Cliente cliente = new Cliente();
		cliente.setId(dto.getCliente().getId());
		ficha.setCliente(cliente);
		ficha.setFichaAtendimentoLanc(new ArrayList<FichaAtendimentoLanc>());
		FichaAtendimentoLanc lanc = this.converterDtoLanc(dto.getLancamento());
		ficha.getFichaAtendimentoLanc().add(lanc);
		return ficha;
	}
	
//	private PecaOutroServico converterDtoPecaServico(final PecaServicoOrdemDto dto) {
//		final PecaOutroServico pecaServico = new PecaOutroServico();
//		//		pecaServico.setId(dto.getIdOrdem()...);
//		pecaServico.setDescricao(dto.getDescricao());
//		pecaServico.setQuantidade(dto.getQtde());
//		pecaServico.setValor(dto.getValor());
//		pecaServico.setId(dto.getSequencia());
//		pecaServico.setOrdemServico(new OrdemServico());
//		pecaServico.getOrdemServico().setId(dto.getIdOrdem());
//		return pecaServico;
//	}
	
	private FichaAtendimentoLanc converterDtoLanc(final LancamentoDto dto) {
		final FichaAtendimentoLanc lanc = new FichaAtendimentoLanc();
		lanc.setData(dto.getData());
		lanc.setObservacao(dto.getObservacao());
		lanc.setSituacao(dto.getSituacao());
		lanc.setUsuario(new Usuario());
		lanc.getUsuario().setId(dto.getIdUsuario());
		lanc.setSequencia(dto.getSequencia());
		lanc.setFichaAtendimento(new FichaAtendimento());
		lanc.getFichaAtendimento().setId(dto.getId());
		return lanc;
	}

}
