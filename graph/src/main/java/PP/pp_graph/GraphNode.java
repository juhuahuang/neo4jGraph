package PP.pp_graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class GraphNode extends Node {
	private final String primary_index;
	public HashMap<String,String> properties;
	public HashSet<GraphNode> neighbors;

	
	private Iterator<GraphNode> neighborIt ;
	
	public GraphNode(){
		properties = new HashMap<String,String>();
		neighbors = new  HashSet<GraphNode> ();
		primary_index = "0";
		neighborIt = neighbors.iterator();
	}
	public GraphNode(String[] key, String[] val){
		properties = new HashMap<String,String>();
		neighbors = new  HashSet<GraphNode> ();
		int i = 0;
		while( i < key.length){
			properties.put(key[i], val[i]);
			i++;
		}
		primary_index = key[0];
	}
	
	public void addNeighbor(GraphNode neighbor){
		this.neighbors.add(neighbor);
		
	}
	
	public String get_primary_key(){
		return properties.get(primary_index);
	}
	//todo
	
	public void refreshNeighborIter(){
		neighborIt = neighbors.iterator();
	}
	
	public boolean hasNextNeighbor(){
		return neighborIt.hasNext();
	}
	public GraphNode nextNeighor(){
		GraphNode neighbor = null;
		if(neighborIt.hasNext() ){
			neighbor = neighborIt.next();
		}

		return neighbor;
	}

	@Override
	public boolean equals(Object ob){
		try{
			if( !(ob instanceof  GraphNode) || ob == null) return false;
			if( ob == this) return true;
			GraphNode target = (GraphNode)ob;
			return this.get_primary_key() == target.get_primary_key();
			//
			
		}
		catch(Exception ex){
			//todo implementation
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return this.get_primary_key().hashCode();
	}
	
	

}
