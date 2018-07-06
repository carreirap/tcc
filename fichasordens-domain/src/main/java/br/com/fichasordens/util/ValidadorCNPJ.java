package br.com.fichasordens.util;

import java.util.InputMismatchException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ValidadorCNPJ {
	private ValidadorCNPJ() {
	}
	private static final Logger LOGGER = LogManager.getLogger(ValidadorCNPJ.class);
	
	public static boolean validarCNPJ(String cnpj) {
		boolean cnpjValidado = false;
		cnpj = removeCaracteresEspeciais(cnpj);
		if (!isNumerosCnpjIguais(cnpj)) {
			cnpjValidado = isCNPJ(cnpj);
		}
		return cnpjValidado;
	}

	private static boolean isCNPJ(String cnpj) {
		boolean cnpjValidado;
		try {
			char digito13 = calcularPrimeiroDigito(cnpj);

			char digito14 = calcularSegundoDigito(cnpj);

			cnpjValidado = digitosDoCnpjBatemComCalculados(cnpj, digito13, digito14);
		} catch (InputMismatchException error) {
			LOGGER.info(error.getMessage(), error);
			cnpjValidado = false;
		}
		return cnpjValidado;
	}

	private static  boolean digitosDoCnpjBatemComCalculados(String cnpj, char digito13, char digito14) {
		boolean digitosBatem = false;
		if ((digito13 == cnpj.charAt(12)) && (digito14 == cnpj.charAt(13))) {
			digitosBatem = true;
		}
		return digitosBatem;
	}

	private static char calcularSegundoDigito(String cnpj) {
		char digito;
		int sm;
		int i;
		int r;
		int num;
		int peso;
		sm = 0;
		peso = 2;
		for (i = 12; i >= 0; i--) {
			num = (cnpj.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso + 1;
			if (peso == 10) {
				peso = 2;
			}
		}

		r = sm % 11;
		if ((r == 0) || (r == 1)) {
			digito = '0';
		} else {
			digito = (char) ((11 - r) + 48);
		}
		return digito;
	}

	private static char calcularPrimeiroDigito(String cnpj) {
		char digito;
		int sm;
		int i;
		int r;
		int num;
		int peso;
		sm = 0;
		peso = 2;
		for (i = 11; i >= 0; i--) {
			num = (cnpj.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso + 1;
			if (peso == 10) {
				peso = 2;
			}
		}

		r = sm % 11;
		if ((r == 0) || (r == 1)) {
			digito = '0';
		} else {
			digito = (char) ((11 - r) + 48);
		}
		return digito;
	}

	private static boolean isNumerosCnpjIguais(String cnpj) {
		boolean valido = false;
		if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222")
				|| cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
				|| cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888")
				|| cnpj.equals("99999999999999") || (cnpj.length() != 14)) {
			valido = true;
		}
		return valido;
	}

	private static String removeCaracteresEspeciais(String doc) {
		if (doc.contains(".")) {
			doc = doc.replace(".", "");
		}
		if (doc.contains("-")) {
			doc = doc.replace("-", "");
		}
		if (doc.contains("/")) {
			doc = doc.replace("/", "");
		}
		return doc;
	}
}
