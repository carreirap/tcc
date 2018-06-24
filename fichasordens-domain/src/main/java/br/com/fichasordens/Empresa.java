package br.com.fichasordens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.entities.EmpresaEntity;
import br.com.fichasordens.entities.EnderecoEntity;
import br.com.fichasordens.repository.EmpresaRepository;

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
		
		return converter(entity);
	}

	private Empresa converter(EmpresaEntity entity) {
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
	
	@Transactional
	public Empresa salvarEmpresa(final Empresa empresa) {
		Endereco ender = this.endereco.salvarEndereco(empresa.getEndereco());
		
		EmpresaEntity entity = converterParaEntity(empresa);
		entity.setEndereco(new EnderecoEntity());
		entity.getEndereco().setId(ender.getId());
		entity.setId(ID_EMPRESA);
		this.repository.save(entity);
		return empresa;
	}

	private EmpresaEntity converterParaEntity(final Empresa empresa) {
		EmpresaEntity entity = new EmpresaEntity();
		entity.setCnpj(empresa.getCnpj());
		entity.setEmai(empresa.getEmail());
		entity.setFone(empresa.getFone());
		entity.setNome(empresa.getNome());
		entity.setSite(empresa.getSite());
		return entity;
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
