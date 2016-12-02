package Board;

public class Edge {
	//1 = Lake
	//2 = Jungle
	//3 = game-trail
	//8 = den
        //9 = Jungle Trail Lake Connection
    
	int edgeType;
        
	public Edge(int type){
		edgeType = type;		
	}
        
        public int getEdge(){
            return edgeType;
        }
	
}