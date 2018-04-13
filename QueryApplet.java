
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
//<applet width= "600" height="600"  code="QueryApplet"></applet>
public class QueryApplet extends Applet implements Runnable,ActionListener {
    private Thread worker;
    private java.awt.TextArea textA=null;
    private java.awt.TextArea textR=null;
    private java.awt.TextArea text_estado=null;
    private String message = "Listo...Esperando una accion";
    private String sql = "SELECT * FROM lista";
    JPanel panel_general,panel_botones,panel_insetar_registro,panel_boton_submit,panel_text_box_informacion;
    JLabel labelMatricula,labelNombre,labelExa1,labelExa2,labelExa3;
    JButton boton_ver_lista,boton_insertar_nuevo_registro,boton_eliminar_registro,submit_insertar;
    JTextField text_matricula,text_nombres,text_exa1,text_exa2,text_exa3;
    /* 
     * JDBC url to use to connect to. This can be overridden in the 
     * applet parameter: jdbcUrl 
     */
    public  String url = "jdbc:odbc:curso1";

    /* 
     * query to run: JOB runs DatabaseMetaData.getTables() when query="tables" 
     * This can be overridden in the applet parameter: query 
     */
    public  String query = "lista";

    public synchronized void start() {
    // Every time "start" is called we create a worker thread to
    // re-evaluate the database query.
        if (worker == null) {   
            message = "Connecting to database";
            worker = new Thread(this);
            worker.start();

        }
    }

    public void  init () {
    //Paneles y demas

    setLayout(new GridLayout(1,1,0,0));
    panel_general = new JPanel(new GridLayout(3,1)); //Panel General
    panel_botones = new JPanel(new GridLayout(1,3)); //Panel contenedor botones

    
    panel_insetar_registro = new JPanel(new GridLayout(3,2)); //Panel contenedor botones
    panel_boton_submit = new JPanel(new GridLayout(1,1)); //Panel contenedor botones
    panel_text_box_informacion = new JPanel(new GridLayout(1,3)); //Panel contenedor botones
    boton_ver_lista = new JButton("Ver lista");
    boton_ver_lista.setSize(10,10);
    boton_insertar_nuevo_registro = new JButton("Agregar nuevo");
    submit_insertar = new JButton("Agregar");
    boton_eliminar_registro = new JButton("Eliminar registro");

    labelMatricula = new JLabel("MATRICULA");
    labelNombre = new JLabel("NOMBRES");
    labelExa1 = new JLabel("EXA1");
    labelExa2 = new JLabel("EXA2");
    labelExa3 = new JLabel("EXA3");

    text_matricula = new JTextField(10);
    text_nombres = new JTextField(20);
    text_exa1 = new JTextField(2);
    text_exa2 = new JTextField(2);
    text_exa3 = new JTextField(2);
    
    //Agregando paneles al panel principal
    
    add(panel_general);//Agregando el panel principal al applet
    panel_general.add(panel_botones);
    panel_botones.add(boton_ver_lista);
    panel_botones.add(boton_insertar_nuevo_registro);
    panel_botones.add(boton_eliminar_registro);
    //Emisor de eventos, esperar una accion sobre algun elemento 
    boton_ver_lista.addActionListener(this);
    boton_insertar_nuevo_registro.addActionListener(this);
    submit_insertar.addActionListener(this);

    // Obteniendo los parametros de la consulta
    String queryParameter = "";
    try {
        queryParameter = getParameter("SELECT * FROM lista;");
    }
    catch (Exception e) 
        { //Esto debe ser ignorado a menos que se este utilizando un navegador
        }
        if (queryParameter!=null && !queryParameter.equals(""))
            query=queryParameter;

    // URL de la ubicacion de la base de datos en este caso odbc
        String jdbcUrl = "";

        try {
            jdbcUrl = getParameter("jdbc:odbc:curso1");
        }
        catch (Exception e) 
        { //Esto debe ser ignorado a menos que se este utilizando un navegador
        }
        
        if (textR==null) {
            textR = new java.awt.TextArea(message,12,70);
            panel_general.add(textR);
        }


    }

