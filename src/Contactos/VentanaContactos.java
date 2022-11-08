/*
 * Agenda Teleónica, permite añadir, borrar y modificar contactos.
 * 
 * @Author Aarón
 * @versión 1.1
 */
package Contactos;

import java.sql.*;


import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

class VentanaContactos extends JFrame{
	
	//Variables de los componentes de la Ventana
	JPanel panelGeneral;
	//Variables de cmponentes de Ventana de agregar
	JTextField campoNombre;
	JTextField campoApellido;
	JTextField campoTelefono;
	
	//VARIABLES QUE CONFIGURAN LA TABLA
	JScrollPane scroll;
	JPanel panelListin;
	final int COLUMNAS = 4;
	final String[] ENCABEZADO = new String[] {"Id.","NOMBRE","APELLIDO", "TELÉFONO"};
	final int numeroContactos = 5;
	JTable tbContactos;
	
	String[] encabezadoTabla;
	String[][] contactos;
	
	
	//Variables para gestionar baseDatos
	String path ="jdbc:mysql://localhost:3306/agenda";
	Connection conexionBD;
	Statement envioInstruccion;

	
	
	/*
	 * Constructor de Ventana
	 */
	public VentanaContactos(){
		startVentana();
		
	}
	
	/*
	 * Método que configura la ventana
	 */
	public void startVentana() {
		this.setBounds(100,20,600,400);
		this.setTitle("Agenda de Contactos");
		startPanelPrincipal();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		

		
	}
	public void startPanelPrincipal() {

	//PANEL PRINCIPAL
		panelGeneral = new JPanel();
		panelGeneral.setLayout(null);
		panelGeneral.setBackground(Color.YELLOW);
		this.add(panelGeneral);
		
		
		
	//BOTONES DE INTERACCION CON LA AGENDA
		Eventos evento = new Eventos("Añadir", "Añade un nuevo contacto", 1);
		JButton btAgregar = new JButton(evento);
		btAgregar.setBounds(100,70,90,25);
		panelGeneral.add(btAgregar);
		
		Eventos evento2 = new Eventos("Modificar", "Modifica los datos de un contacto existente", 2);
		JButton btModificar = new JButton(evento2);
		btModificar.setBounds(200,70,90,25);
		panelGeneral.add(btModificar);
		
		Eventos evento3 = new Eventos("Eliminar", "Elimina un contacto de manera permanente", 3);
		JButton btEliminar = new JButton(evento3);
		btEliminar.setBounds(300,70,90,25);
		panelGeneral.add(btEliminar);
	
	//ETIQUETAS COLUMNAS
		JLabel lbContactos = new JLabel("Id.Contacto");
		lbContactos.setBounds(10,100,70,20);
		panelGeneral.add(lbContactos);
		
		JLabel lbNombre = new JLabel("Nombre");
		lbNombre.setBounds(132,100,90,25);
		panelGeneral.add(lbNombre);
		
		JLabel lbApellido = new JLabel("Apellido");
		lbApellido.setBounds(255,100,90,25);
		panelGeneral.add(lbApellido);
		
		JLabel lbTelefono = new JLabel("Teléfono");
		lbTelefono.setBounds(376,100,90,25);
		panelGeneral.add(lbTelefono);
		
	//PANEL SCROLL
		panelListin = new JPanel();
		panelListin.setLayout(null);
		panelListin.setBackground(Color.RED);
		panelListin.setPreferredSize(new Dimension(480,600));
		
		scroll = new JScrollPane();
		scroll.setBounds(10,120,500,220);
		scroll.setViewportView(panelListin);
		panelGeneral.add(scroll);
		
		conectarBD();
		cargarListaContactos();
		//TABLA Dentro del SCROLL
		//encabezadoTabla = new String[] {"Id","Nombre","Apellido","Teléfono"};
		//tbContactos = new JTable(contactos,encabezadoTabla);
		//tbContactos.setSize(485,600);
		//panelListin.add(tbContactos);	
	}
	
	/*
	 * Método encargado de conectar con la BASE DE DATOS
	 */
	public void conectarBD() {
		try {
			conexionBD = DriverManager.getConnection(path,"root","");
			envioInstruccion = conexionBD.createStatement();
			
		}catch(SQLException e) {
			System.out.println("Error de conexión");
		}
	}
	
	/*
	 * Método que carga los contactos en la agenda
	 */
	public void cargarListaContactos() {
		String consulta = "select * from contactos;";
		try {
			envioInstruccion = conexionBD.createStatement();
			
			ResultSet recepcion = envioInstruccion.executeQuery(consulta);
			
			contactos = new String[100][COLUMNAS];
			
				for(int i = 0; recepcion.next(); i++) {
						contactos[i][0] = recepcion.getString("id");
						contactos[i][1] = recepcion.getString("nombre");
						contactos[i][2] = recepcion.getString("apellido");
						contactos[i][3] = recepcion.getString("telefono");
					}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			encabezadoTabla = new String[] {"Id","Nombre","Apellido","Teléfono"};
			tbContactos = new JTable(contactos,encabezadoTabla);
			tbContactos.setSize(485,600);
			panelListin.add(tbContactos);
			tbContactos.setVisible(true);
		}
	}
	
