package br.com.fichasordens.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.fichasordens.util.ResultadoPesquisaDto;

public interface HistoricoService {
	
	Page<ResultadoPesquisaDto> pesquisar(final String tipo, final long numero, final String cnpjcpf, final String situacao, final Pageable page);
	

}
