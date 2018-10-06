package br.com.fichasordens.util;

import br.com.fichasordens.Empresa;
import br.com.fichasordens.dto.EmpresaDto;

public class ConversorEmpresa {
	
	private ConversorEmpresa() {}
	
	public static EmpresaDto convertEmpresaParaDto (final Empresa empresa) {
		final EmpresaDto dto = new EmpresaDto();		
		
		dto.setNome(empresa.getNome());
		dto.setCnpj(empresa.getCnpj());
		dto.setBairro(empresa.getEndereco().getBairro());
		dto.setCep(empresa.getEndereco().getCep());
		dto.setCidade(empresa.getEndereco().getCidade());
		dto.setComplemento(empresa.getEndereco().getComplemento());
		dto.setEmail(empresa.getEmail());
		dto.setEstado(empresa.getEndereco().getEstado());
		dto.setFone(empresa.getFone());
		dto.setLogradouro(empresa.getEndereco().getLogradouro());
		dto.setNumero(empresa.getEndereco().getNumero());
		dto.setSite(empresa.getSite());
		dto.setIdEndereco(empresa.getEndereco().getId());
		return dto;
	}

}
