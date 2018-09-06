package br.com.fichasordens.util;

import br.com.fichasordens.Endereco;
import br.com.fichasordens.dto.ClienteDto;

public class ConverterEndereco {
	
	private ConverterEndereco() {}
	
	public static Endereco converteDtoParaEndereco(ClienteDto dto) {
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
