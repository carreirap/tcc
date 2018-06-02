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
import br.com.fichasordens.Endereco;
import br.com.fichasordens.dto.ClienteDto;

@RestController
@RequestMapping("/cliente")
@EnableResourceServer
public class ClienteController {
	
	@Autowired 
	private Cliente cliente;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvarCliente(@RequestBody final ClienteDto clienteDto) {
		final Cliente cliente = this.converterClienteDtoParaCliente(clienteDto); 
		this.cliente.salvarCliente(cliente);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private Cliente converterClienteDtoParaCliente(final ClienteDto dto) { 
		final Cliente cliente = new Cliente();
		cliente.setFone(dto.getFone());
		cliente.setCnpjCpf(dto.getCnpj());
		cliente.setEmail(dto.getEmail());
		cliente.setNome(dto.getNome());
		cliente.setCelular(dto.getCelular());
		if (dto.getId() == 0) {
			cliente.setId(dto.getId());
		}
		
		cliente.setEndereco(this.converteDtoParaEndereco(dto));
		return cliente;
	}
	
	private Endereco converteDtoParaEndereco(ClienteDto dto) {
		Endereco end = new Endereco();
		end.setBairro(dto.getBairro());
		end.setCep(dto.getCep());
		end.setCidade(dto.getCidade());
		end.setComplemento(dto.getComplemento());
		end.setEstado(dto.getEstado());
		end.setLogradouro(dto.getLogradouro());
		end.setNumero(dto.getNumero());
		return end;
	}
	
}
