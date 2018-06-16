package br.com.fichasordens;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.entities.EmpresaEntity;
import br.com.fichasordens.repository.EmpresaRepository;
import br.com.fichasordens.util.DadosTestes;

public class EmpresaTest {
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private Empresa empresa;
	
	@Mock
	private EmpresaRepository mockEmpresaRepository;
	
	@Mock
	private Endereco endereco;
	
	private static long ID_EMPRESA = 1L;
	
	@Test
	public void test_buscarEmpresa_success() {
		when(this.mockEmpresaRepository.findOne(EmpresaTest.ID_EMPRESA)).thenReturn(DadosTestes.loadEmpresaEntity());
		
		Empresa emp =  this.empresa.buscarEmpresa();
		assertNotNull(emp);
	}
	
	@Test
	public void test_salvarEmpresa_sucess() {
		when(this.mockEmpresaRepository.save(org.mockito.Mockito.any(EmpresaEntity.class))).thenReturn(new EmpresaEntity());
		when(this.endereco.salvarEndereco(org.mockito.Mockito.any(Endereco.class))).thenReturn(DadosTestes.loadEndereco());
		
		Empresa emp = this.empresa.salvarEmpresa(DadosTestes.loadEmpresa());
		
		assertNotNull(emp);
	}
	
	

}
