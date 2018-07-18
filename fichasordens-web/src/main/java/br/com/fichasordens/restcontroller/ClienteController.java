package br.com.fichasordens.restcontroller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.Endereco;
import br.com.fichasordens.dto.ClienteDto;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.entities.ClienteEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;

@RestController
@RequestMapping("/cliente")
@EnableResourceServer
public class ClienteController {
	
	@Autowired 
	private Cliente cliente;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvarCliente(@RequestBody final ClienteDto clienteDto) {
		final Cliente cliente = this.converterClienteDtoParaCliente(clienteDto); 
		try {
			this.cliente.salvarCliente(cliente);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public Page<ClienteDto> pesquisarCliente(@RequestParam(required=false) final String cnpjcpf, @RequestParam(required=false) final String nome, 
			final Pageable page) {
		Cliente cli = new Cliente();
		cli.setCnpjCpf(cnpjcpf);
		cli.setNome(nome);
		Page<ClienteEntity> clientePaged = this.cliente.pesquisarCliente(cli, page);
		int totalElements = (int) clientePaged.getTotalElements();
        return new PageImpl<ClienteDto>(clientePaged.getContent()
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
      
//		List<ClienteDto> dtoList = new ArrayList<>();
		/*	clienteList.forEach(a -> {
				ClienteDto dto = new ClienteDto();
				dto.setNome(a.getNome());
				dto.setId(a.getId());
				dto.setCnpj(a.getCnpjCpf());
				dto.setFone(a.getFone());
				dtoList.add(dto);
		});*/
		
	}
	
	private Cliente converterClienteDtoParaCliente(final ClienteDto dto) { 
		final Cliente cliente = new Cliente();
		cliente.setFone(dto.getFone());
		cliente.setCnpjCpf(dto.getCnpj());
		cliente.setEmail(dto.getEmail());
		cliente.setNome(dto.getNome());
		cliente.setCelular(dto.getCelular());
		if (dto.getId() != 0) {
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
		if (dto.getIdEndereco() != 0) {
			end.setId(dto.getIdEndereco());
		}
		return end;
	}
	
}
