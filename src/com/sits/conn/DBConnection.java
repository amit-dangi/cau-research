package com.sits.conn;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.sits.general.ReadProps;


public class DBConnection {
	private static Connection con = null;
	private DBConnection(){}
	public static void initFile() {

		try	{
			String driver = ReadProps.getkeyValue("db.driver","sitsResource");
			String url = ReadProps.getkeyValue("db.url","sitsResource");
			String username = ReadProps.getkeyValue("db.username","sitsResource");
			String password = ReadProps.getkeyValue("db.password","sitsResource");
			//System.out.println(url+"|"+username+"|"+password);
			Context ctx = null;
			//OracleDataSource ods = null;
			MysqlDataSource mds = null;
			ctx = new InitialContext();
			//ods = new OracleDataSource();
			mds = new MysqlDataSource();
			mds.setURL(url);
			mds.setUser(username);
			mds.setPassword(password);
			ctx.rebind("MyDS", mds);
		} catch (Exception e) {
			System.out.println("Error in Connection [01] : "+e.getMessage());	
		}	
	}

	public static Connection getConnection(){
		Context ctx = null;
		//OracleDataSource ods = null;
		MysqlDataSource mds = null;
		try {
			initFile();
			ctx = new InitialContext();
			//ods = new OracleDataSource();
			//ods = (OracleDataSource)ctx.lookup("MyDS");
			mds = new MysqlDataSource();
			mds = (MysqlDataSource)ctx.lookup("MyDS");
			//con = ods.getConnection();
			con = mds.getConnection();
		} catch (Exception e) {
		  e.printStackTrace();	
		}
		return con;
	}

	//----------------------------------------
	public static Connection getConnection(String schemaName){
		  Context ctx = null;
		  //OracleDataSource ods = null;
		  MysqlDataSource mds = null;
		  try {
		   initFile(schemaName);
		   ctx = new InitialContext();
		   //ods = new OracleDataSource();
		   //ods = (OracleDataSource)ctx.lookup("MyDS");
		   mds = new MysqlDataSource();
		   mds = (MysqlDataSource)ctx.lookup("MyDS1");
		   //con = ods.getConnection();
		   con = mds.getConnection();
		  } catch (Exception e) {
		    e.printStackTrace(); 
		  }
		  return con;
		 }

		 public static void initFile(String dbUrl) {

		  try {
		   
		   String url =""; 
		   String username =""; 
		   String password =""; 
	       url = dbUrl;
	       username = ReadProps.getkeyValue("db.username","sitsResource");
	       password =ReadProps.getkeyValue("db.password","sitsResource");
		   
		   //System.out.println(url+"|"+username+"|"+password);
		   Context ctx = null;
		   //OracleDataSource ods = null;
		   MysqlDataSource mds = null;
		   ctx = new InitialContext();
		   //ods = new OracleDataSource();
		   mds = new MysqlDataSource();
		   mds.setURL(url);
		   mds.setUser(username);
		   mds.setPassword(password);
		   ctx.rebind("MyDS1", mds);
		  } catch (Exception e) {
		   System.out.println("Error in Connection [01] : "+e.getMessage()); 
		  } 
		 }
	
	//-------------------------
	public static Connection getConnection1(){
		try{
			if((con==null)||con.isClosed()){
				getConnection();
		    }
		}catch(Exception e){
			System.out.println("Error in Connection getConnection() : "+e.getMessage());
		}
		return con;		
	}

	public static void closeConnection(){
		try{
			con.close();
		}catch(Exception ex){
			System.out.println("Error in connection closeConnection(): "+ex.getMessage());
		}
	}	

}
