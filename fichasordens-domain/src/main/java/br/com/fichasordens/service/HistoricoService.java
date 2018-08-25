package br.com.fichasordens.service;

import java.util.List;

import br.com.fichasordens.util.ResultadoPesquisaDto;

public interface HistoricoService {
	
	List<ResultadoPesquisaDto> pesquisar(final String tipo, final long numero, final String cnpjcpf, final long idUsuario, final String situacao);
	

}
