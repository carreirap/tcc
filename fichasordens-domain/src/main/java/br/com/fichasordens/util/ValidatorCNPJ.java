package br.com.fichasordens.util;

import java.util.InputMismatchException;

public class ValidatorCNPJ {
	
	public static boolean isCNPJ(String CNPJ) {

		CNPJ = removeCaracteresEspeciais(CNPJ);

		if (isNumerosCnpjIguais(CNPJ) == false) {
			return false;
		}

		char digito13, digito14;

		try {
			digito13 = calcularPrimeiroDigito(CNPJ);

			digito14 = calcularSegundoDigito(CNPJ);

			return digitosDoCnpjBatemComCalculados(CNPJ, digito13, digito14);
		} catch (InputMismatchException erro) {
			return false;
		}
	}

	private static  boolean digitosDoCnpjBatemComCalculados(String CNPJ, char digito13, char digito14) {
		if ((digito13 == CNPJ.charAt(12)) && (digito14 == CNPJ.charAt(13)))
			return (true);
		else
			return (false);
	}

	private static char calcularSegundoDigito(String CNPJ) {
		char digito;
		int sm;
		int i;
		int r;
		int num;
		int peso;
		sm = 0;
		peso = 2;
		for (i = 12; i >= 0; i--) {
			num = (int) (CNPJ.charAt(i) - 48);
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

	private static char calcularPrimeiroDigito(String CNPJ) {
		char digito;
		int sm;
		int i;
		int r;
		int num;
		int peso;
		sm = 0;
		peso = 2;
		for (i = 11; i >= 0; i--) {
			// converte o i-ésimo caractere do CNPJ em um número:
			// por exemplo, transforma o caractere '0' no inteiro 0
			// (48 eh a posição de '0' na tabela ASCII)
			num = (int) (CNPJ.charAt(i) - 48);
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

	private static boolean isNumerosCnpjIguais(String CNPJ) {
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222")
				|| CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
				|| CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888")
				|| CNPJ.equals("99999999999999") || (CNPJ.length() != 14)) {
			return false;
		} else {
			return true;
		}
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
