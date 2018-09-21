package br.com.fichasordens.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.entities.FichaAtendLancEntity;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.entities.OrdemServicoLancEntity;
import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.repository.FichaAtendimentoRepository;
import br.com.fichasordens.repository.OrdemServicoRepository;
import br.com.fichasordens.service.HistoricoService;
import br.com.fichasordens.util.CustomPage;
import br.com.fichasordens.util.ResultadoPesquisaDto;

@Service
public class HistoricoServiceImpl implements HistoricoService {
	
	private static final String TODAS = "Todas";
	
	@Autowired
	private FichaAtendimentoRepository repository;
	
	@Autowired
	private OrdemServicoRepository ordemRepository;
	
	@Autowired
	OrdemServico ordemServico;
	
	@Transactional
	@Override
	public Page<ResultadoPesquisaDto> pesquisar(final String tipo, final long numero, final String cnpjcpf,
			 final String situacao, final Date inicio, final Date fim, final Pageable pageable) {
		if (tipo.equals("Ficha")) {
			return this.pesquisarFichaPelosParametros(numero, cnpjcpf, situacao, inicio, fim, pageable);
		} else {
			return this.pesquisarOrdemPelosParametros(numero, cnpjcpf, situacao, inicio, fim, pageable);
		}
	}

	private Page<ResultadoPesquisaDto> pesquisarTodasSituacaoOrdem(final Date inicio, final Date fim, final Pageable pageable) {
		Page<OrdemServicoEntity> paged = null;
		if (inicio == null)
			paged = this.ordemRepository.findAllOrdens(pageable);
		else 
			paged = this.ordemRepository.findAllOrdensByDatas(inicio, fim, pageable);
		final List<ResultadoPesquisaDto> lst = this.converterListaDeOrdensParaDto(paged.getContent());
		final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<>(lst, pageable, lst.size());
		pages.setTotalPages(paged.getTotalPages());
		
		return pages;
	}

	private Page<ResultadoPesquisaDto> pesquisarCnpfCpfOrdem(final String cnpjcpf, final String situacao, final Date inicio, final Date fim,		
			final Pageable pageable) {
		Page<OrdemServicoEntity> paged = null;
		if (situacao.equals(TODAS) && inicio == null)
			paged = this.ordemRepository.findAllOrdensByCnpfcpf(cnpjcpf, pageable);
		else if (!situacao.equals(TODAS) && inicio == null)
			paged = this.ordemRepository.findAllOrdensByCnpfcpfAndSituacao(cnpjcpf, situacao, pageable);
		else if (situacao.equals(TODAS) && inicio != null)
			paged = this.ordemRepository.findAllOrdensByCnpfcpfAndDatas(cnpjcpf, inicio, fim, pageable);
		else	
			paged = this.ordemRepository.findAllOrdensByCnpfcpfAndSituacaoAndDatas(cnpjcpf, situacao, inicio, fim, pageable);
		
		/*
		if (situacao.equals(TODAS) && inicio == null)
			paged = this.repository.findAllFichaByCnpfcpf(cnpjcpf, pageable);
		else if (situacao.equals(TODAS) && inicio != null)
			paged = this.repository.findAllFichaByCnpfcpfAndDatas(cnpjcpf, inicio, fim, pageable);
		else if (!situacao.equals(TODAS) && inicio == null) {
			paged = this.repository.findAllFichaByCnpfcpfAndSituacao(cnpjcpf, situacao, pageable);
		} else {
			paged = this.repository.findAllFichaByCnpfcpfAndSituacaoAndDatas(cnpjcpf, situacao, inicio, fim, pageable);
		}
		 */
		final List<ResultadoPesquisaDto> lst = this.converterListaDeOrdensParaDto(paged.getContent());
		final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<>(lst, pageable, lst.size());
		pages.setTotalPages(paged.getTotalPages());
		
		return pages;
	}
	
