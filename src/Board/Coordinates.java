package Board;
import java.util.ArrayList;


public class Coordinates {
    private static final int NUM_TILES = 76;
    private static final int CENTER = 75;
    
    public ArrayList<BoardSpace> placedSpaces;
    public BoardSpace[][] boardSpace;
    public ArrayList <BoardSpace> incompleteDens;
        
    public Coordinates(){
        boardSpace = new BoardSpace[(NUM_TILES * 2)-1][(NUM_TILES * 2)-1];
        placedSpaces = new ArrayList<>();
        incompleteDens = new ArrayList<>();
        for (int i = 0; i < boardSpace.length; i++) {
            for (int j = 0; j < boardSpace.length; j++) {
                boardSpace[i][j] = new BoardSpace();
                boardSpace[i][j].x = i;
                boardSpace[i][j].y = j;
            }
        }
    }
    
    public BoardSpace[][] copy(){
    	BoardSpace[][] copy = new BoardSpace[NUM_TILES][NUM_TILES];
    	for (int i = 0; i < NUM_TILES; i++) {
            for (int j = 0; j < NUM_TILES; j++) {
            	if (boardSpace[i][j].hasTile){
            		copy[i][j] = new BoardSpace(boardSpace[i][j].tile);
            	}
            	else{
            		copy[i][j] = new BoardSpace();
            	}
                copy[i][j].x = i;
                copy[i][j].y = j;
            }
        }
    	return copy;
    }
    
    public void resetFlags(){
    	for (int i = 0; i < placedSpaces.size(); i++) {
    		BoardSpace bs = placedSpaces.get(i);
    		bs.AIflag = false;
    		top(bs).AIflag = false;
    		bottom(bs).AIflag = false;
    		left(bs).AIflag = false;
    		right(bs).AIflag = false;
    	}
    }
    
    public BoardSpace top(BoardSpace space){
        return boardSpace[space.x][space.y+1];
    }
    
    public BoardSpace bottom(BoardSpace space){
        return boardSpace[space.x][space.y-1];
    }
    
    public BoardSpace left(BoardSpace space){
        return boardSpace[space.x-1][space.y];
    }
    
    public BoardSpace right(BoardSpace space){
        return boardSpace[space.x+1][space.y];
    }
    
    public void rotateTile(int x, int y) {
        if(boardSpace[x][y].hasTile)
            boardSpace[x][y].tile.rotate();
    }
    
    public int getTileRotation(int x, int y) {
        if(boardSpace[x][y].hasTile)
            return boardSpace[x][y].getTileRotation();
        else
            return -1;
    }
    
    public void addTile(int x, int y, Tile tile){
            boardSpace[x][y].addTile(tile);
            placedSpaces.add(boardSpace[x][y]);
            if (tile.getMiddle()==8 && tile.numTigers > 0){
            	incompleteDens.add(boardSpace[x][y]);
            }
    }
//Returns Validity of "All, 0, 90, 180, 270" Rotations (If result[0] is true, one or more rotations are valid)
    public boolean[] checkAllRotations(int x, int y, Tile tile){ 
        boolean[] result = {false, false, false, false, false};
        for(int i = 1; i < 5; i++) {
            if ( validMove (x, y, tile) ) {
                result[0] = true;
                result[i] = true;
            }
            tile.rotate();
        }
        tile.rotate();
        return result;
    }
    // Looks at every tile adjacent to input tile and location to see if it is valid. 
    public boolean validMove(int x, int y, Tile tile){ 
	if (boardSpace[x][y].hasTile){
		System.out.println("There is already a tile here");
		return false;
	}
	if(boardSpace[x-1][y].hasTile && boardSpace[x-1][y].tile.r.edgeType != tile.l.edgeType){
		return false;
	}
	if(boardSpace[x+1][y].hasTile && boardSpace[x+1][y].tile.l.edgeType != tile.r.edgeType){
		return false;
	}
	if(boardSpace[x][y-1].hasTile && boardSpace[x][y-1].tile.b.edgeType != tile.t.edgeType){
		return false;
	}
	if(boardSpace[x][y+1].hasTile && boardSpace[x][y+1].tile.t.edgeType != tile.b.edgeType){
		return false;
	}
	return true;
}
    
    public boolean validMove(BoardSpace bs, Tile tile){ 
	if (bs.hasTile){
		System.out.println("There is already a tile here");
		return false;
	}
	if(top(bs).hasTile && top(bs).tile.b.edgeType != tile.t.edgeType){
		return false;
	}
	if(bottom(bs).hasTile && bottom(bs).tile.t.edgeType != tile.b.edgeType){
		return false;
	}
	if(left(bs).hasTile && left(bs).tile.r.edgeType != tile.l.edgeType){
		return false;
	}
	if(right(bs).hasTile && right(bs).tile.l.edgeType != tile.r.edgeType){
		return false;
	}
	return true;
}

    public int[][] getValidMoves(Tile tile) {
    	int[][] valid = new int[154][3];
    	for (int q = 0; q < valid.length; q++){
    		valid[q][0] = -1;
    	}
    	BoardSpace bs;
    	int count = 0;
        for (int j = 0; j < 4; j++){
            for(int i = 0; i < placedSpaces.size(); i++) {//Checks For Blanks Around Already Placed Tiles
            	bs = placedSpaces.get(i);
                if(!top(bs).hasTile && !top(bs).AIflag && validMove(top(bs), tile)){
                    valid[count][0]=top(bs).getX();
                    valid[count][1]=top(bs).getY();
                    valid[count][2]=tile.getRoto();
                    count++;
                }
                if(!bottom(bs).hasTile  && !bottom(bs).AIflag && validMove(bottom(bs), tile)){
                	valid[count][0]=bottom(bs).getX();
                    valid[count][1]=bottom(bs).getY();
                    valid[count][2]=tile.getRoto();
                    count++;
                    bottom(bs).flag();

                }
                if(!left(bs).hasTile && !left(bs).AIflag && validMove(left(bs), tile)){
                	valid[count][0]=left(bs).getX();
                    valid[count][1]=left(bs).getY();
                    valid[count][2]=tile.getRoto();
                    count++;
                    left(bs).flag();

                }
                if(!right(bs).hasTile  && !right(bs).AIflag && validMove(right(bs), tile)){
                	valid[count][0]=right(bs).getX();
                    valid[count][1]=right(bs).getY();
                    valid[count][2]=tile.getRoto();
                    count++;
                    right(bs).flag();
                    
                }
                
            }
            resetFlags();
            tile.rotate();
        }

        int [][] valid2= new int[count][3];
        for (int w = 0; w < count; w++){
        	valid2[w][0] = valid[w][0];
        	valid2[w][1] = valid[w][1];
        	valid2[w][2] = valid[w][2];
        } 
        return valid2;
    }
	
    public int getCenter() {
        return CENTER;
    }
}
