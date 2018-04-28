package br.com.fichasordens.dto;

public enum PapelEnum {
	
	ADMIN("Admin"),
	USUARIO("User");
	
	private String nome;
	
	private PapelEnum(final String nome) {
		this.nome = nome;
	}
	
	public static PapelEnum convertEnum(final String value) {
		switch (value) {
		case "User":
			return USUARIO;
		case "Admin":
			return ADMIN;
		default:
			return null;
		}
	}

	public String getNome() {
		return nome;
	}
	
}
