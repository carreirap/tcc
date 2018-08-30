package br.com.fichasordens.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
	public Page<ResultadoPesquisaDto> pesquisar(final String tipo, final long numero, final String cnpjcpf, final long idUsuario,
			final String situacao, final Pageable pageable) {
		if (tipo.equals("Ficha")) {
			if (numero != 0 ) {
				final FichaAtendimentoEntity ent = this.repository.findOne(numero); 
				final List<ResultadoPesquisaDto> lst = this.converterListaDeFichasParaDto(Arrays.asList(ent));
				final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<ResultadoPesquisaDto>(lst, pageable, lst.size());
				pages.setTotalPages(1);
				
				return pages;
			}
			
			if (numero == 0 && (cnpjcpf != null && !cnpjcpf.equals("")) && idUsuario == 0) {
				Page<FichaAtendimentoEntity> paged = null;
				if (situacao.equals(TODAS))
					paged = this.repository.FindAllFichaByCnpfcpf(cnpjcpf, pageable);
				else 
					paged = this.repository.FindAllFichaByCnpfcpfAndSituacao(cnpjcpf, situacao, pageable);
				final List<ResultadoPesquisaDto> lst = this.converterListaDeFichasParaDto(paged.getContent());
				final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<ResultadoPesquisaDto>(lst, pageable, lst.size());
				pages.setTotalPages(paged.getTotalPages());
				
				return pages;
			}
			
			if (numero == 0 && (cnpjcpf == null || cnpjcpf.equals("")) && idUsuario == 0) {
				final Page<FichaAtendimentoEntity> paged = this.repository.FindAllFichaByStatus(situacao, pageable); 
				final List<ResultadoPesquisaDto> lst = this.converterListaDeFichasParaDto(paged.getContent());
				final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<ResultadoPesquisaDto>(lst, pageable, lst.size());
				pages.setTotalPages(paged.getTotalPages());
				
				return pages;
			}
		} else {
			if (numero != 0 ) {
				final OrdemServicoEntity ent = this.ordemRepository.findOne(numero); 
				final List<ResultadoPesquisaDto> lst = this.converterListaDeOrdensParaDto(Arrays.asList(ent));
				final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<ResultadoPesquisaDto>(lst, pageable, lst.size());
				pages.setTotalPages(1);
				
				return pages;
			}
			
			if (numero == 0 && (cnpjcpf != null && !cnpjcpf.equals("")) && idUsuario == 0) {
				Page<OrdemServicoEntity> paged = null;
				if (situacao.equals(TODAS))
					paged = this.ordemRepository.FindAllOrdensByCnpfcpf(cnpjcpf, pageable);
				else 
					paged = this.ordemRepository.FindAllOrdensByCnpfcpfAndSituacao(cnpjcpf, situacao, pageable);
				final List<ResultadoPesquisaDto> lst = this.converterListaDeOrdensParaDto(paged.getContent());
				final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<ResultadoPesquisaDto>(lst, pageable, lst.size());
				pages.setTotalPages(paged.getTotalPages());
				
				return pages;
			}
			
			if (numero == 0 && (cnpjcpf == null || cnpjcpf.equals("")) && idUsuario == 0) {
				final Page<OrdemServicoEntity> paged = this.ordemRepository.FindAllOrdensByStatus(situacao, pageable); 
				final List<ResultadoPesquisaDto> lst = this.converterListaDeOrdensParaDto(paged.getContent());
				final CustomPage<ResultadoPesquisaDto> pages = new CustomPage<ResultadoPesquisaDto>(lst, pageable, lst.size());
				pages.setTotalPages(paged.getTotalPages());
				
				return pages;
			}
		}
		
		return null;
	}

	private List<ResultadoPesquisaDto> converterListaDeFichasParaDto(final List<FichaAtendimentoEntity> lst) {
		List<ResultadoPesquisaDto> resultadoList = new ArrayList<ResultadoPesquisaDto>();
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
					break;
				}
			}
			
			resultadoList.add(dto);
		});
		return resultadoList;
	}
	
	private List<ResultadoPesquisaDto> converterListaDeOrdensParaDto(final List<OrdemServicoEntity> lst) {
		List<ResultadoPesquisaDto> resultadoList = new ArrayList<ResultadoPesquisaDto>();
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
					break;
				}
			}
			resultadoList.add(dto);
		});
		return resultadoList;
	}
	

}
