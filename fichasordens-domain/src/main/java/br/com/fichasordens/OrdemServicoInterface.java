package br.com.fichasordens;

import br.com.fichasordens.exception.ExcecaoRetorno;

public interface OrdemServicoInterface {
	
	OrdemServico gravarOrdem(OrdemServico ordemServico) throws ExcecaoRetorno;
	
	OrdemServico buscarOrdem(long id);
	
	PecaOutroServico gravarPecaServicoOrdem(PecaOutroServico pecaServicoOrdem) throws ExcecaoRetorno;
	
	OrdemServicoLanc gravarOrdemServicoLanc(final OrdemServicoLanc ordemServicoLanc) throws ExcecaoRetorno;

}