	public Page<ResultadoPesquisaDto> pesquisarFichaPelosParametros(final long numero, final String cnpjcpf,
			final String situacao, final Date inicio, final Date fim, final Pageable pageable) {
		Page<ResultadoPesquisaDto> paged = null;
		if (numero != 0 ) {
			paged = pesquisarNumeroFicha(numero, pageable);
		} else if ((cnpjcpf != null && !cnpjcpf.equals(""))) {
			paged = pesquisarCnpfCpfFicha(cnpjcpf, situacao, inicio, fim, pageable);
		} else if (situacao.equals(TODAS) && numero == 0 && inicio == null) {
			paged = pesquisarTodasSituacaoFicha(pageable);
		} else if (cnpjcpf == null || cnpjcpf.equals("")) {
			paged = pesquisarPorSituacaoFicha(situacao, inicio, fim,  pageable);
		}
		return paged;
	}
	
	public Page<ResultadoPesquisaDto> pesquisarOrdemPelosParametros(final long numero, final String cnpjcpf,
			final String situacao, final Date inicio, final Date fim, final Pageable pageable) {
		Page<ResultadoPesquisaDto> paged = null;
		if (numero != 0 ) {
			paged = pesquisarNumeroOrdem(numero, pageable);
		} else if ((cnpjcpf != null && !cnpjcpf.equals(""))) {
			paged = pesquisarCnpfCpfOrdem(cnpjcpf, situacao, inicio, fim, pageable);
		}  else if (situacao.equals(TODAS) && numero == 0 && inicio == null) {
			paged = pesquisarTodasSituacaoOrdem(inicio, fim, pageable);
		} else if (cnpjcpf == null || cnpjcpf.equals("")) {
			paged = pesquisarPorSituacaoOrdem(situacao, inicio, fim,  pageable);
		}
		return paged;
	}


	private Page<ResultadoPesquisaDto> pesquisarNumeroOrdem(final long numero, final Pageable pageable) {
		final OrdemServicoEntity ent = this.ordemRepository.findOne(numero); 
		final List<ResultadoPesquisaDto> lst = this.converterListaDeOrdensParaDto(Arrays.asList(ent));
		final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<>(lst, pageable, lst.size());
		pages.setTotalPages(1);
		
		return pages;
	}

	private Page<ResultadoPesquisaDto> pesquisarPorSituacaoFicha(final String situacao, final Date inicio, final Date fim, final Pageable pageable) {
		Page<FichaAtendimentoEntity> paged = null;
		if (inicio == null) 
			paged = this.repository.findAllFichaByStatus(situacao, pageable);
		else if (!situacao.equals("Todas")) 
			paged = this.repository.findAllFichaBySituacaoAndDatas(situacao, inicio, fim, pageable);
		else
			paged = this.repository.findAllFichaByDatas(inicio, fim, pageable);
		final List<ResultadoPesquisaDto> lst = this.converterListaDeFichasParaDto(paged.getContent());
		final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<>(lst, pageable, lst.size());
		pages.setTotalPages(paged.getTotalPages());
		
		return pages;
	}

	private Page<ResultadoPesquisaDto> pesquisarPorSituacaoOrdem(final String situacao, final Date inicio, final Date fim, final Pageable pageable) {
		Page<OrdemServicoEntity> paged = null;
		if (inicio == null)
			paged = this.ordemRepository.findAllOrdensByStatus(situacao, pageable);
		else
			paged = this.ordemRepository.findAllOrdensByStatusAndDataBetween(situacao, inicio, fim, pageable);
		final List<ResultadoPesquisaDto> lst = this.converterListaDeOrdensParaDto(paged.getContent());
		final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<>(lst, pageable, lst.size());
		pages.setTotalPages(paged.getTotalPages());
		
		return pages;
	}

