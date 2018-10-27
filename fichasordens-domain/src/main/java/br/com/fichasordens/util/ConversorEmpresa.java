package br.com.fichasordens.util;

import br.com.fichasordens.Empresa;
import br.com.fichasordens.Endereco;
import br.com.fichasordens.entities.EmpresaEntity;

public class ConversorEmpresa {
	
	private ConversorEmpresa() {}
	
	public static Empresa converterEntity(final EmpresaEntity entity) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(entity.getCnpj());
		empresa.setEmail(entity.getEmai());
		empresa.setEndereco(new Endereco());
		empresa.getEndereco().setBairro(entity.getEndereco().getBairro());
		empresa.getEndereco().setCep(entity.getEndereco().getCep());
		empresa.getEndereco().setCidade(entity.getEndereco().getCidade());
		empresa.getEndereco().setComplemento(entity.getEndereco().getComplemento());
		empresa.getEndereco().setEstado(entity.getEndereco().getEstado());
		empresa.getEndereco().setId(entity.getEndereco().getId());
		empresa.getEndereco().setLogradouro(entity.getEndereco().getLogradouro());
		empresa.getEndereco().setNumero(entity.getEndereco().getNumero());
		
		empresa.setFone(entity.getFone());
		empresa.setNome(entity.getNome());
		empresa.setSite(entity.getSite());
		return empresa;
	}
	
	public static EmpresaEntity converterParaEntity(final Empresa empresa) {
		EmpresaEntity entity = new EmpresaEntity();
		entity.setCnpj(empresa.getCnpj());
		entity.setEmai(empresa.getEmail());
		entity.setFone(empresa.getFone());
		entity.setNome(empresa.getNome());
		entity.setSite(empresa.getSite());
		return entity;
	}

}
