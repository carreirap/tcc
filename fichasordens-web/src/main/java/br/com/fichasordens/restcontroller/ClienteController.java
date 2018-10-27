package br.com.fichasordens.restcontroller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.dto.ClienteDto;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.web.util.ConverterCliente;

@RestController
@RequestMapping("/cliente")
@EnableResourceServer
public class ClienteController {
	
	@Autowired 
	private Cliente cliente;

	
	@PostMapping
	public ResponseEntity salvarCliente(@RequestBody final ClienteDto clienteDto) {
		final Cliente cli = ConverterCliente.converterClienteDtoParaCliente(clienteDto); 
		try {
			this.cliente.salvarCliente(cli);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@GetMapping
	public Page<ClienteDto> pesquisarCliente(@RequestParam(required=false) final String cnpjcpf, @RequestParam(required=false) final String nome, 
			final Pageable page) {
		Cliente cli = new Cliente();
		cli.setCnpjCpf(cnpjcpf);
		cli.setNome(nome);
		Page<ClienteEntity> clientePaged = this.cliente.pesquisarCliente(cli, page);
		int totalElements = (int) clientePaged.getTotalElements();
        return new PageImpl<>(clientePaged.getContent()
                .stream()
                .map(e -> new ClienteDto(
                        e.getId(),
                        e.getNome(),
                        e.getCnpjCpf(),
                        e.getEmail(),
                        e.getEndereco().getId(),
                        e.getEndereco().getLogradouro(),
                        e.getEndereco().getBairro(),
                        e.getEndereco().getCidade(),
                        e.getEndereco().getEstado(),
                        e.getFone(),
                        e.getCelular(),
                        e.getEndereco().getNumero(),
                        e.getEndereco().getCep(),
                        e.getEndereco().getComplemento()
                        ))
                .collect(Collectors.toList()), page, totalElements);
		
	}
	
	
	
	
	
}
