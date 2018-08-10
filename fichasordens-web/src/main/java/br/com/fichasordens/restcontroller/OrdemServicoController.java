package br.com.fichasordens.restcontroller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.OrdemServicoLanc;
import br.com.fichasordens.PecaOutroServico;
import br.com.fichasordens.Usuario;
import br.com.fichasordens.dto.LancamentoDto;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.dto.OrdemServicoDto;
import br.com.fichasordens.dto.PecaOutroServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.util.ConverterPecaOutroServico;

@RestController
@RequestMapping("/ordem")
@EnableResourceServer
public class OrdemServicoController {
	
	@Autowired
	OrdemServico ordemServico;

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvarOrdemServico(@RequestBody final OrdemServicoDto dto) {
		try {
			OrdemServico ordemServico = this.converterDto(dto);
			ordemServico = this.ordemServico.gravarOrdem(ordemServico);
			dto.setNumeroOrdem(ordemServico.getId());
			return new ResponseEntity<OrdemServicoDto>(dto, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST,path="/pecaServico")
	public ResponseEntity salvarItemOrdemServico(@RequestBody final PecaOutroServicoDto dto) {
		try {
			final PecaOutroServico peca = ConverterPecaOutroServico.converterDtoPecaServico(dto);
			this.ordemServico.gravarPecaServicoOrdem(peca);
			return new ResponseEntity( HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST,path="/lancamento")
	public ResponseEntity salvarLancamentoTecnico(@RequestBody final LancamentoDto dto) {
		try {
			OrdemServicoLanc peca = this.converterDtoOrdemServicoLanc(dto);
			this.ordemServico.gravarOrdemServicoLanc(peca);
			return new ResponseEntity( HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE,path="/pecaServico")
	public ResponseEntity deletarOrdemDeServico(@RequestParam final int id, @RequestParam final int sequencia) {
		this.ordemServico.deletarPecaOutroServico(id, sequencia);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	private OrdemServico converterDto(final OrdemServicoDto dto) {
		final OrdemServico ent = new OrdemServico();
		ent.setFabricante(dto.getFabricante());
		ent.setDescDefeito(dto.getDescDefeito());
		ent.setDescEquip(dto.getDescEquip());
		//ent.setDescServico(dto.getDescServico());
		ent.setEstadoItensAcomp(dto.getEstadoItensAcomp());
		ent.setModelo(dto.getModelo());
		ent.setSerie(dto.getSerie());
		ent.setTipoServico(dto.getTipoServico().equals("true") ? "Instalacao" : "Suporte");
		final Cliente cliente = new Cliente();
		cliente.setId(dto.getCliente().getId());
		ent.setCliente(cliente);
		ent.setOrdemServicoLanc(new ArrayList<OrdemServicoLanc>());
		OrdemServicoLanc lanc = this.converterDtoOrdemServicoLanc(dto.getLancamento());
		ent.getOrdemServicoLanc().add(lanc);
		return ent;
	}
	
	private OrdemServicoLanc converterDtoOrdemServicoLanc(final LancamentoDto dto) {
		final OrdemServicoLanc lanc = new OrdemServicoLanc();
		lanc.setData(dto.getData());
		lanc.setObservacao(dto.getObservacao());
		lanc.setSituacao(dto.getSituacao());
		lanc.setUsuario(new Usuario());
		lanc.getUsuario().setId(dto.getIdUsuario());
		lanc.setSequencia(dto.getSequencia());
		lanc.setOrdemServico(new OrdemServico());
		lanc.getOrdemServico().setId(dto.getId());
		return lanc;
	}

}
