package br.com.fichasordens.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServico;
import net.sf.jasperreports.engine.JRException;

public interface GeradorPdfService {
	
	ByteArrayOutputStream gerarOrdemServicoPdf(OrdemServico ordem) throws JRException;
	
	ByteArrayOutputStream gerarFichaAtendimentoPdf(FichaAtendimento ficha) throws JRException, FileNotFoundException;

}
