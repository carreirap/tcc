package br.com.fichasordens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.entities.OrdemServicoLancEntity;
import br.com.fichasordens.entities.OrdemServicoLancId;
import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntityId;
import br.com.fichasordens.entities.UsuarioEntity;
import br.com.fichasordens.exception.ExcecaoRetorno;
import br.com.fichasordens.repository.OrdemServicoLancRepository;
import br.com.fichasordens.repository.OrdemServicoRepository;
import br.com.fichasordens.repository.PecaServicoOrdemRepository;
import br.com.fichasordens.util.DadosMockEntity;

public class OrdemServicoTest {
	
	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private OrdemServico mockOrdemServico;
	
	@Mock
	private OrdemServicoRepository ordemServicoRepository;

	@Mock
	private OrdemServicoLancRepository ordemServicoLancRepository;
	
	@Mock
	private PecaServicoOrdemRepository pecaServicoOrdemRepository;
	
	@Mock
	private Usuario usuario;
	
	@Test
	public void teste_salvarOrdem_success() throws ExcecaoRetorno {
		OrdemServicoEntity ent = createOrdemServicoEntity();
		OrdemServicoLancEntity lancEnt = createOrdemServicoLancEntity();
		when(this.ordemServicoRepository.save(org.mockito.Mockito.any(OrdemServicoEntity.class))).thenReturn(ent);
		when(ordemServicoLancRepository.findByOrdemServicoIdAndAtualSituacao(ent.getId(), true)).thenReturn(lancEnt);
		OrdemServico ordemServico = this.createOrdemServicoMock();
		this.mockOrdemServico.salvarOrdem(ordemServico);
		
		Mockito.verify(this.ordemServicoLancRepository, Mockito.times(2)).save(org.mockito.Mockito.any(OrdemServicoLancEntity.class));
		Mockito.verify(this.ordemServicoRepository, Mockito.times(1)).save(org.mockito.Mockito.any(OrdemServicoEntity.class));
	}
	
	@Test
	public void teste_salvarOrdem_V2_success() throws ExcecaoRetorno {
		OrdemServicoEntity ent = createOrdemServicoEntity();
		when(this.ordemServicoRepository.save(org.mockito.Mockito.any(OrdemServicoEntity.class))).thenReturn(ent);
		when(ordemServicoLancRepository.findByOrdemServicoIdAndAtualSituacao(ent.getId(), true)).thenReturn(null);
		OrdemServico ordemServico = this.createOrdemServicoMock();
		this.mockOrdemServico.salvarOrdem(ordemServico);
		
		Mockito.verify(this.ordemServicoLancRepository, Mockito.times(1)).save(org.mockito.Mockito.any(OrdemServicoLancEntity.class));
		Mockito.verify(this.ordemServicoRepository, Mockito.times(1)).save(org.mockito.Mockito.any(OrdemServicoEntity.class));
		
	}
	
	
	@Test(expected=ExcecaoRetorno.class)
	public void teste_salvarOrdem_exception() throws ExcecaoRetorno {
		try {
		when(this.ordemServicoRepository.save(org.mockito.Mockito.any(OrdemServicoEntity.class))).thenReturn(null);
		} catch (Exception e) {}
		OrdemServico ordemServico = this.createOrdemServicoMock();
		this.mockOrdemServico.salvarOrdem(ordemServico);
	}
	
	@Test
	public void teste_buscarOrdem_success() {
		OrdemServicoEntity ent = createOrdemServicoEntity();
		when(this.ordemServicoRepository.findOne(199L)).thenReturn(ent);
		OrdemServico result = this.mockOrdemServico.buscarOrdem(199L);
		
		assertNotNull(result);
		assertEquals(ent.getDescDefeito(), result.getDescDefeito());
		
	}
	
	@Test
	public void teste_gravarPecaServicoOrdem_success() throws ExcecaoRetorno {
		PecaOutroServico peca = createPecaOutroServico();
		when(pecaServicoOrdemRepository.save(org.mockito.Mockito.any(PecaServicoOrdemEntity.class))).thenReturn(null);
		this.mockOrdemServico.gravarPecaServicoOrdem(peca);
		
		Mockito.verify(this.pecaServicoOrdemRepository, Mockito.times(1)).save(org.mockito.Mockito.any(PecaServicoOrdemEntity.class));
		
	}
	
	@Test(expected=ExcecaoRetorno.class)
	public void teste_gravarPecaServicoOrdem_Exception() throws ExcecaoRetorno {
		PecaOutroServico peca = createPecaOutroServico();
		when(pecaServicoOrdemRepository.save(org.mockito.Mockito.any(PecaServicoOrdemEntity.class))).thenThrow(new RuntimeException("DB exception"));
		this.mockOrdemServico.gravarPecaServicoOrdem(peca);
	}
	
	@Test
	public void teste_listarOrdens_sucess() {
		when(this.ordemServicoRepository.findAllOrdensByStatus("Aguardando")).thenReturn(Arrays.asList(createOrdemServicoEntity()));
		List<?> lst = this.mockOrdemServico.listarOrdens("Aguardando");
		
		assertEquals(1, lst.size());
	}
	
