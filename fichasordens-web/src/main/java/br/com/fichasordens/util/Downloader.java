package br.com.fichasordens.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class Downloader {
	
	private Downloader() {}
	
	public static void retornarArquivoParaDownload(final HttpServletResponse response, final long id, final ByteArrayOutputStream out) throws IOException {
		response.addHeader("Content-disposition", "attachment;filename=ordem-" + id + ".pdf");
		response.setHeader("Content-Length", String.valueOf(out.size()));
		response.setContentType("application/pdf");
	
		OutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(out.toByteArray());
		responseOutputStream.close();
		out.close();
		response.flushBuffer();
	}

}
