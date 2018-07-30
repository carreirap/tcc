package br.com.fichasordens.util;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.dto.ClienteDto;

public class ConverterCliente {
	
	public static ClienteDto converterClienteParaDto(final Cliente cliente) {
		final ClienteDto clienteDto = new ClienteDto();
		clienteDto.setId(cliente.getId());
		clienteDto.setNome(cliente.getNome());
		clienteDto.setCnpj(cliente.getCnpjCpf());
		clienteDto.setCelular(cliente.getCelular());
		clienteDto.setFone(cliente.getFone());
		return clienteDto;
	}

}
