package br.com.fichasordens.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fichasordens.Empresa;
import br.com.fichasordens.FichaAtendimento;
import br.com.fichasordens.OrdemServico;
import br.com.fichasordens.service.GeradorPdfService;
import br.com.fichasordens.util.StatusServicoEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class GeradorPdfServiceImpl implements GeradorPdfService {

	@Autowired
	private Empresa empresa;
	
	final Locale local = new Locale("pt","BR");
	
	@Override
	public ByteArrayOutputStream gerarOrdemServicoPdf(final OrdemServico ordem) throws JRException {

		
		Map<String, Object> parametros = new HashMap<>();
		this.preencherDadosEmpresa(parametros);
		
		parametros.put("numero", String.valueOf(ordem.getId()));
		parametros.put("nomeCliente", ordem.getCliente().getNome());
		StringBuilder end = new StringBuilder();
		end.append(ordem.getCliente().getEndereco().getLogradouro()).append(" ")
				.append(ordem.getCliente().getEndereco().getNumero())
				.append(ordem.getCliente().getEndereco().getComplemento());
		parametros.put("enderecoCliente", end.toString());
		parametros.put("foneCliente", ordem.getCliente().getFone());
		parametros.put("responsavel", ordem.getLancamento().get(0).getUsuario().getNome());
		StringBuilder cep = new StringBuilder();
		cep.append(ordem.getCliente().getEndereco().getCep()).append(" ")
				.append(ordem.getCliente().getEndereco().getCidade()).append(" - ")
				.append(ordem.getCliente().getEndereco().getEstado());
		parametros.put("cepCliente", cep.toString());
		parametros.put("tipo", ordem.getTipoServico());
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy", local);
		parametros.put("abertura", form.format(ordem.getLancamento().get(0).getData()));
		ordem.getLancamento().stream()
			.filter(lanc -> lanc.getSituacao().equals(StatusServicoEnum.FECHADO.getValue()))
			.forEach( a-> parametros.put("saida", form.format(a.getData())));
		
		parametros.put("fabricante", ordem.getFabricante());
		parametros.put("modelo", ordem.getModelo());
		parametros.put("serie", ordem.getSerie());
		parametros.put("descricaoEquip", ordem.getDescEquip());
		parametros.put("defeito", ordem.getDescDefeito());
		parametros.put("estado", ordem.getEstadoItensAcomp());
		// onnection conexao = // obtem uma conexão jdbc...

		final ClassLoader classLoader = getClass().getClassLoader();
		final InputStream input = classLoader.getResourceAsStream("ordemservico.jasper");
		final JRBeanCollectionDataSource coll = new JRBeanCollectionDataSource(ordem.getPecaOutroServico());
		final JasperPrint print = JasperFillManager.fillReport(input, parametros, coll);

		JRPdfExporter pdfExporter = new JRPdfExporter();
		pdfExporter.setExporterInput(new SimpleExporterInput(print));
		ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
		pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
		pdfExporter.exportReport();

		return pdfReportStream;
	}
	
	private Map<String, Object> preencherDadosEmpresa(Map<String, Object> parametros) {
		final Empresa emp = this.empresa.buscarEmpresa();
		
		parametros.put("nomeEmpresa", emp.getNome());
		parametros.put("fone", emp.getFone());
		StringBuilder endEmp = new StringBuilder();
		endEmp.append(emp.getEndereco().getLogradouro()).append(" ").append(emp.getEndereco().getNumero()).append(" ")
				.append(emp.getEndereco().getComplemento());
		parametros.put("endereco", endEmp.toString());
		StringBuilder cepEmp = new StringBuilder();
		cepEmp.append(emp.getEndereco().getCep()).append("  ").append(emp.getEndereco().getCidade()).append(" - ")
				.append(emp.getEndereco().getEstado());
		parametros.put("cepCidade", cepEmp.toString());
		
		
		return parametros;
	}

	@Override
	public ByteArrayOutputStream gerarFichaAtendimentoPdf(final FichaAtendimento ficha) throws JRException, FileNotFoundException {
		Map<String, Object> parametros = new HashMap<>();
		
		this.preencherDadosEmpresa(parametros);
		
		parametros.put("numero", String.valueOf(ficha.getId()));
		parametros.put("nomeCliente", ficha.getCliente().getNome());
		StringBuilder end = new StringBuilder();
		end.append(ficha.getCliente().getEndereco().getLogradouro()).append(" ")
				.append(ficha.getCliente().getEndereco().getNumero())
				.append(ficha.getCliente().getEndereco().getComplemento());
		parametros.put("enderecoCliente", end.toString());
		parametros.put("foneCliente", ficha.getCliente().getFone());
		parametros.put("responsavel", ficha.getFichaAtendimentoLancList().get(0).getUsuario().getNome());
		StringBuilder cep = new StringBuilder();
		cep.append(ficha.getCliente().getEndereco().getCep()).append(" ")
				.append(ficha.getCliente().getEndereco().getCidade()).append(" - ")
				.append(ficha.getCliente().getEndereco().getEstado());
		parametros.put("cepCliente", cep.toString());
		parametros.put("tipo", ficha.getTipoServico());
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy", local);
		parametros.put("abertura", form.format(ficha.getFichaAtendimentoLancList().get(0).getData()));
		ficha.getFichaAtendimentoLancList().stream()
			.filter(lanc -> lanc.getSituacao().equals(StatusServicoEnum.FECHADO.getValue()))
			.forEach( a-> parametros.put("saida", form.format(a.getData())));
		
		final ClassLoader classLoader = getClass().getClassLoader();
		InputStream input = classLoader.getResourceAsStream("ficha_atend.jasper");
		
		JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(ficha.getAtendimentoList());
		final JRBeanCollectionDataSource coll = new JRBeanCollectionDataSource(ficha.getPecaOutroServicoList());
		parametros.put("mydatasource", jrDataSource);
		parametros.put("datasourcepeca", coll);
		
		final JasperPrint print = JasperFillManager.fillReport(input, parametros);

		JRPdfExporter pdfExporter = new JRPdfExporter();
		pdfExporter.setExporterInput(new SimpleExporterInput(print));
		ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
		pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
		pdfExporter.exportReport();
		
		return pdfReportStream;
	}

}
