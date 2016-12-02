package Board;
import java.util.Queue;

import Player.Player;



public class Counting {/*
	public int CountAnimalsOnGT(BoardSpace root, Coordinates board){
			int count = 0;
		    Queue<BoardSpace> queue = new java.util.LinkedList<BoardSpace>();
		    queue.offer(root);
		    while(!queue.isEmpty()){
		    	BoardSpace node = queue.poll();
		    	if (node.tile.getMiddle() == 4 || node.tile.getMiddle() == 6 || node.tile.getMiddle() == 7){
		    		count++;
		    	}
		        node.AIflag = true;
		        if (node.tile.l.edgeType == 3 && board.left(node).hasTile && !board.left(node).AIflag){
		        	queue.offer(board.left(node));
		        }
		        if (node.tile.t.edgeType == 3 && board.top(node).hasTile && !board.top(node).AIflag){
		        	queue.offer(board.top(node));
		        }
		        if (node.tile.r.edgeType == 3 && board.right(node).hasTile && !board.right(node).AIflag){
		        	queue.offer(board.right(node));
		        }
		        if (node.tile.b.edgeType == 3 && board.bottom(node).hasTile && !board.bottom(node).AIflag){
		        	queue.offer(board.bottom(node));
		        }		        		    
		    }
		board.resetFlags();
		return count;
		}*/
  
	
	public int checkLakeCompleteion(BoardSpace root, Coordinates board, Player player){
		
	    Queue<BoardSpace> queue = new java.util.LinkedList<BoardSpace>();
	    queue.offer(root);
	    int tigerCount = 0;
	    while(!queue.isEmpty()){
	    	BoardSpace node = queue.poll();
	    	if (node.tile.r.edgeType == 1 && !board.right(node).hasTile){ 
	    		return -1;
	    	}
	    	if (node.tile.l.edgeType == 1 && !board.left(node).hasTile){ 
	    		return -1;
	    	}
	    	if (node.tile.b.edgeType == 1 && !board.bottom(node).hasTile){ 
	    		return -1;
	    	}
	    	if (node.tile.t.edgeType == 1 && !board.top(node).hasTile){ 
	    		return -1;
	    	}
	        node.AIflag = true;
	        tigerCount += node.tile.numTigers;
	        
	        if (node.tile.l.edgeType == 1 && board.left(node).hasTile && !board.left(node).AIflag){
	        	if(board.left(node).tile.m.edgeType != 2){
	        		queue.offer(board.left(node));
	        	}
	        	board.left(node).AIflag = true;
	        	tigerCount += node.tile.numTigers;
	        }
	        if (node.tile.t.edgeType == 1 && board.top(node).hasTile && !board.top(node).AIflag){
	        	if(board.top(node).tile.m.edgeType != 2){
	        		queue.offer(board.top(node));
	        	}
	        	board.top(node).AIflag = true;
	        	tigerCount += node.tile.numTigers;
	        }
	        if (node.tile.r.edgeType == 1 && board.right(node).hasTile && !board.right(node).AIflag){
	        	if(board.right(node).tile.m.edgeType != 2){
	        		queue.offer(board.right(node));
	        	}	        	
	        	board.right(node).AIflag = true;
	        	tigerCount += node.tile.numTigers;
	        }
	        if (node.tile.b.edgeType == 1 && board.bottom(node).hasTile && !board.bottom(node).AIflag){
	        	if(board.bottom(node).tile.m.edgeType != 2){
	        		queue.offer(board.bottom(node));
	        	}
	        	board.bottom(node).AIflag = true;
	        	tigerCount += node.tile.numTigers;
	        }		        		    
	    }
	    board.resetFlags();
	    player.getBackTiger(tigerCount);
		return tigerCount;
	}
	/*
	public int CountUniqueAnimalsOnLake(BoardSpace root, Coordinates board){
		int count = 0;
		int buffalo = 1;
		int boar = 1;
		int deer =1;
	    Queue<BoardSpace> queue = new java.util.LinkedList<BoardSpace>();
	    queue.offer(root);
	    while(!queue.isEmpty() && count!=3){
	    	BoardSpace node = queue.poll();
	    	if (node.tile.getMiddle() == 4){ 
	    		count+=buffalo;
	    		buffalo=0;
	    	}	    		
	    	else if (node.tile.getMiddle() == 6 ){
	    		count+=deer;
	    		deer=0;
	    	}
	    	else if(node.tile.getMiddle() == 7){
	    		count+=boar;
	    		boar = 0;
	    	}
	        node.AIflag = true;
	        if (node.tile.l.edgeType == 1 && board.left(node).hasTile && !board.left(node).AIflag){
	        	if(board.left(node).tile.m.edgeType != 2){
	        		queue.offer(board.left(node));
	        	}
	        	else if (board.left(node).tile.getMiddle() == 4){ 
		    		count+=buffalo;
		    		buffalo=0;
		    	}	    		
		    	else if (board.left(node).tile.getMiddle() == 6 ){
		    		count+=deer;
		    		deer=0;
		    	}
		    	else if (board.left(node).tile.getMiddle() == 7){
		    		count+=boar;
		    		boar = 0;
		    	}
	        	board.left(node).AIflag = true;
	        }
	        if (node.tile.t.edgeType == 1 && board.top(node).hasTile && !board.top(node).AIflag){
	        	if(board.top(node).tile.m.edgeType != 2){
	        		queue.offer(board.top(node));
	        	}
	        	else if (board.top(node).tile.getMiddle() == 4){ 
		    		count+=buffalo;
		    		buffalo=0;
		    	}	    		
		    	else if (board.top(node).tile.getMiddle() == 6 ){
		    		count+=deer;
		    		deer=0;
		    	}
		    	else if (board.top(node).tile.getMiddle() == 7){
		    		count+=boar;
		    		boar = 0;
		    	}
	        	board.top(node).AIflag = true;
	        }
	        if (node.tile.r.edgeType == 1 && board.right(node).hasTile && !board.right(node).AIflag){
	        	if(board.right(node).tile.m.edgeType != 2){
	        		queue.offer(board.right(node));
	        	}
	        	else if (board.right(node).tile.getMiddle() == 4){ 
		    		count+=buffalo;
		    		buffalo=0;
		    	}	    		
		    	else if (board.right(node).tile.getMiddle() == 6 ){
		    		count+=deer;
		    		deer=0;
		    	}
		    	else if (board.right(node).tile.getMiddle() == 7){
		    		count+=boar;
		    		boar = 0;
		    	}
	        	board.right(node).AIflag = true;
	        }
	        if (node.tile.b.edgeType == 1 && board.bottom(node).hasTile && !board.bottom(node).AIflag){
	        	if(board.bottom(node).tile.m.edgeType != 2){
	        		queue.offer(board.bottom(node));
	        	}
	        	else if (board.bottom(node).tile.getMiddle() == 4){ 
		    		count+=buffalo;
		    		buffalo=0;
		    	}	    		
		    	else if (board.bottom(node).tile.getMiddle() == 6 ){
		    		count+=deer;
		    		deer=0;
		    	}
		    	else if (board.bottom(node).tile.getMiddle() == 7){
		    		count+=boar;
		    		boar = 0;
		    	}
	        	board.bottom(node).AIflag = true;
	        }		        		    
	    }
	    board.resetFlags();
	    return count;
	}


	public ArrayList<BoardSpace> FindPotentialSteal(Tile bs, Coordinates board){
		int[][] validMoves = board.getValidMoves(bs);
		ArrayList<BoardSpace> stealList = new ArrayList<BoardSpace>();
		for (int i = 0; i < validMoves.length; i++){
			BoardSpace nextValid = validMoves.get(i);
			
			// checks top*right top*left top*bottom
			if (nextValid.tile.t.edgeType == nextValid.tile.r.edgeType
				&& ((board.top(nextValid).tile.numTigers > 0 && board.right(nextValid).tile.numOppTigers > 0)
					 ||(board.top(nextValid).tile.numOppTigers > 0 && board.right(nextValid).tile.numTigers > 0))){
				stealList.add(nextValid);		
			}
			if (nextValid.tile.t.edgeType == nextValid.tile.l.edgeType
				&& ((board.top(nextValid).tile.numTigers > 0 && board.left(nextValid).tile.numOppTigers > 0)
					 ||(board.top(nextValid).tile.numOppTigers > 0 && board.left(nextValid).tile.numTigers > 0))){
				stealList.add(nextValid);
			}
			if (nextValid.tile.t.edgeType == nextValid.tile.b.edgeType
				&& nextValid.tile.t.edgeType == nextValid.tile.m.edgeType
				&& ((board.top(nextValid).tile.numTigers > 0 && board.bottom(nextValid).tile.numOppTigers > 0)
					 ||(board.top(nextValid).tile.numOppTigers > 0 && board.bottom(nextValid).tile.numTigers > 0))){
					stealList.add(nextValid);
			}
			
			// checks right*bottom right*left
			
			if (nextValid.tile.r.edgeType == nextValid.tile.b.edgeType
				&& ((board.right(nextValid).tile.numTigers > 0 && board.bottom(nextValid).tile.numOppTigers > 0)
					 ||(board.right(nextValid).tile.numOppTigers > 0 && board.bottom(nextValid).tile.numTigers > 0))){
					stealList.add(nextValid);		
			}
			if (nextValid.tile.r.edgeType == nextValid.tile.l.edgeType
				&& nextValid.tile.r.edgeType == nextValid.tile.m.edgeType
				&& ((board.right(nextValid).tile.numTigers > 0 && board.left(nextValid).tile.numOppTigers > 0)
					 ||(board.right(nextValid).tile.numOppTigers > 0 && board.left(nextValid).tile.numTigers > 0))){
					stealList.add(nextValid);
			}
			
			//checks bottom*left
			
			if (nextValid.tile.b.edgeType == nextValid.tile.l.edgeType
				&& ((board.bottom(nextValid).tile.numTigers > 0 && board.left(nextValid).tile.numOppTigers > 0)
					 ||(board.bottom(nextValid).tile.numOppTigers > 0 && board.left(nextValid).tile.numTigers > 0))){
					stealList.add(nextValid);		
				}			
		}
		return stealList;
	}*/
  

}
