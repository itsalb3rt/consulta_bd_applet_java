
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
//<applet width= "700" height="800"  code="QueryApplet"></applet>
public class QueryApplet extends Applet implements Runnable,ActionListener {
    private Thread worker;
    private java.awt.TextArea textA=null;
    private java.awt.TextArea textR=null;
    private java.awt.TextArea text_estado=null;
    private String message = "Listo...Esperando una accion";
    public int consulta_bd = 0;
    JPanel panel_general,panel_botones,panel_insetar_registro,panel_boton_submit,panel_text_box_informacion,panel_inicio,panel_boton_volver_menu,panel_actualizar_registro,panel_eliminar_registro;
    JLabel labelMatricula,labelNombre,labelExa1,labelExa2,labelExa3,labelMatricula_eliminar;
    JButton boton_ver_lista,boton_insertar_nuevo_registro,boton_eliminar_registro,boton_actualizar_registro,submit_insertar,volver_menu_principal,boton_submit_eliminar;
    JTextField text_matricula,text_nombres,text_exa1,text_exa2,text_exa3,text_matricula_eliminar;
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

    setLayout(new GridLayout(1,1));
    panel_general = new JPanel(new GridLayout(4,1)); //Panel General, este afecta los demas paneles el primer valor son las filas
    panel_inicio = new JPanel(new GridLayout(2,1)); //Panel de inicio de la app
    panel_botones = new JPanel(new GridLayout(1,3)); //Panel contenedor botones
    panel_actualizar_registro = new JPanel(new GridLayout(1,3)); //Panel contenedor botones
    panel_insetar_registro = new JPanel(new GridLayout(6,1)); //Panel contenedor botones
    panel_boton_volver_menu = new JPanel(new GridLayout(1,1)); //Panel contenedor botones
    panel_eliminar_registro = new JPanel(new GridLayout(1,3)); //Panel contenedor botones
    panel_boton_submit = new JPanel(new GridLayout(1,3)); //Panel contenedor botones
    panel_text_box_informacion = new JPanel(new GridLayout(1,3));
    
    boton_ver_lista = new JButton("Ver lista alumno/a");
    boton_insertar_nuevo_registro = new JButton(" + Agregar alumno/a");
    boton_actualizar_registro = new JButton("Actualizar alumno/a");
    submit_insertar = new JButton("+ Agregar");
    boton_eliminar_registro = new JButton(" X Eliminar alumno/a");
    volver_menu_principal = new JButton("Volver al menu");
    boton_submit_eliminar = new JButton("X ELIMINAR");

    //Color botones
    boton_ver_lista.setBackground(new java.awt.Color(139,195,74));
    boton_insertar_nuevo_registro.setBackground(new java.awt.Color(3,169,244));
    submit_insertar.setBackground(new java.awt.Color(3,169,244));
    boton_eliminar_registro.setBackground(new java.awt.Color(244,67,54));
    boton_submit_eliminar.setBackground(new java.awt.Color(244,67,54));
    boton_actualizar_registro.setBackground(new java.awt.Color(189,189,189));
    volver_menu_principal.setBackground(new java.awt.Color(189,189,189));


    labelMatricula = new JLabel("MATRICULA");
    labelMatricula_eliminar = new JLabel("MATRICULA");
    labelNombre = new JLabel("NOMBRES");
    labelExa1 = new JLabel("EXA1");
    labelExa2 = new JLabel("EXA2");
    labelExa3 = new JLabel("EXA3");

    text_matricula = new JTextField(10);
    text_matricula_eliminar = new JTextField(10);
    text_nombres = new JTextField(20);
    text_exa1 = new JTextField(2);
    text_exa2 = new JTextField(2);
    text_exa3 = new JTextField(2);

    //Agregando margin
    panel_botones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
    panel_insetar_registro.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
    panel_boton_volver_menu.setBorder(BorderFactory.createEmptyBorder(50, 200, 0, 200)); 
    //Agregando paneles al panel principal
    
