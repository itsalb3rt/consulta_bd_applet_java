import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class INS extends Frame implements ActionListener{
	Frame f;
	Label l1,l2;
	TextField t1,t2;
	Button b1,b2,b3,b4,b5;
	Connection c;
	Statement s;
	ResultSet r;
	INS (){
		try{
			f=new Frame();
			f.setLayout(null);
			f.setVisible(true);
			f.setSize(800,600);
			l1=new Label("ID");
			l1.setBounds(50, 100, 100, 50);
			f.add(l1);
			l2=new Label("Name");
			l2.setBounds(50, 150, 100, 50);
			f.add(l2);
			
			t1=new TextField();
			t1.setBounds(150, 100, 100, 40);
			f.add(t1);
			t2=new TextField();
			t2.setBounds(150, 150, 100, 40);
			f.add(t2);
			
			b1=new Button("Insertar");
			b1.setBounds(200,300,75,50);
			f.add(b1);
			b1.addActionListener(this);
			
			b2=new Button("Actualizar");
			b2.setBounds(300,300,75,50);
			f.add(b2);
			b2.addActionListener(this);
			
			b3=new Button("Eliminar");
			b3.setBounds(400,300,75,50);
			f.add(b3);
			b3.addActionListener(this);
			
			b5=new Button("Salir");
			b5.setBounds(600,300,75,50);
			f.add(b5);
			b5.addActionListener(this);
Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
c=DriverManager.getConnection("jdbc:odbc:curso1");
s=c.createStatement();
            }catch(Exception e){} 
			
}
public void actionPerformed(ActionEvent ae){
	try{
		if(ae.getSource()==b1){
		String s1="INSERT INTO USER(id,name) VALUES("+t1.getText()+",'"+t2.getText()+"";
		System.out.println(s1);
		s.executeUpdate(s1);
		r=s.executeQuery("select*FROM USER");
		t1.setText("");
		t2.setText("");
		
		}else if (ae.getSource()==b2){
			
		String s2="UPDATE USER SET NAME="+t2.getText()+"'WHERE ID="+t1.getText()+"";
		System.out.println(s2);
		s.executeUpdate(s2);
		r=s.executeQuery("select*FROM USER");
		t1.setText("");
		t2.setText("");
		
		}else if (ae.getSource()==b3){
        
		String s3="DELETE FROM USER WHERE ID="+t1.getText();
		System.out.println(s3);
		s.executeUpdate(s3);
		r=s.executeQuery("select*FROM USER");
		t1.setText("");
		t2.setText("");	
		
		}else if (ae.getSource()==b5){
			c.close();
			f.dispose();
			}
}catch (Exception e){} 

} 

public static void main(String[]args){
	new INS();
	
	}
}
		