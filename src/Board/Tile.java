package Board;

public class Tile {
        /*For Middle Feature Int
        0, Nothing Special
        4, Buffalo
        5, Crocodile
        6, Deer
        7, Boar
        8, Den
        */
        boolean hasCrocodile;
        int numTigers;
        int numOppTigers;
        int tigerSection; //0 if no tiger
        public int middleFeature;
        private int rotation;
	public Edge l, r, t, b, m;
        
	public Tile(int r, int t, int l, int b, int m, int mf){
		this.l=new Edge(l);
		this.r=new Edge(r);
		this.t=new Edge(t);
		this.b=new Edge(b);
		this.m=new Edge(m);
                rotation = 0;
                middleFeature = mf;
                hasCrocodile = false;
                numTigers = 0;
                numOppTigers = 0;
                tigerSection = 0;
                
	}
        
	public void rotate(){
            if(rotation == 270)
                rotation = 0;
            else
                rotation += 90;
            Edge temp = t;
            t = l;
            l = b;
            b = r;
            r = temp;
	}
        
        public int getMiddle() {
            return middleFeature;
        }
        
        public int tigerLocation(){
            return tigerSection;
        }
        
        public void placeTiger(int section) {
            numTigers++;
            tigerSection = section;
        }
        
        public void removeTiger() {
            numTigers--;
            tigerSection = -1;
        }
        
        public void placeOppTiger(int section) {
            numTigers++;
            numOppTigers++;
            tigerSection = section;
        }
        
        public void removeOppTiger() {
            numTigers--;
            numOppTigers--;
            tigerSection = -1;
        }
        
    	public int getRoto() {
            return rotation;
    	}
        
        public void placeCroc(){
            hasCrocodile = true;
        }

}   