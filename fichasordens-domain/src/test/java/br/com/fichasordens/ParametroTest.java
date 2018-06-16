package br.com.fichasordens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Sort;

import br.com.fichasordens.entities.ParametroEntity;
import br.com.fichasordens.repository.ParametroRepository;

public class ParametroTest {
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private Parametro mockParametro;
	
	@Mock
	private ParametroRepository mockParametroRepository;
	
	@Test
	public void test_recuperarParametros_success() {
		List<ParametroEntity> list = loadParametroList();
		when(this.mockParametroRepository.findAll(new Sort(Sort.Direction.ASC, "id"))).thenReturn(list);
		List<Parametro> parametroLista = this.mockParametro.recuperarParametros();
		assertNotNull(parametroLista);
		assertEquals(1, parametroLista.size());
		
	}
	
	@Test
	public void test_salvarParametro_success() {
		ParametroEntity ent = new ParametroEntity();
		ent.setId((byte)1);
		when(this.mockParametroRepository.findOne(ent.getId())).thenReturn(ent);
		when(this.mockParametroRepository.save(ent)).thenReturn(ent);
		
		List<Parametro> lista = Arrays.asList(loadParametro());
		this.mockParametro.salvarParametro(lista);
		
		Mockito.verify(this.mockParametroRepository, Mockito.times(1)).findOne(ent.getId());
		Mockito.verify(this.mockParametroRepository, Mockito.times(1)).save(org.mockito.Mockito.any(ParametroEntity.class));
	}
	
	private List<ParametroEntity> loadParametroList() {
		ParametroEntity p = new ParametroEntity();
		p.setId((byte)1);
		p.setDescricao("teste");
		List<ParametroEntity> list = Arrays.asList(p);
		return list;
	}
	
	private Parametro loadParametro() {
		Parametro p = new Parametro();
		p.setId((byte)1);
		p.setValor(new BigDecimal(10));
		return p;
	}

}
