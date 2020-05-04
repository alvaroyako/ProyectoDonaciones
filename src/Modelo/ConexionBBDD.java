package Modelo;
	import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConexionBBDD {

	private String url= "";
	private   String user = "";
	private String pwd = "";
	private   String usr = "";
	private   Connection conexion;

	public ConexionBBDD()  {


		Properties propiedades = new Properties();
		InputStream entrada = null;
		try {
			File miFichero = new File("src/Modelo/bbdd.ini");
			if (miFichero.exists()){
				entrada = new FileInputStream(miFichero);
				propiedades.load(entrada);
				url=propiedades.getProperty("url");
				user=propiedades.getProperty("user");
				pwd=propiedades.getProperty("pwd");
				usr=propiedades.getProperty("usr");
			}

			else
				System.out.println("Fichero no encontrado");
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally {
			if (entrada != null) {
				try {
					entrada.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conexion = DriverManager.getConnection(url, user, pwd);

			if(conexion.isClosed())
				System.out.println("Fallo en Conexión con la Base de Datos");


		}catch (Exception e) {
			System.out.println("ERROR en conexión con ORACLE");
			e.printStackTrace();
		}
	}



	/*
	 * El método InsertarPersona devuelve un código de error para los siguientes casos:
	 *
	 * 0 - Persona insertada OK!
	 * 1 - Se ha queriro introducir uan persona con un email existente (Primary key violated)
	 * 2 - Otro fallo en el tipo de datos o en la base de datos al hacer la inserción
	 *
	 *
	 */
	public int InsertarPersona(String nombre, String apellido, String email, char sexo, boolean casado) throws SQLException{



		String auxcasado = "N";
		if(casado == true)
			auxcasado = "S";


		// Preparo la sentencia SQL
		String insertsql = "INSERT INTO " + usr +".PERSONAS VALUES (?,?,?,?,?)";

		PreparedStatement pstmt = conexion.prepareStatement (insertsql);
		pstmt.setString(1, nombre);
		pstmt.setString(2, apellido);
		pstmt.setString(3, email);
		pstmt.setString(4, Character.toString(sexo));
		pstmt.setString(5, auxcasado);
		//ejecuto la sentencia
		try{
			int resultado = pstmt.executeUpdate();

			if(resultado != 1)
				System.out.println("Error en la inserción " + resultado);
			else
				System.out.println("Persona insertada con éxito!!!");

			return 0;
		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			if(codeErrorSQL.equals("ORA-00001") ){
				System.out.println("Ya existe una persona con  ese email!!");
				return 1;
			}
			else{
				System.out.println("Ha habido algún problema con  Oracle al hacer la insercion");
				return 2;
			}

		}

	}


	/*
	 * El método InsertarPersona devuelve un código de error para los siguientes casos:
	 *
	 * 0 - Persona insertada OK!
	 * 1 - Se ha queriro introducir uan persona con un email existente (Primary key violated)
	 * 2 - Otro fallo en el tipo de datos o en la base de datos al hacer la inserción
	 *
	 *
	 */
	public int ModificarPersona(String nombre, String apellido, String email, char sexo, boolean casado) throws SQLException{

		String auxcasado = "N";
		if(casado == true)
			auxcasado = "S";

		// Preparo la sentencia SQL CrearTablaPersonas
		String updatesql = "UPDATE " + usr + ".PERSONAS SET NOMBRE= ?, APELLIDO =?, SEXO = ?, CASADO = ? WHERE EMAIL= ?";

		PreparedStatement pstmt = conexion.prepareStatement (updatesql);
		pstmt.setString(1, nombre);
		pstmt.setString(2, apellido);
		pstmt.setString(3, Character.toString(sexo));
		pstmt.setString(3, auxcasado);
		pstmt.setString(5, email);


		//ejecuto la sentencia
		try{
			int resultado = pstmt.executeUpdate();

			if(resultado != 1)
				System.out.println("Error en la actualización " + resultado);
			else
				System.out.println("Persona actualizada con éxito!!!");

			return 0;
		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			if(codeErrorSQL.equals("ORA-00001") ){
				System.out.println("Ya existe una persona con  ese email!!");
				return 1;
			}
			else{
				System.out.println("Ha habido algún problema con  Oracle al hacer la insercion");
				return 2;
			}

		}

	}

	public ObservableList<Persona> ObtenerPersonas() throws SQLException{

		ObservableList<Persona> listapersonas = FXCollections.observableArrayList();

		//Preparo la conexión para ejecutar sentencias SQL de tipo update
		Statement stm = conexion.createStatement();

		// Preparo la sentencia SQL CrearTablaPersonas
		String selectsql = "SELECT * FROM " + usr +".PERSONAS";

		//ejecuto la sentencia
		try{
			ResultSet resultado = stm.executeQuery(selectsql);

			int contador = 0;
			while(resultado.next()){
				contador++;

				String nombre = resultado.getString(1);
				String apellido = resultado.getString(2);
				String email = resultado.getString(3);
				char sexo = resultado.getString(4).charAt(0);
				boolean casado = false;
				if(resultado.getString(5).charAt(0) == 'S')
					casado = true;

				Persona nueva = new Persona(nombre, apellido, email,sexo,casado);
				listapersonas.add(nueva);
			}

			if(contador==0)
				System.out.println("no data found");

		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			System.out.println(codeErrorSQL);
		}

		return listapersonas;
	}


	public int BorrarPersona(String email) throws SQLException{

		// Preparo la sentencia SQL y la conexión para ejecutar sentencias SQL de tipo update
		String deletesql = "DELETE " + usr +".PERSONAS WHERE EMAIL= ?";
		PreparedStatement pstmt = conexion.prepareStatement (deletesql);
		pstmt.setString(1, email);

		//ejecuto la sentencia
		try{
			int resultado = pstmt.executeUpdate();

			if(resultado != 1)
				System.out.println("Error en el borrado " + resultado);
			else
				System.out.println("Persona borrada con éxito!!!");

			return 0;
		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			System.out.println("Ha habido algún problema con  Oracle al hacer el borrado" + codeErrorSQL);
			return 2;
		}

	}



	public ObservableList<Persona> BuscarPersonas(String apellido) throws SQLException{

		ObservableList<Persona> listapersonas = FXCollections.observableArrayList();
		PreparedStatement pstmt;


		// Preparo la sentencia SQL en función de lo que venga en apellido y la conexión para ejecutar sentencias SQL de tipo SELECT
		String selectsql = "";
		if(apellido.equals("")){
			selectsql = "SELECT * FROM " + usr +".PERSONAS";
			pstmt = conexion.prepareStatement (selectsql);
		}
		else{
			selectsql = "SELECT * FROM " + usr +".PERSONAS WHERE APELLIDO LIKE ?%";
			pstmt = conexion.prepareStatement (selectsql);
			pstmt.setString(1, apellido);
		}



		//ejecuto la sentencia
		try{
			ResultSet resultado = pstmt.executeQuery();

			int contador = 0;
			while(resultado.next()){
				contador++;

				String nombre = resultado.getString(1);
				String apellidos = resultado.getString(2);
				String email = resultado.getString(3);
				char sexo = resultado.getString(4).charAt(0);
				boolean casado = false;
				if(resultado.getString(5).charAt(0) == 'S')
					casado = true;

				Persona nueva = new Persona(nombre, apellidos, email,sexo,casado);
				listapersonas.add(nueva);
			}

			if(contador==0)
				System.out.println("no data found");

		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			System.out.println(codeErrorSQL);
		}

		return listapersonas;
	}

}