    add(panel_general);//Agregando el panel principal al applet
    panel_inicio.add(panel_botones);
    panel_botones.add(boton_ver_lista);
    panel_botones.add(boton_insertar_nuevo_registro);
    panel_botones.add(boton_actualizar_registro);
    panel_botones.add(boton_eliminar_registro);
    panel_eliminar_registro.add(boton_submit_eliminar);

        //Agregando los botones al insertar nuevo alumno
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
        //Panel eliminar alumno
        panel_eliminar_registro.add(labelMatricula_eliminar);
        panel_eliminar_registro.add(text_matricula_eliminar);
        panel_eliminar_registro.add(boton_submit_eliminar);
        //
        text_estado = new java.awt.TextArea("",4,20);
        textR = new java.awt.TextArea(message,12,70);  
        textR.setRows(100);
        panel_inicio.add(textR);
        panel_insetar_registro.add(panel_boton_submit); 
        panel_text_box_informacion.setVisible(false);
        panel_general.add(panel_insetar_registro);
        panel_general.add(panel_inicio);
        panel_boton_volver_menu.add(volver_menu_principal);
        panel_general.add(panel_eliminar_registro);
        panel_general.add(panel_boton_volver_menu);
        
        //panel_general.add(panel_boton_submit); 
        /*Este panel es para que el boton este independiente de los campos del formualrio */
        panel_insetar_registro.setVisible(false);
        panel_eliminar_registro.setVisible(false);
        
        panel_inicio.setBorder(BorderFactory.createLineBorder(Color.black));
        panel_eliminar_registro.setBorder(BorderFactory.createLineBorder(Color.black));
        panel_insetar_registro.setBorder(BorderFactory.createLineBorder(Color.black));

    //Emisor de eventos, esperar una accion sobre algun elemento 
    boton_ver_lista.addActionListener(this);
    boton_insertar_nuevo_registro.addActionListener(this);
    submit_insertar.addActionListener(this);
    volver_menu_principal.addActionListener(this);
    boton_eliminar_registro.addActionListener(this);
    boton_submit_eliminar.addActionListener(this);

    // Obteniendo los parametros de la consulta
    String queryParameter = "";
    try {
        queryParameter = getParameter("SELECT * FROM lista ORDER BY id asc;");
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


    }

