package PP.pp_graph;

import java.util.Properties;

import iot.jcypher.database.DBAccessFactory;
import iot.jcypher.database.DBProperties;
import iot.jcypher.database.DBType;
import iot.jcypher.database.IDBAccess;

public class NeoGraph extends ppGraph{
	private Neo4jConnection neo4j;
	
	public  NeoGraph(){
		neo4j = new Neo4jConnection();
		
	}
	
	public void write2Neo4j(){
		while(this.graph.contains("xxx")){
			
		}
		
		
	}

}
