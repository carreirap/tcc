package br.com.fichasordens.service;

import java.io.ByteArrayOutputStream;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServico;

public interface GeradorPdfService {
	
	ByteArrayOutputStream gerarOrdemServicoPdf(OrdemServico ordem) throws Exception;
	
	ByteArrayOutputStream gerarFichaAtendimentoPdf(FichaAtendimento ficha) throws Exception;

}
