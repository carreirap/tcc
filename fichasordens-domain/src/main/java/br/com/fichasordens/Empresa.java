package br.com.fichasordens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.EmpresaEntity;
import br.com.fichasordens.entities.EnderecoEntity;
import br.com.fichasordens.repository.EmpresaRepository;
import br.com.fichasordens.util.ConversorEmpresa;

@Component
public class Empresa extends Pessoa{
	
	private static final long ID_EMPRESA = 1L;
	
	private long id;
	private String cnpj;
	private String site;
	
	@Autowired private Endereco endereco;
	
	@Autowired 
	private EmpresaRepository repository;
	
	@Transactional
	public Empresa buscarEmpresa() {
		EmpresaEntity entity = this.repository.findOne(Empresa.ID_EMPRESA);
		return ConversorEmpresa.converter(entity);
	}
	
	@Transactional
	public Empresa salvarEmpresa(final Empresa empresa) {
		Endereco ender = this.endereco.salvarEndereco(empresa.getEndereco());
		
		EmpresaEntity entity = ConversorEmpresa.converterParaEntity(empresa);
		entity.setEndereco(new EnderecoEntity());
		entity.getEndereco().setId(ender.getId());
		entity.setId(ID_EMPRESA);
		this.repository.save(entity);
		return empresa;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public Endereco getEndereco() {
		return endereco;
	}
	
	@Override
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
}
