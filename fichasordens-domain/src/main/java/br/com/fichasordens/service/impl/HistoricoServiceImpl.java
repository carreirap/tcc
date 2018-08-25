package br.com.fichasordens.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.service.HistoricoService;
import br.com.fichasordens.util.ResultadoPesquisaDto;
import br.com.fichasordens.util.StatusServicoEnum;

@Service
public class HistoricoServiceImpl implements HistoricoService {
	
	@Autowired
	FichaAtendimento fichaAtendimento;
	
	@Autowired
	OrdemServico ordemServico;
	
	@Transactional
	@Override
	public List<ResultadoPesquisaDto> pesquisar(String tipo, long numero, String cnpjcpf, long idUsuario,
			String situacao) {
		if (tipo.equals("Ficha")) {
			if (numero == 0 && (cnpjcpf == null || cnpjcpf.equals("")) && idUsuario == 0) {
				List<FichaAtendimentoEntity> lst = this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(StatusServicoEnum.valueOf(situacao.toUpperCase()));
				return this.converterListaDeFichasParaDto(lst);
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
			dto.setTipo("F");
			dto.setValor(new BigDecimal(200));
			resultadoList.add(dto);
		});
		
		
		return resultadoList;
	}
	

}