	@Test
	public void teste_buscarOrdensDeServicoPorSituacao_sucess() {
		Date inicio = new Date();
		Date fim = new Date();
		when(this.ordemServicoRepository.findAllOrdensByStatusAndDataBetween("Aguardando", inicio, fim)).thenReturn(Arrays.asList(createOrdemServicoEntity()));
		List<?> lst = this.mockOrdemServico.buscarOrdensDeServicoPorSituacao("Aguardando", inicio, fim);
		
		assertEquals(1, lst.size());
	}
	
	
	@Test
	public void teste_deletarPecaOutroServico_success() {
		doNothing().when(pecaServicoOrdemRepository).delete(org.mockito.Mockito.any(PecaServicoOrdemEntity.class));
		this.mockOrdemServico.deletarPecaOutroServico(199, 1);
		
		Mockito.verify(this.pecaServicoOrdemRepository, Mockito.times(1)).delete(org.mockito.Mockito.any(PecaServicoOrdemEntity.class));
	}
	
	/*@Test(expected=ExcecaoRetorno.class)
	public void teste_gravarOrdemServicoLanc_success() throws ExcecaoRetorno {
		OrdemServicoLancEntity lanc = createOrdemServicoLancEntity();
		UsuarioEntity userEnt = DadosMockEntity.createUsuarioEntity();
		
		when(ordemServicoLancRepository.save(lanc)).thenReturn(lanc);
		when(usuario.listarUsuario(userEnt.getUsuario())).thenReturn(Arrays.asList(UsuarioTest.createUsuario()));
		this.mockOrdemServico.gravarOrdemServicoLanc(createOrdemServicoLanc());
		
		Mockito.verify(this.ordemServicoLancRepository, Mockito.times(1)).save(org.mockito.Mockito.any(OrdemServicoLancEntity.class));
	}*/
	
	private OrdemServico createOrdemServicoMock() {
		OrdemServico ordem = new OrdemServico();
		ordem.setCliente(ClienteTest.loadCliente());
		ordem.setDescDefeito("Teste defeito");
		ordem.setDescEquip("Notebook Dell Vostro");
		ordem.setDescServico("Nao liga a tela");
		ordem.setEstadoItensAcomp("Riscado tampa / Acompanha a fonte");
		ordem.setFabricante("Dell");
		ordem.setId(199);
		ordem.setModelo("Vostro");
		ordem.setSerie("111111");
		ordem.setTipoServico("Manutencao");
		ordem.setOrdemServicoLanc(Arrays.asList(createOrdemServicoLanc()));
		return ordem;
	}
	
	private OrdemServicoEntity createOrdemServicoEntity() {
		OrdemServicoEntity ent = new OrdemServicoEntity();
		ent.setCliente(DadosMockEntity.createClienteEntity());
		ent.setDescDefeito("Teste defeito");
		ent.setDescEquip("Notebook Dell Vostro");
		ent.setDescServico("Nao liga a tela");
		ent.setEstadoItensAcomp("Riscado tampa / Acompanha a fonte");
		ent.setFrabricante("Dell");
		ent.setId(199);
		ent.setModelo("Vostro");
		ent.setSerie("111111");
		ent.setTipoServico("Manutencao");
		ent.setOrdemServicoLancs(Sets.newSet(createOrdemServicoLancEntity()));
		ent.setPecaServicoOrdems(Sets.newSet(createPecaServicoOrdemEntity()));
		return ent;
	}
	
	private OrdemServicoLanc createOrdemServicoLanc() {
		OrdemServicoLanc lanc = new OrdemServicoLanc();
		lanc.setData(new Date());
		lanc.setObservacao("Teste");
		lanc.setSituacao("Aberto");
		lanc.setSequencia(1);
		lanc.setUsuario(UsuarioTest.createUsuario());
		lanc.setOrdemServico(new OrdemServico());
		lanc.getOrdemServico().setId(199);
		
		return lanc;
	}
	
	private OrdemServicoLancEntity createOrdemServicoLancEntity() {
		OrdemServicoLancEntity lanc = new OrdemServicoLancEntity();
		lanc.setData(new Date());
		lanc.setObservacao("Teste");
		lanc.setSituacao("Aberto");
		lanc.setId(new OrdemServicoLancId());
		lanc.getId().setSequencia(0);
		lanc.getId().setOrdemServicoId(199);
		lanc.setUsuario(new UsuarioEntity());
		lanc.getUsuario().setId(10);
		
		return lanc;
	}
	
	private PecaOutroServico createPecaOutroServico() {
		PecaOutroServico peca = new PecaOutroServico();
		peca.setDescricao("Teste");
		peca.setId(1);
		peca.setQuantidade(10);
		peca.setValor(new BigDecimal(10));
		peca.setOrdemServico(new OrdemServico());
		peca.getOrdemServico().setId(199);
		
		return peca;
	}
	
	private PecaServicoOrdemEntity createPecaServicoOrdemEntity() {
		PecaServicoOrdemEntity peca = new PecaServicoOrdemEntity();
		peca.setDescricao("Teste");
		peca.setQuantidade(10L);
		peca.setValor(new BigDecimal(10));
		peca.setId(new PecaServicoOrdemEntityId());
		peca.getId().setSequencia(0);
		peca.getId().setOrdemServicoId(199);
		peca.setOrdemServico(new OrdemServicoEntity());
		peca.getOrdemServico().setId(199);
		
		return peca;
	}

}
