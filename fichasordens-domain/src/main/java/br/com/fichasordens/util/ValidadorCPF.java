package br.com.fichasordens.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ValidadorCPF {
	
	private ValidadorCPF() {
	}
	private static final Logger LOGGER = LogManager.getLogger(ValidadorCPF.class);

	public static boolean validarCPF(String cpf) {
		boolean cpfValidado = false;
		cpf = removeCaracteresEspeciais(cpf);
		if(!isNumerosCPFIguais(cpf)) {
			cpfValidado = isCPF(cpf);
		}
		return cpfValidado;
	}

	private static boolean isCPF(String cpf) {
		boolean cpfValidado;
		try {
			char digito10 = calcularPrimeiroDigitoCpf(cpf);
			char digito11 = calcularSegundoDigitoCpf(cpf);

			cpfValidado = digitosDoCpfBatemComCalculados(cpf, digito10, digito11);
		} catch(Exception error) {
			LOGGER.info(error.getMessage(), error);
			cpfValidado = false;
		}
		return cpfValidado;
	}

	private static boolean digitosDoCpfBatemComCalculados(String cpf, char digito10, char dig11) {
		boolean digitosBatem = false;
		if ((digito10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))) {
			digitosBatem = true;
		}
		return digitosBatem;
	}

	private static char calcularSegundoDigitoCpf(String cpf) {
		char digito;
		int sm = 0;
		int i;
		int resto;
		int num;
		int peso = 11;
		for (i = 0; i < 10; i++) {
			num = (cpf.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso - 1;
		}

		resto = 11 - (sm % 11);
		if ((resto == 10) || (resto == 11)) {
			digito = '0';
		} else {
			digito = (char) (resto + 48);
		}
		return digito;
	}

	private static char calcularPrimeiroDigitoCpf(String cpf) {
		char digito10;
		int sm = 0;
		int i;
		int r;
		int num;
		int peso = 10;
		for (i = 0; i < 9; i++) {
			// converte o i-esimo caractere do CPF em um numero:
			// por exemplo, transforma o caractere '0' no inteiro 0
			// (48 eh a posicao de '0' na tabela ASCII)
			num = (cpf.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso - 1;
		}

		r = 11 - (sm % 11);
		if ((r == 10) || (r == 11)) {
			digito10 = '0';
		} else {
			digito10 = (char) (r + 48); // converte no respectivo caractere numerico
		}
		return digito10;
	}

	private static boolean isNumerosCPFIguais(String cpf) {
		boolean numeroIguais = false;
		if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222")
				|| cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555")
				|| cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888")
				|| cpf.equals("99999999999") || (cpf.length() != 11)) {
			numeroIguais =  true;
		} 
		return numeroIguais;
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