	/*
	 * Método para Agregar nuevos contactos a la Base de Datos
	 * @param nombre: Nombre del contacto
	 * @param apellido: Apellido del contacto
	 * @param telefono: Telefono del contacto
	 */
	public void agregarContacto(String nombre,String apellido, String telefono) {
		String sentencia = "insert into contactos (nombre,apellido,telefono) VALUES ('" + nombre + "','" + apellido +
				"','" + telefono + "')";
		try {
		envioInstruccion.executeUpdate(sentencia);
		}catch(SQLException e1) {
		e1.printStackTrace();
		}finally {
			campoNombre.setText("");
			campoApellido.setText("");
			campoTelefono.setText("");
			
			tbContactos.setVisible(false);
			//try {
			//Thread.sleep(10);
			//} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
			conectarBD();
			cargarListaContactos();
		}
	}
	
	//METODO QUE INICIALIZA LA VENTANA DE AGREGAR USUARIOS
		public void abrirVentanaAgregarContacto() {
			JFrame ventanaAgregar = new JFrame("Agregar Contacto");
			ventanaAgregar.setBounds(250,80,140,200);
			
			JPanel panelAgregar = new JPanel();
			panelAgregar.setLayout(null);
			panelAgregar.setBackground(Color.ORANGE);
			ventanaAgregar.add(panelAgregar);
			
			//Conjunto campo 1
				JLabel lbNombreAgregar = new JLabel("Nombre");
				lbNombreAgregar.setBounds(5,10,70,25);
				panelAgregar.add(lbNombreAgregar);
			
				campoNombre = new JTextField();
				campoNombre.setBounds(5,30,110,25);
				panelAgregar.add(campoNombre);
			
			//Conjunto campo 2
					JLabel lbApellidoAgregar = new JLabel("Apellido");
					lbApellidoAgregar.setBounds(130,10,70,25);
					panelAgregar.add(lbApellidoAgregar);
					
					campoApellido = new JTextField();
					campoApellido.setBounds(130,30,110,25);
					panelAgregar.add(campoApellido);
					
			//Conjunto campo 3
					JLabel lbTelefonoAgregar = new JLabel("Teléfono");
					lbTelefonoAgregar.setBounds(260,10,100,25);
					panelAgregar.add(lbTelefonoAgregar);
					
					campoTelefono = new JTextField();
					campoTelefono.setBounds(260,30,110,25);
					panelAgregar.add(campoTelefono);
			
			//BOTON AGREGAR USUARIOS
					Eventos evento4 = new Eventos("Añadir","Añadir Contacto",4);
					JButton bt04 = new JButton(evento4);
					bt04.setBounds(140,80,90,25);
					panelAgregar.add(bt04);
					
			ventanaAgregar.setVisible(true);
		}
	
	/*
	 * METODO DE BORRADO DE DATOS
	 */
		
	public void borrarContacto() {
		int filaSeleccionada = tbContactos.getSelectedRow();
		
		String sentenciaBorrado = "DELETE FROM contactos WHERE id = " + tbContactos.getValueAt(filaSeleccionada,0) + ";";
		try {
			envioInstruccion.executeUpdate(sentenciaBorrado);
		} catch (SQLException e) {
			System.out.println("Sentencia de borrado no ejecutada");
		}finally {
			tbContactos.setVisible(false);
			conectarBD();
			cargarListaContactos();
		}
	}
	
	/*
	 * METODO MODIFICAR CONTACTO
	 */
	public void modificarContacto() {
		int filaSeleccionada = tbContactos.getSelectedRow();
		String sentenciaModificada= "Update contactos set nombre = '"+ tbContactos.getValueAt(filaSeleccionada,1) +"', apellido = '"
		+ tbContactos.getValueAt(filaSeleccionada, 2) +"', telefono = '" + tbContactos.getValueAt(filaSeleccionada,3) +
				"' where id = " + tbContactos.getValueAt(filaSeleccionada,0);
		try {
			envioInstruccion.execute(sentenciaModificada);
		}catch(SQLException e) {
			System.out.println("sentencia de actualización falló");
			System.out.println(tbContactos.getValueAt(filaSeleccionada,1));
		}finally {
			tbContactos.setVisible(false);
			conectarBD();
			cargarListaContactos();
		}
	}
	/*
	 * Clase interna Hereda de AbstractAction
	 */
	class Eventos extends AbstractAction{
		
		/*
		 * Constructor Eventos
		 * @param nombre: nombre del boton
		 * @param descripcion: Descripcion de la funcionalidad del boton
		 * @para id: identificador int para habilitarlo en el switch de actionPerformed().
		 */
		Eventos(String nombre,String descripcion,int id){
			putValue(Action.NAME,nombre);
			putValue(Action.SHORT_DESCRIPTION,descripcion);
			putValue("id",id);
		}
		
		/*
		 * Instrucciones para los botones de la apicación
		 * @param e : evento que recibe de los clic 
		 * Switch que controla los id de los botones.
		 */
		public void actionPerformed(ActionEvent e) {
			
			int valor = (int)getValue("id");

			switch(valor) {
			case 1: abrirVentanaAgregarContacto();
			break;
			case 2:modificarContacto();
			break;
			case 3: borrarContacto();
			break;
			case 4: agregarContacto(campoNombre.getText(), campoApellido.getText(),campoTelefono.getText());
			break;
			}
		}


	}
}
