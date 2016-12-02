package Player;

import Board.BoardSpace;
import Board.Coordinates;
import Board.Tile;
import java.util.List;
import java.util.ArrayList;
/*
 * Player is the parent class of the AI and Human 

 */
public abstract class Player {
        private static final int NUM_OF_TIGERS = 7;
        private static final int NUM_OF_CROCS = 2;
        private int tigers;
        private int crocodiles;
        
        public Player(){
            tigers = NUM_OF_TIGERS;
            crocodiles = NUM_OF_CROCS;
        }
    
        public int numberOfTigers(){
            return tigers;
        }
        
        public int numberOfCrocodiles() {
            return crocodiles;
        }

        public void useTiger() {
            tigers--;
        }
        
        public void useCroc(){
            crocodiles--;
        }
        
        public void getBackTiger(int i) {
            tigers += i;
        }


    
}