package PP.pp_graph;
import iot.jcypher.database.DBType;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.util.Properties;
import java.sql.ResultSet;

import iot.jcypher.database.DBAccessFactory;
import iot.jcypher.database.DBProperties;
import iot.jcypher.database.DBType;
import iot.jcypher.database.DBVersion;
import iot.jcypher.database.IDBAccess;

public class trial_graph {
	static IDBAccess dbAccess;
	
	public static void main(String[] args) throws SQLException{
		
		String sql = args[0];
		String graphNodeName = args[1];
		String graphEdgeName = args[2];
		
		String driver = "com.mysql.jdbc.Driver";
		 
		String url = "jdbc:mysql://10.45.237.101:3306/test?serverTimezone=UTC";
	 
		String user = "production";

		String password = "BEEQXXTGICARSCLU";
		 
		mysql_connection db = new mysql_connection(driver, url, user,password);
		
		ResultSet result = db.query(sql);
		/*
		try{
		while(result.next()){
			System.out.println(result.getLong("id"));
			System.out.println(result.getLong("property"));
			}
		}
		catch(Exception ex){
			
		}
		*/
		
		ppGraph g = new ppGraph();
		try{
		g = new ppGraph(result,graphNodeName,graphEdgeName);
		g.print_node();
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		DBType db_type = DBType.REMOTE;
		String db_mode = DBProperties.SERVER_ROOT_URI;
		String db_path = "http://localhost:7474/";
		Neo4jConnection neoConn = new Neo4jConnection( db_type,  db_mode,  db_path, "hjh", "hjh");
		neoConn.createGraphFromGraphModel(g);
		
		
	}
	
}