    //Emisor de eventos
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()== boton_ver_lista){
            try {
                Connection con = getJOBConnection();
            //Funcion que ejecuta la query
                consultar_registros(con); 
                //Indicando que fue realizada la Query
                if (text_estado==null) {        
                 text_estado = new java.awt.TextArea("Query realizada",12,70);
                 panel_general.add(text_estado);
             }   
         } catch(Exception ex) {
            setError( ex.getMessage());
        }
    }
    //Cuando se pulsa para agregar un boton
    if(ae.getSource()== boton_insertar_nuevo_registro){
            try {
             panel_general.removeAll();
             Connection con = getJOBConnection();
             insertar_registro(con); 
         } catch(Exception ex) {
            System.out.println( ex.getMessage());
        }
    }
    //Cuando se pulsa agregar registro
    if(ae.getSource()== submit_insertar){
            try {
                text_estado = new java.awt.TextArea("",12,70);
                text_estado.setText(text_matricula.getText() + text_nombres.getText() + text_exa1.getText() + text_exa2.getText() + text_exa3.getText()); //Leer el texto del campo matricula
                System.out.println(text_matricula.getText() + text_nombres.getText() + text_exa1.getText() + text_exa2.getText() + text_exa3.getText());
         } catch(Exception ex) {
            System.out.println( ex.getMessage());
        }
    }
}

    /**
     * The "run" method is called from the worker thread. Notice that
     * because this method is doing potentially slow databases accesses
     * we avoid making it a synchronized method.
     */

    public void run() {


    }
    
    public Connection getJOBConnection() throws SQLException {

    // Load the driver
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch(Exception ex) {
            throw new SQLException(ex);
        }

    // Now establish connection with via the Driver manager
        Connection con = DriverManager.getConnection("jdbc:odbc:curso1","jdbc","jdbc");
        return con;
    }

    /**
     * In subclasses this method can be overwritten to execute different
     * db tasks then this given an open connection to the JDBC data source
     * specified in the applet tag
     */
    public void consultar_registros(Connection con)  throws SQLException {
        /**
         * Recibe la conexion para realizar la consulta de los registro en la base de datos
         */
    // Create a statement Object
        Statement stmt = con.createStatement();
    // Execute a query returning a result set
        ResultSet rs = stmt.executeQuery(sql);
    // Get the metadata for the result set
        ResultSetMetaData rsmd = rs.getMetaData();
    // Find out the number of columns in the resultset
        int columnCount = rsmd.getColumnCount();
    //StringBuffer data = new StringBuffer();
        String data = "";
        String rowData = "";
    // Recorriendo el rsultado para imprimir todos los datos
        while (rs.next()) {
            rowData = "";
        // get the data for each column in a row and
        // construct a string of comma separated column values
            for (int i = 1; i<= columnCount; i++) {
                rowData = rowData + rs.getString(i) + " ";
            }
            data = data + rowData + "\n";
        }
    // Update result set text area
        textR.setText(data);
    // Close result set to release database resources
        rs.close();
    // Close Statement to release database resources
        stmt.close();
    //Close Connection to release database resources
        con.close();
        textA.setText("Query realizada");

    }

    public void insertar_registro(Connection con) throws SQLException{

        panel_general.add(panel_botones);
        panel_botones.add(boton_ver_lista);
        panel_botones.add(boton_insertar_nuevo_registro);
        panel_botones.add(boton_eliminar_registro);
        /**
         * Agregar personalizacion a los botones
         */
        

        //Agregando los botones al panel
        panel_insetar_registro.add(labelMatricula);
        panel_insetar_registro.add(text_matricula);

        panel_insetar_registro.add(labelNombre);
        panel_insetar_registro.add(text_nombres);

        panel_insetar_registro.add(labelExa1);
        panel_insetar_registro.add(text_exa1);

        panel_insetar_registro.add(labelExa2);
        panel_insetar_registro.add(text_exa2);

        panel_insetar_registro.add(labelExa3);
        panel_insetar_registro.add(text_exa3);

        panel_boton_submit.add(submit_insertar);
        //---
        
        text_estado = new java.awt.TextArea("",12,70);
        panel_text_box_informacion.add(text_estado);
        panel_general.add(panel_text_box_informacion);
        panel_general.add(panel_insetar_registro);
        panel_general.add(panel_boton_submit); 
        /*Este panel es para que el boton este independiente de los campos del formualrio */

        //Agregar campos del formulario al contenedor
        
    }
    /**
     * This private method is used to record an error message for
     * later display.
     */

    synchronized void setError(String mess) {
        //textA.setText(mess); //Muestra un mensaje en un text area
        worker = null;
    // And ask AWT to repaint this applet.
        repaint();
    }
}