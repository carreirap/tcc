package br.com.fichasordens;

import br.com.fichasordens.exception.ExcecaoRetorno;

public interface OrdemServicoInterface {
	
	OrdemServico gravarOrdem(OrdemServico ordemServico) throws ExcecaoRetorno;

}
