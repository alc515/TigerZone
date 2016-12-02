package Board;

public class BoardSpace {
    
    public boolean hasTile;
    public Tile tile;
    boolean AIflag;
    
    int x;
    int y;
    
    public BoardSpace() {
        hasTile = false;
    }
    
    public BoardSpace(Tile tileToAdd) {
    	tile = tileToAdd;
        hasTile = true;
    }
    
    public void addTile(Tile tileToAdd){
        tile = tileToAdd;
        hasTile = true;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }

    public void flag() {
	AIflag = true;
		
    }

    public Tile getTile() {
	return tile;
    }
    
    public int getTileRotation(){
        if(hasTile)
            return tile.getRoto();
        else
            return -1;
    }


}