	private Page<ResultadoPesquisaDto> pesquisarTodasSituacaoFicha(final Pageable pageable) {
		final Page<FichaAtendimentoEntity> paged = this.repository.findAllFichas(pageable); 
		final List<ResultadoPesquisaDto> lst = this.converterListaDeFichasParaDto(paged.getContent());
		final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<>(lst, pageable, lst.size());
		pages.setTotalPages(paged.getTotalPages());
		
		return pages;
	}

	private Page<ResultadoPesquisaDto> pesquisarCnpfCpfFicha(final String cnpjcpf, final String situacao,
			final Date inicio, final Date fim, final Pageable pageable) {
		Page<FichaAtendimentoEntity> paged = null;
		if (situacao.equals(TODAS) && inicio == null)
			paged = this.repository.findAllFichaByCnpfcpf(cnpjcpf, pageable);
		else if (situacao.equals(TODAS) && inicio != null)
			paged = this.repository.findAllFichaByCnpfcpfAndDatas(cnpjcpf, inicio, fim, pageable);
		else if (!situacao.equals(TODAS) && inicio == null) {
			paged = this.repository.findAllFichaByCnpfcpfAndSituacao(cnpjcpf, situacao, pageable);
		} else {
			paged = this.repository.findAllFichaByCnpfcpfAndSituacaoAndDatas(cnpjcpf, situacao, inicio, fim, pageable);
		}
		final List<ResultadoPesquisaDto> lst = this.converterListaDeFichasParaDto(paged.getContent());
		final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<>(lst, pageable, lst.size());
		pages.setTotalPages(paged.getTotalPages());
		
		return pages;
	}

	private Page<ResultadoPesquisaDto> pesquisarNumeroFicha(final long numero, final Pageable pageable) {
		final Page<FichaAtendimentoEntity> page = this.repository.findById(numero, pageable); 
		final List<ResultadoPesquisaDto> lst = this.converterListaDeFichasParaDto(page.getContent());
		final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<>(lst, pageable, lst.size());
		pages.setTotalPages(1);
		
		return pages;
	}

	private List<ResultadoPesquisaDto> converterListaDeFichasParaDto(final List<FichaAtendimentoEntity> lst) {
		List<ResultadoPesquisaDto> resultadoList = new ArrayList<>();
		lst.stream().forEach(a -> {
			ResultadoPesquisaDto dto = new ResultadoPesquisaDto();
			dto.setNomeCliente(a.getCliente().getNome());
			dto.setNumero(a.getId());
			dto.setTipo("Ficha");
			dto.setValor(new BigDecimal(0));
			for(PecaServicoFichaEntity b: a.getPecaServicoFichas()) {
				dto.setValor(dto.getValor().add(b.getValor()));
			}
			for(FichaAtendLancEntity b: a.getFichaAtendLancs()) {
				if (b.getAtualSituacao()) {
					dto.setData(b.getData());
					dto.setSituacao(b.getSituacao());
					break;
				}
			}
			
			resultadoList.add(dto);
		});
		return resultadoList;
	}
	
	private List<ResultadoPesquisaDto> converterListaDeOrdensParaDto(final List<OrdemServicoEntity> lst) {
		List<ResultadoPesquisaDto> resultadoList = new ArrayList<>();
		lst.stream().forEach(a -> {
			ResultadoPesquisaDto dto = new ResultadoPesquisaDto();
			dto.setNomeCliente(a.getCliente().getNome());
			dto.setNumero(a.getId());
			dto.setTipo("Ordem");
			dto.setValor(new BigDecimal(0));
			for(PecaServicoOrdemEntity b: a.getPecaServicoOrdems()) {
				dto.setValor(dto.getValor().add(b.getValor()));
			}
			for(OrdemServicoLancEntity b: a.getOrdemServicoLancs()) {
				if (b.getAtualSituacao()) {
					dto.setData(b.getData());
					dto.setSituacao(b.getSituacao());
					break;
				}
			}
			resultadoList.add(dto);
		});
		return resultadoList;
	}
	

}
