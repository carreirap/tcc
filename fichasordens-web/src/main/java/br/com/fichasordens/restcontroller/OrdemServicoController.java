package br.com.fichasordens.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.OrdemServicoInterface;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.dto.OrdemServicoDto;
import br.com.fichasordens.exception.ExcecaoRetorno;

@RestController
@RequestMapping("/ordem")
@EnableResourceServer
public class OrdemServicoController {
	
	@Autowired
	OrdemServicoInterface ordemServicoService;

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvarCliente(@RequestBody final OrdemServicoDto dto) {
		System.out.println(dto);
		try {
			OrdemServico ordemServico = this.converterParaEntity(dto);
			ordemServico = this.ordemServicoService.gravarOrdem(ordemServico);
			dto.setNumeroOrdem(ordemServico.getId());
			return new ResponseEntity<OrdemServicoDto>(dto, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	private OrdemServico converterParaEntity(final OrdemServicoDto dto) {
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
		return ent;
	}

}
