package br.com.fichasordens.service;

import java.io.ByteArrayOutputStream;

import br.com.fichasordens.OrdemServico;

public interface GeradorPdfService {
	
	ByteArrayOutputStream generateOrdemServicoPdf(OrdemServico ordem) throws Exception;

}
