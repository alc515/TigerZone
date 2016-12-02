
package Core;

import Server.TileConvertor;
import Board.Coordinates;
import Board.Tile;
import Player.AI;
import java.util.ArrayList;
import java.util.Arrays;


public class Game {
        int numberOfTurns;
        
        ArrayList<Tile> tileStack;
        
        AI player;
        Coordinates gameA = new Coordinates();
        Coordinates  gameB = new Coordinates();
        
        public Game(){
            player = new AI();
            
            gameA.addTile(gameA.getCenter(), gameA.getCenter(), TileConvertor.serverStringToTile("TLTJ-")); //This puts the start tile on the board
            gameB.addTile(gameB.getCenter(), gameB.getCenter(), TileConvertor.serverStringToTile("TLTJ-")); //This puts the start tile on the board
        }
        
        public void startGame(Tile[] initialTiles){
            tileStack = new ArrayList<>(Arrays.asList(initialTiles));
            numberOfTurns = tileStack.size();
        }
        
        public int[] getAIMove(Tile tile, int tpm) {
            
            //System.out.println("TILE: " + TileConvertor.tileToServerString(tile));
            //System.out.println("GAME A VALID MOVES");
            //for (int[] validMove : gameA.getValidMoves(tile)) {
            //    for (int j = 0; j < 3; j++) {
            //        System.out.print(validMove[j] + " ");
            //    }
            //    System.out.println();
            //}
            //System.out.println("TILE: " + TileConvertor.tileToServerString(tile));
            //System.out.println("GAME B VALID MOVES");
            //for (int[] validMove : gameB.getValidMoves(tile)) {
            //    for (int j = 0; j < 3; j++) {
            //        System.out.print(validMove[j] + " ");
            //    }
            //   System.out.println();
            //}
            if(tpm == 0)
                return player.makeNextMove(gameA.getValidMoves(tile), tile, gameA);
            else
                return player.makeNextMove(gameB.getValidMoves(tile), tile, gameB);
        }
        
}
