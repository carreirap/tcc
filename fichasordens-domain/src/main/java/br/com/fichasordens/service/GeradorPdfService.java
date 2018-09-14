package br.com.fichasordens.service;

import java.io.ByteArrayOutputStream;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServico;

public interface GeradorPdfService {
	
	ByteArrayOutputStream generateOrdemServicoPdf(OrdemServico ordem) throws Exception;
	
	ByteArrayOutputStream generateFichaPdf(FichaAtendimento ficha) throws Exception;

}
