package br.com.fichasordens.util;

import br.com.fichasordens.Cliente;
import br.com.fichasordens.dto.ClienteDto;

public class ConverterCliente {
	
	private ConverterCliente() {}
	
	public static ClienteDto converterClienteParaDto(final Cliente cliente) {
		final ClienteDto clienteDto = new ClienteDto();
		clienteDto.setId(cliente.getId());
		clienteDto.setNome(cliente.getNome());
		clienteDto.setCnpj(cliente.getCnpjCpf());
		clienteDto.setCelular(cliente.getCelular());
		clienteDto.setFone(cliente.getFone());
		return clienteDto;
	}
	
	public static Cliente converterClienteDtoParaCliente(final ClienteDto dto) { 
		final Cliente cli = new Cliente();
		cli.setFone(dto.getFone());
		cli.setCnpjCpf(dto.getCnpj());
		cli.setEmail(dto.getEmail());
		cli.setNome(dto.getNome());
		cli.setCelular(dto.getCelular());
		if (dto.getId() != 0) {
			cli.setId(dto.getId());
		}
		
		cli.setEndereco(ConverterEndereco.converteDtoParaEndereco(dto));
		return cli;
	}

}
