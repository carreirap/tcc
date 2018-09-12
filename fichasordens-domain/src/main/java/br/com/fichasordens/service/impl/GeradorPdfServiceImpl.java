package br.com.fichasordens.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fichasordens.Empresa;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.service.GeradorPdfService;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class GeradorPdfServiceImpl implements GeradorPdfService {
	
	@Autowired
	private Empresa empresa;

	@Override
	public ByteArrayOutputStream generateOrdemServicoPdf(OrdemServico ordem) throws Exception {
		
		Empresa emp = this.empresa.buscarEmpresa();
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("nomeEmpresa", emp.getNome());
		parametros.put("fone", emp.getFone());
		StringBuilder endEmp = new StringBuilder();
		endEmp.append(emp.getEndereco().getLogradouro()).append(" ")
		 .append(emp.getEndereco().getNumero()).append(" ")
		 .append(emp.getEndereco().getComplemento());
		parametros.put("endereco", endEmp.toString());
		StringBuilder cepEmp = new StringBuilder();
		cepEmp.append(emp.getEndereco().getCep()).append("  ")
		 .append(emp.getEndereco().getCidade()).append(" - ")
		 .append(emp.getEndereco().getEstado());
		parametros.put("cepCidade", cepEmp.toString());
		parametros.put("numero", String.valueOf(ordem.getId()));
		parametros.put("nomeCliente", ordem.getCliente().getNome());
		StringBuilder end = new StringBuilder();
		end.append(ordem.getCliente().getEndereco().getLogradouro()).append(" ")
		  .append(ordem.getCliente().getEndereco().getNumero())
		  .append(ordem.getCliente().getEndereco().getComplemento()); 
		parametros.put("enderecoCliente",  end.toString());
		parametros.put("foneCliente", ordem.getCliente().getFone());
		parametros.put("responsavel", ordem.getLancamento().get(0).getUsuario().getNome());
		StringBuilder cep = new StringBuilder();
		cep.append(ordem.getCliente().getEndereco().getCep()).append(" ")
		 .append(ordem.getCliente().getEndereco().getCidade()).append(" - ")
		 .append(ordem.getCliente().getEndereco().getEstado());
		parametros.put("cepCliente", cep.toString());
		parametros.put("tipo", ordem.getTipoServico());
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
		parametros.put("abertura", form.format(ordem.getLancamento().get(0).getData()));
		parametros.put("saida", "10/09/2018");
		parametros.put("fabricante", ordem.getFabricante());
		parametros.put("modelo", ordem.getModelo());
		parametros.put("serie", ordem.getSerie());
		parametros.put("descricaoEquip", ordem.getDescEquip());
		parametros.put("defeito", ordem.getDescDefeito());
		parametros.put("estado", ordem.getEstadoItensAcomp());
		//onnection conexao = // obtem uma conexão jdbc...

		
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream input  = classLoader.getResourceAsStream("report2.jasper");
		//URL jasperResURL = this.getClass().getResource();
		//JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperResURL);
		JasperPrint print = JasperFillManager.fillReport(input, parametros);
		

		 JRPdfExporter pdfExporter = new JRPdfExporter();
         pdfExporter.setExporterInput(new SimpleExporterInput(print));
         ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
         pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
         pdfExporter.exportReport();

		
		
		return pdfReportStream;
	}

}
