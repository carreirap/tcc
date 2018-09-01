package br.com.fichasordens.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.entities.FichaAtendLancEntity;
import br.com.fichasordens.entities.FichaAtendimentoEntity;
import br.com.fichasordens.entities.OrdemServicoEntity;
import br.com.fichasordens.entities.OrdemServicoLancEntity;
import br.com.fichasordens.entities.PecaServicoFichaEntity;
import br.com.fichasordens.entities.PecaServicoOrdemEntity;
import br.com.fichasordens.service.DashboardService;
import br.com.fichasordens.util.DashBoardDto;
import br.com.fichasordens.util.DataUtil;
import br.com.fichasordens.util.StatusServicoEnum;

@PropertySource("classpath:application.properties")
@Service
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	private FichaAtendimento fichaAtendimento;
	
	@Autowired
	private OrdemServico ordemServico;
	
	@Value("${alerta.servico.parado.situacao}")
	private String qtdDiasAlerta;

	@Override
	public Map<String,DashBoardDto> contarFichasAtendimento() {
		return this.contarFichasPorSituacao();
	}

	@Override
	public Map<String,DashBoardDto> contarOrdensDeServicos() {
		return this.contarOrdensPorSituacao();
	}
	
	private Map<String,DashBoardDto> contarFichasPorSituacao() {
		Map<String,DashBoardDto> map = new HashMap<>();
		for(StatusServicoEnum e: StatusServicoEnum.values()) {
			if (e == StatusServicoEnum.FECHADO || e == StatusServicoEnum.CANCELADO) {
				Map<String,Date> dateMap = DataUtil.getDataInicioDataFim();
				List<FichaAtendimentoEntity> lst = this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(e, dateMap.get(DataUtil.DATA_INICIO), dateMap.get(DataUtil.DATA_FIM));
				map.putAll(calcularTotaisFichaAtendimento(lst, e.getValue()));
			} else {
				List<FichaAtendimentoEntity> lst = this.fichaAtendimento.buscarFichaAtendimentoPorSituacao(e);
				map.putAll(calcularTotaisFichaAtendimento(lst, e.getValue()));
			}
			
		}
		return map;
	}
	
	private Map<String,DashBoardDto> calcularTotaisFichaAtendimento(List<FichaAtendimentoEntity> lst, final String situacao) {
		final int diasAlerta = Integer.parseInt(qtdDiasAlerta);
		Map<String,DashBoardDto> map = new HashMap<>();
		int qtdAberto = 0;
		
		BigDecimal totalAberto = new BigDecimal(0);
		
		boolean alertaAberto = false;
				
		for (FichaAtendimentoEntity a : lst) {
			for (FichaAtendLancEntity lanc : a.getFichaAtendLancs()) {
				if (lanc.getAtualSituacao()) {
					qtdAberto = qtdAberto + 1; 
					totalAberto = totalAberto.add(calcularTotalPecaServicos(a));
					if (DataUtil.calcularDiferencaDiasEntreUmaDataEAgora(lanc.getData()) > diasAlerta ) {
						alertaAberto = true;
					}
				}
			}
		}
		map.put(situacao, new DashBoardDto(qtdAberto, totalAberto, (alertaAberto ? 'S' : 'N')));
		
		return map;
	}
	
	private BigDecimal calcularTotalPecaServicos(FichaAtendimentoEntity ficha) {
		BigDecimal totalPecaServico = new BigDecimal(0);
		for (PecaServicoFichaEntity lanc : ficha.getPecaServicoFichas()) {
			BigDecimal resultado = totalPecaServico.add(lanc.getValor());
			totalPecaServico = resultado;
		}
		return totalPecaServico;
	}
	
	private Map<String,DashBoardDto> contarOrdensPorSituacao() {
		Map<String,DashBoardDto> map = new HashMap<>();
		for(StatusServicoEnum e: StatusServicoEnum.values()) {
			if (e == StatusServicoEnum.FECHADO || e == StatusServicoEnum.CANCELADO) {
				Map<String,Date> dateMap = DataUtil.getDataInicioDataFim();
				List<OrdemServicoEntity> lst = this.ordemServico.buscarOrdensDeServicoPorSituacao(e.getValue(), dateMap.get(DataUtil.DATA_INICIO), dateMap.get(DataUtil.DATA_FIM));
				map.putAll(calcularTotaisOrdens(lst, e.getValue()));
			} else {	
				List<OrdemServicoEntity> lst = this.ordemServico.buscarOrdensDeServicoPorSituacao(e.getValue());
				map.putAll(calcularTotaisOrdens(lst, e.getValue()));
			}
		}
		return map;
	}

	private Map<String,DashBoardDto> calcularTotaisOrdens(final List<OrdemServicoEntity> lst, final String situacao) {
		Map<String,DashBoardDto> map = new HashMap<>();
		int qtdAberto = 0;
		BigDecimal totalAberto = new BigDecimal(0);
		
		boolean alerta = false;
		
		for (OrdemServicoEntity a : lst) {
			for (OrdemServicoLancEntity lanc : a.getOrdemServicoLancs()) {
				if (lanc.getAtualSituacao()) {
					qtdAberto = qtdAberto + 1; 
					totalAberto = totalAberto.add(calcularTotalPecaServicos(a));
					if (DataUtil.calcularDiferencaDiasEntreUmaDataEAgora(lanc.getData()) > 1 ) {
						alerta = true;
					}
				}
			}
		}
		
		map.put(situacao, new DashBoardDto(qtdAberto, totalAberto, (alerta ? 'S' : 'N')));
		return map;
	}
	
	private BigDecimal calcularTotalPecaServicos(OrdemServicoEntity ordem) {
		BigDecimal totalPecaServico = new BigDecimal(0);
		for (PecaServicoOrdemEntity lanc : ordem.getPecaServicoOrdems()) {
			BigDecimal resultado = totalPecaServico.add(lanc.getValor());
			totalPecaServico = resultado;
		}
		return totalPecaServico;
	}


}
