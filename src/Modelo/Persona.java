package Modelo;

public class Persona {

	private String Nombre;
	private String Apellido;
	private String email;
	private char sexo;
	private boolean casado;

	public Persona(String nombre, String apellido, String email, char sexo, boolean casado) {
		super();
		Nombre = nombre;
		Apellido = apellido;
		this.email = email;
		this.sexo = sexo;
		this.casado = casado;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getApellido() {
		return Apellido;
	}

	public void setApellido(String apellido) {
		Apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public char getSexo() {
		return sexo;
	}

	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	public boolean isCasado() {
		return casado;
	}

	public void setCasado(boolean casado) {
		this.casado = casado;
	}



}
