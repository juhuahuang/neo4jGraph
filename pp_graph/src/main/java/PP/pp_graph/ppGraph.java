package PP.pp_graph;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class ppGraph {
	
	protected  HashSet<GraphNode> graph ;
	protected String nodeLabel;
	protected String edgeLabel;
	public ppGraph(){
		
	}
	
	public ppGraph(ArrayList<String> node, ArrayList<String> val){
		
		
	}
	
	public ppGraph(ResultSet data, String nodeLabel, String edgeLabel) throws SQLException{
		graph = new HashSet<GraphNode>();
		this.nodeLabel = nodeLabel;
		this.edgeLabel = edgeLabel;
		Entry<String, String> connectedKey;
		ResultSetMetaData metaData =  data.getMetaData();
		int columnCount = metaData.getColumnCount();
		
		String[] columns = new String[columnCount];
		for( int i = 0;i<columnCount;++i){
			columns[i] = metaData.getColumnName(i+1);
		}
		
		Hashtable<Entry<String, String>, ArrayList<GraphNode>> connectedNodeDict = new Hashtable<Entry<String, String>,ArrayList<GraphNode> >();
		try{
				while(data.next()){
					String[] rowData = new String[columnCount];
					for( int i = 0;i<columnCount;++i){
						rowData[i] = data.getString(columns[i]);
					}
					GraphNode curNode = new GraphNode(columns,rowData);
					
					if( graph.contains(curNode)){
						throw new Exception();// graph can't have duplicate nodes;
					}
					
					try{
					graph.add(curNode);
					}
					catch(Exception ex){
						System.out.println(ex.getMessage());
					}
					ArrayList<GraphNode> childNodes = new ArrayList<GraphNode>();
					
					
					Iterator<Entry<String, String>> propertiesIt = curNode.properties.entrySet().iterator();
					while(propertiesIt.hasNext()){
						connectedKey = propertiesIt.next();
						if(connectedNodeDict.containsKey(connectedKey)){
							ArrayList<GraphNode> connectedNode = connectedNodeDict.get(connectedKey);
							childNodes.addAll(connectedNode);
							connectedNode.add(curNode);
							connectedNodeDict.put(connectedKey,  connectedNode );
						}
						else{
							
							connectedNodeDict.put(connectedKey,  new ArrayList<GraphNode>( Arrays.asList(curNode) ) );
						}
						
					}
					
					for(  int i = 0 ;i < childNodes.size(); ++i){
						if( childNodes.get(i) != curNode) {
							childNodes.get(i).addNeighbor(curNode);
							curNode.addNeighbor(childNodes.get(i));
						}
					}

			}
		}
			catch(Exception ex){
			//todo implementation	
				System.out.println(ex.getMessage());
			}
			
	}
	
	public void write2Neo4j(){};
	
	public void print_node(){
		System.out.println(graph);
		
	}
	
	
}
