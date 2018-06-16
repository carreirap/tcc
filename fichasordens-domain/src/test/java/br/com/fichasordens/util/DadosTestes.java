package br.com.fichasordens.util;

import br.com.fichasordens.Empresa;
import br.com.fichasordens.Endereco;
import br.com.fichasordens.entities.EmpresaEntity;
import br.com.fichasordens.entities.EnderecoEntity;

public class DadosTestes {

	public static EmpresaEntity loadEmpresaEntity() {
		EmpresaEntity empresa = new EmpresaEntity();
		empresa.setCnpj("7644455500001");
		empresa.setEmai("test@test.com");

		empresa.setEndereco(new EnderecoEntity());
		empresa.getEndereco().setBairro("Tester");
		empresa.getEndereco().setCep("8114120");
		empresa.getEndereco().setCidade("Curitiba");
		empresa.getEndereco().setComplemento("sda");
		empresa.getEndereco().setEstado("SP");
		empresa.getEndereco().setId(1L);
		empresa.getEndereco().setLogradouro("Rua das Flores");
		empresa.getEndereco().setNumero(100);

		empresa.setFone("9999999999");
		empresa.setNome("Paulo Carreira");
		empresa.setSite("www.aindanaofaznada.com");
		return empresa;
	}
	
	public static EnderecoEntity loadEnderecoEntity() {
		EnderecoEntity ent = new EnderecoEntity();
	    ent.setId(1L);
		return ent;
	}

	public static Empresa loadEmpresa() {
		Empresa emp = new Empresa();
		emp.setFone("99999999999999");
		emp.setCnpj("cnpjCpf");
		emp.setEmail("test");
		emp.setFone("988888888");
		emp.setNome("test");
		emp.setEndereco(loadEndereco());
		return emp;
	}

	public static Endereco loadEndereco() {
		Endereco end = new Endereco();
		end.setBairro("test");
		end.setCep("81444-444");
		end.setCidade("cidade");
		end.setComplemento("test");
		end.setEstado("PR");
		end.setLogradouro("logradouro");
		end.setNumero(999);
		end.setId(1);
		return end;
	}

}