    //Emisor de eventos
    public void actionPerformed(ActionEvent ae){
        //Boton que despliega los alumnos registrados
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
    if(ae.getSource()== volver_menu_principal){
            panel_insetar_registro.setVisible(false);
            panel_eliminar_registro.setVisible(false);
            panel_inicio.setVisible(true);
            textR.setText("");
    }
    //Cuando se pulsa para agregar un boton
    if(ae.getSource()== boton_insertar_nuevo_registro){
            try {
             submit_insertar.setVisible(true);
             panel_inicio.setVisible(false);
             panel_insetar_registro.setVisible(true);
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
                panel_text_box_informacion.setVisible(true); //Notificar al usuario que se inserto el registros
                Connection con = getJOBConnection();
                insertar_registro(con,text_matricula.getText(),text_nombres.getText(),text_exa1.getText(),text_exa2.getText(),text_exa3.getText());
         } catch(Exception ex) {
            System.out.println( ex.getMessage());
        }
    }
    //Cuando se pulsa eliminar alummno
    if(ae.getSource()== boton_eliminar_registro){
            panel_eliminar_registro.setVisible(true);
            boton_submit_eliminar.setVisible(true);
            panel_inicio.setVisible(false);
    }
    if(ae.getSource()== boton_submit_eliminar){
            try {
                Connection con = getJOBConnection();
                eliminar_registro(con,text_matricula_eliminar.getText());
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
     * Recibe la conexion y retorna un valor entero el cual representa la cantidad 
     * de datos que existen en la base de datos
     * @param  con          [conexion]
     * @return              [entero con la cantidad de filas]
     * @throws SQLException [manejo de excepcion]
     */
    public int verificar_registro_existente(Connection con,String matricula)  throws SQLException{

        int count = 0; //Cantidad de filas de la query
        String sql = "SELECT MATRICULA FROM lista WHERE MATRICULA = '" + matricula + "'";
        // Creando objeto statement
        Statement stmt = con.createStatement();
        // ejecutando la query
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            ++count;
        }
        return count;
    }
    
    /** En subclases, este método se puede sobrescribir para ejecutar diferentes
     * tareas db luego esto dado una conexión abierta a la fuente de datos JDBC
     * especificado en la etiqueta del applet
     */
    public void consultar_registros(Connection con)  throws SQLException {
        /**
         * Recibe la conexion para realizar la consulta de los registro en la base de datos
         */
    
    String sql = "SELECT * FROM lista";
    // Creando objeto statement
        Statement stmt = con.createStatement();
    // ejecutando la query
        ResultSet rs = stmt.executeQuery(sql);
    // Obteniendo la data para luego procesarla en un ciclo
        ResultSetMetaData rsmd = rs.getMetaData();
    // Buscando el numero de columnas del resultado
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
    // 
        textR.setText(data);
    // Close result set to release database resources
        rs.close();
    // Close Statement to release database resources
        stmt.close();
    //Close Connection to release database resources
        con.close();
        textA.setText("Query realizada");

    } 
    public void insertar_registro(Connection con,String matricula,String nombres,String exa1,String exa2,String exa3)  throws SQLException {
        /**
         * Recibe la conexion para realizar la consulta de los registro en la base de datos
         */
    consulta_bd = verificar_registro_existente(con,matricula);
    if(consulta_bd > 0){
        JOptionPane.showMessageDialog(null, "Esta matricula ya existe en la base de datos");
    }else{
        if(matricula.equals("")){
        JOptionPane.showMessageDialog(null, "Debe ingresar una matricula");
        }else if(nombres.equals("")){
            JOptionPane.showMessageDialog(null, "Debe ingresar el Nombre");
        }else{
           String sql = "INSERT INTO lista (MATRICULA,NOMBRES,EXA1,EXA2,EXA3)" +  "VALUES" + "('"+ matricula +"','" + nombres +"','" + exa1 +"','" + exa2 +"','" + exa3 + "')";
            // Create a statement Object
                Statement stmt = con.createStatement();
            // Execute a query returning a result set
                stmt.executeUpdate(sql);
            // Close Statement to release database resources
                stmt.close();
            //Close Connection to release database resources
                con.close();
            //Ocultando el boton insertar
            limpiar_jtext();
            submit_insertar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Elumno Registrado"); 
        }
    }
    }
    public void eliminar_registro(Connection con,String matricula)  throws SQLException {
        /**
         * Recibe la conexion para realizar la consulta de los registro en la base de datos
         */
    consulta_bd = verificar_registro_existente(con,matricula);
    if(consulta_bd == 0){
        JOptionPane.showMessageDialog(null, "La Matricula no existe");
    }else{
            if(matricula.equals("")){
            JOptionPane.showMessageDialog(null, "Debe ingresar una matricula");
        }else{
            String sql = "DELETE * FROM lista WHERE MATRICULA ='"+ matricula + "';";
                // Create a statement Object
                    Statement stmt = con.createStatement();
                // Execute a query returning a result set
                    stmt.executeUpdate(sql);
                // Cerrando Statement 
                    stmt.close();
                //Cerrando la conexion
                    con.close();
                    boton_submit_eliminar.setVisible(false);
                   limpiar_jtext();
                    JOptionPane.showMessageDialog(null, "Elumno eliminado");
        }
    }
    
    } //Fin funcion eliminar_registro

    public void limpiar_jtext(){
         //lIMPIANDO JTEXT
        text_matricula_eliminar.setText("");
        text_matricula.setText("");
        text_nombres.setText("");
        text_exa1.setText("");
        text_exa2.setText("");
        text_exa3.setText("");
    }
    public void insertar_registro(Connection con) throws SQLException{

        
        /**
         * Agregar personalizacion a los botones
         */
        

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