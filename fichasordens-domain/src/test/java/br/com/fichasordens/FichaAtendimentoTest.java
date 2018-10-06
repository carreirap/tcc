package br.com.fichasordens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import br.com.fichasordens.entities.AtendimentoFichaEntity;
import br.com.fichasordens.entities.AtendimentoFichaId;
import br.com.fichasordens.entities.FichaAtendLancEntity;
import br.com.fichasordens.entities.FichaAtendLancId;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.UsuarioEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.AtendimentoFichaRepository;
import br.com.fichasordens.repository.FichaAtendLancRepository;
import br.com.fichasordens.repository.FichaAtendimentoRepository;
import br.com.fichasordens.util.DadosMockEntity;
import br.com.fichasordens.util.StatusServicoEnum;

public class FichaAtendimentoTest {
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private FichaAtendimento ficha;
	
	@Mock
	private FichaAtendimentoRepository mockFichaRepository;
	
	@Mock 
	FichaAtendLancRepository fichaAtendLancRepository;
	
	
	
	@Test
	public void test_salvarFicha_success() throws ExcecaoRetorno {
		final FichaAtendimento fichaAtendimento = createFichaAtendimento();
		FichaAtendimentoEntity ent = new FichaAtendimentoEntity();
		ent.setId(299);
		FichaAtendLancEntity lanc = new FichaAtendLancEntity();
		FichaAtendLancEntity lancA = new FichaAtendLancEntity();
		
		when(this.mockFichaRepository.save(org.mockito.Mockito.any(FichaAtendimentoEntity.class))).thenReturn(ent);
		when(this.fichaAtendLancRepository.findByFichaAtendimentoIdAndAtualSituacao(fichaAtendimento.getId(), true)).thenReturn(lancA);
		when(this.fichaAtendLancRepository.save(lancA)).thenReturn(lanc);
		when(this.fichaAtendLancRepository.save(lanc)).thenReturn(lanc);
		
		ficha.salvarFicha(fichaAtendimento);
		
		Mockito.verify(this.mockFichaRepository, Mockito.times(1)).save(org.mockito.Mockito.any(FichaAtendimentoEntity.class));
	}
	
	@Test(expected=ExcecaoRetorno.class)
	public void test_salvarFicha_exception() throws ExcecaoRetorno {
		final FichaAtendimento fichaAtendimento = createFichaAtendimento();
		try {
			when(this.mockFichaRepository.save(org.mockito.Mockito.any(FichaAtendimentoEntity.class))).thenThrow(new RuntimeException("DB exception"));
		} finally {		
			ficha.salvarFicha(fichaAtendimento);
		}
		
	}
	
	
	
	/*@Test
	public void test_buscarFichaDeAtendimento_success() {
		when(this.mockFichaRepository.findAllFichas()).thenReturn(new ArrayList());
		List lst = ficha.buscarFichasDeAtendimento();
		assertNotNull(lst);
	}*/
	
	
	
	@Test 
	public void test_listaFichas_success() {
		FichaAtendimentoEntity ent = createFichaAtendimentoEntity();
		ent.setPecaServicoFichas(Sets.newSet(PecaOutroServicoTest.createPecaServicoFichaEntity()));
		ent.setAtendimentoFichas(Sets.newSet(createAtendimentoFichaEntity()));
		FichaAtendLancEntity lancEnt = createFichaAtendLancEntity();
		ent.setFichaAtendLancs(Sets.newSet(lancEnt));
		when(this.ficha.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.ABERTO)).thenReturn(Arrays.asList(ent));
		when(this.fichaAtendLancRepository.findBySituacaoAndFichaAtendimentoIdAndAtualSituacao(StatusServicoEnum.ABERTO.getValue(),ent.getId(), true)).thenReturn(lancEnt);
		List<?> lst = this.ficha.listarFichas(StatusServicoEnum.ABERTO);
		
		assertEquals(1, lst.size());
	}
	
	
	
	@Test
	public void test_buscarFichaAtendimentoPorSituacao_success() {
		Date inicio = new Date();
		Date fim = new Date();
		when(this.mockFichaRepository.findAllFichaByStatusAndDataInicioAndDataFim(StatusServicoEnum.AGUARDANDO.getValue(), inicio, fim)).thenReturn(Arrays.asList(new FichaAtendimentoEntity()));
		List<?> lst = this.ficha.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.AGUARDANDO, inicio, fim);
		
		assertEquals(1, lst.size());
	}
	
	@Test
	public void buscarFichaID_success() {
		
		when(this.mockFichaRepository.findOne(199L)).thenReturn(this.createFichaAtendimentoEntity());
		FichaAtendimento fic = this.ficha.buscarFichaAtendimento(199);
		
		assertNotNull(fic);
	}
	
	
	
	
	
	public static FichaAtendimento createFichaAtendimento() {
				
		final FichaAtendimento fichaA = new FichaAtendimento();
		fichaA.setTipoServico("Test");
		fichaA.setId(199);
		final Cliente cliente = new Cliente();
		cliente.setId(200);
		fichaA.setCliente(cliente);
		fichaA.setFichaAtendimentoLancList(new ArrayList<Lancamento>());
		Lancamento lanc = createFichaAtendimenLanctoMock();
		fichaA.getFichaAtendimentoLancList().add(lanc);
		return fichaA;
	}
	
	private static Lancamento createFichaAtendimenLanctoMock() {
		final Lancamento lanc = new Lancamento();
		lanc.setData(new Date());
		lanc.setObservacao("Test");
		lanc.setSituacao("Aberto");
		lanc.setUsuario(new Usuario());
		lanc.getUsuario().setId(7);
		lanc.setSequencia(1);
		lanc.setFichaAtendimento(new FichaAtendimento());
		lanc.getFichaAtendimento().setId(199);
		return lanc;
	}
	
	
	
	private static AtendimentoFichaEntity createAtendimentoFichaEntity() {
		AtendimentoFichaEntity atendEnt = new AtendimentoFichaEntity();
		atendEnt.setDate(new Date());
		atendEnt.setDescricao("Test");
		atendEnt.setDuracao(new BigDecimal(1.6));
		atendEnt.setValor(new BigDecimal(10));
		atendEnt.setId(new AtendimentoFichaId());
		atendEnt.getId().setSequencia(1);
		atendEnt.getId().setFichaAtendimentoId(199);
		return atendEnt;
	}
	
	public static FichaAtendimentoEntity createFichaAtendimentoEntity() {
		FichaAtendimentoEntity ent = new FichaAtendimentoEntity();
		ent.setId(199);
		ent.setTipoServico("Instalacao");
		ent.setCliente(DadosMockEntity.createClienteEntity());
		ent.setFichaAtendLancs(Sets.newSet(createFichaAtendLancEntity()));
		ent.setAtendimentoFichas(Sets.newSet(createAtendimentoFichaEntity()));
		ent.setPecaServicoFichas(Sets.newSet(PecaOutroServicoTest.createPecaServicoFichaEntity()));
		return ent;
	}
	
	private static FichaAtendLancEntity createFichaAtendLancEntity() {
		FichaAtendLancEntity lanc = new FichaAtendLancEntity();
		UsuarioEntity userEnt = new UsuarioEntity();
		userEnt.setId(1);
		lanc.setUsuario(userEnt);
		lanc.setId(new FichaAtendLancId());
		lanc.getId().setSequencia(1);
		lanc.getId().setFichaAtendimentoId(199);
		lanc.setData(new Date());
		lanc.setAtualSituacao(true);
		return lanc;
	}
}
