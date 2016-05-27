package PP.pp_graph;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import iot.jcypher.database.DBAccessFactory;
import iot.jcypher.database.DBProperties;
import iot.jcypher.database.DBType;
import iot.jcypher.database.IDBAccess;
import iot.jcypher.database.remote.RemoteDBAccess;
import iot.jcypher.database.embedded.EmbeddedDBAccess;
import iot.jcypher.database.embedded.InMemoryDBAccess;
import iot.jcypher.query.JcQueryResult;
import iot.jcypher.query.api.*;
import iot.jcypher.query.ast.*;
import iot.jcypher.query.factories.clause.*;
import iot.jcypher.query.result.JcError;
import iot.jcypher.query.values.JcNode;
import iot.jcypher.graph.GrNode;
import iot.jcypher.graph.GrRelation;
import iot.jcypher.graph.Graph;




public class Neo4jConnection {
	private IDBAccess dbAccess ;
	private Graph neoGraph;
	public Neo4jConnection(){}
	
	
	public Neo4jConnection(DBType db_type, String db_mode, String db_path,String userId, String passwd){
		
		Properties props = new Properties();
		props.setProperty(db_mode, db_path);
		Class<? extends IDBAccess> dbAccessClass = null;
		IDBAccess dbAccess = null;
		try {
						if (db_type == DBType.REMOTE) {
							dbAccessClass =
									(Class<? extends IDBAccess>) Class.forName("iot.jcypher.database.remote.RemoteDBAccess");
						} else if (db_type == DBType.EMBEDDED) {
					dbAccessClass =
						(Class<? extends IDBAccess>) Class.forName("iot.jcypher.database.embedded.EmbeddedDBAccess");
				} else if (db_type == DBType.IN_MEMORY) {
							dbAccessClass =
								(Class<? extends IDBAccess>) Class.forName("iot.jcypher.database.embedded.InMemoryDBAccess");
					}
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
		if( dbAccessClass != null){
			try{
				Method init = dbAccessClass.getDeclaredMethod("initialize",new Class[] {Properties.class});
				dbAccess = dbAccessClass.newInstance();
				init.invoke(dbAccess, new Object[] {props});
				
			}
			catch(Throwable e){
				throw new RuntimeException(e);
			}
		}
		dbAccess = DBAccessFactory.createDBAccess(db_type, props);
		neoGraph = Graph.create(dbAccess);
	}

/*
 * #todo add HashMap<GraphNode, GrNode> graphNodeFindNeoNode
 *       complete createGraphEdgeFromGraphModel

*/ 
	
	public void createGraphFromGraphModel(ppGraph graph){
		HashMap<GraphNode, GrNode> graphNodeFindNeoNode = new HashMap<GraphNode, GrNode>();
		createNodeFromGraphModel(graph,graphNodeFindNeoNode );
		createEdgeFromGraphModel(graph, graphNodeFindNeoNode);
		
	}
	private void createNodeFromGraphModel(ppGraph graph, HashMap<GraphNode, GrNode> graphNodeFindNeoNode ){
		Iterator<GraphNode> it = graph.graph.iterator();
		while( it.hasNext()){
			GrNode neoNode = neoGraph.createNode();
			GraphNode node = it.next();
			neoNode.addLabel(graph.nodeLabel);
			HashMap<String,String> properties = node.properties;
			Iterator<Entry<String, String>> propertiesIt = properties.entrySet().iterator();
			while(propertiesIt.hasNext()){
				HashMap.Entry pair = propertiesIt.next();
				neoNode.addProperty((String) pair.getKey(), pair.getValue());
			}

			
			graphNodeFindNeoNode.put(node,neoNode);
			
			List<JcError> errors = neoGraph.store();
			if (!errors.isEmpty())
				printErrors(errors);
			
		}

	}
	
	private void createEdgeFromGraphModel(ppGraph graph, HashMap<GraphNode, GrNode> graphNodeFindNeoNode ){
		Iterator<GraphNode> it = graph.graph.iterator();
		while( it.hasNext()){
			GraphNode parent = it.next();
			parent.refreshNeighborIter();
			while( parent.hasNextNeighbor() ){
				GraphNode neighbor = parent.nextNeighor();
				GrRelation rel = neoGraph.createRelation(graph.edgeLabel,  graphNodeFindNeoNode.get(parent),graphNodeFindNeoNode.get(neighbor));
				Iterator<Entry<String, String>> parentPropertiesIt = parent.properties.entrySet().iterator();
				while( parentPropertiesIt.hasNext()){
					HashMap.Entry pair = parentPropertiesIt.next();
					if( ((String)pair.getValue()).equals(neighbor.properties.get(pair.getKey()))){
						rel.addProperty((String)pair.getKey(), (String)pair.getValue());
					}
					
				}
			}
			List<JcError> errors = neoGraph.store();
			if (!errors.isEmpty())
				printErrors(errors);
		}	
	}	
	
//#####################################################################################################################
	/**
	 * print errors to System.out
	 * @param result
	 */
	private static void printErrors(JcQueryResult result) {
		StringBuilder sb = new StringBuilder();
		sb.append("---------------General Errors:");
		appendErrorList(result.getGeneralErrors(), sb);
		sb.append("\n---------------DB Errors:");
		appendErrorList(result.getDBErrors(), sb);
		sb.append("\n---------------end Errors:");
		String str = sb.toString();
		System.out.println("");
		System.out.println(str);
	}
	
	/**
	 * print errors to System.out
	 * @param result
	 */
	private static void printErrors(List<JcError> errors) {
		StringBuilder sb = new StringBuilder();
		sb.append("---------------Errors:");
		appendErrorList(errors, sb);
		sb.append("\n---------------end Errors:");
		String str = sb.toString();
		System.out.println("");
		System.out.println(str);
	}
	
	private static void appendErrorList(List<JcError> errors, StringBuilder sb) {
		int num = errors.size();
		for (int i = 0; i < num; i++) {
			JcError err = errors.get(i);
			sb.append('\n');
			if (i > 0) {
				sb.append("-------------------\n");
			}
			sb.append("codeOrType: ");
			sb.append(err.getCodeOrType());
			sb.append("\nmessage: ");
			sb.append(err.getMessage());
			if (err.getAdditionalInfo() != null) {
				sb.append("\nadditional info: ");
				sb.append(err.getAdditionalInfo());
			}
		}
	}
	
	
	
}
