package br.com.fichasordens.util;

public class ValidatorCPF {

	public static boolean isCPF(String CPF) {
		CPF = removeCaracteresEspeciais(CPF);
		if(isNumerosCPFIguais(CPF)) {
			return false;
		}

		try {
			char digito10 = calcularPrimeiroDigitoCpf(CPF);
			char digito11 = calcularSegundoDigitoCpf(CPF);

			return digitosDoCpfBatemComCalculados(CPF, digito10, digito11);
		} catch(Exception erro) {
			return (false);
		}
	}

	private static boolean digitosDoCpfBatemComCalculados(String CPF, char digito10, char dig11) {
		if ((digito10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
			return (true);
		} else {
			return (false);
		}
	}

	private static char calcularSegundoDigitoCpf(String CPF) {
		char digito;
		int sm;
		int i;
		int resto;
		int num;
		int peso;
		sm = 0;
		peso = 11;
		for (i = 0; i < 10; i++) {
			num = (int) (CPF.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso - 1;
		}

		resto = 11 - (sm % 11);
		if ((resto == 10) || (resto == 11))
			digito = '0';
		else
			digito = (char) (resto + 48);
		return digito;
	}

	private static char calcularPrimeiroDigitoCpf(String CPF) {
		char digito10;
		int sm;
		int i;
		int r;
		int num;
		int peso;
		sm = 0;
		peso = 10;
		for (i = 0; i < 9; i++) {
			// converte o i-esimo caractere do CPF em um numero:
			// por exemplo, transforma o caractere '0' no inteiro 0
			// (48 eh a posicao de '0' na tabela ASCII)
			num = (int) (CPF.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso - 1;
		}

		r = 11 - (sm % 11);
		if ((r == 10) || (r == 11))
			digito10 = '0';
		else
			digito10 = (char) (r + 48); // converte no respectivo caractere numerico
		return digito10;
	}

	private static boolean isNumerosCPFIguais(String CPF) {
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
				|| CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
				|| CPF.equals("99999999999") || (CPF.length() != 11)) {
			return true;
		} else {
			return false;
		}
		
	}

	/*public boolean isCNPJ(String CNPJ) {

		CNPJ = removeCaracteresEspeciais(CNPJ);

		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222")
				|| CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
				|| CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888")
				|| CNPJ.equals("99999999999999") || (CNPJ.length() != 14))
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o c�digo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-�simo caractere do CNPJ em um n�mero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posi��o de '0' na tabela ASCII)
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			// Verifica se os d�gitos calculados conferem com os d�gitos informados.
			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}*/

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
