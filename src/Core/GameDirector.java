package Core;

import Server.TileConvertor;
import Board.*;
import Player.AI;
import Server.ServerInterface;
import Server.StartingInfo;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameDirector {
        public static final int GAME_A = 0;
        public static final int GAME_B = 1;
        
        Game[] game;
        Tile[] tiles;
        AI player = new AI();
        ServerInterface server;
        
        String playerName;
        String opName;
        String challengeID;
        
        int numOfMatches;
        
        public static void main(String args[]) {
        //System.out.println(TileScrambler.getTileString());
        GameDirector gameDir = new GameDirector();
        gameDir.startGame();
        }
        
        public GameDirector(){
            StartingInfo start = new StartingInfo();
            start.setVisible(true);
            while(!(start.confirmed)){
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameDirector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            server = new ServerInterface();
            server.startClient(start.getIP(), start.getPort());
            server.joinGame(start.getTPass(),start.getUser(), start.getPass());
            playerName = server.getplayerID();
            //System.out.println("--Got Player ID--");
            challengeID = server.getchallengeID();
            //System.out.println("--Got Challenge ID--");
        }
        
        public void startGame() {
            //System.out.println("--Starting Challenge--");
            numOfMatches = server.getNumberOfMatches();
            game = new Game[numOfMatches];
            for(int i = 0; i < numOfMatches; i++){
                game[i] = new Game();
                startMatch(i);
            }
        }
        
        public void startMatch(int gameOn) {
            System.out.println("--Starting Match--");
            tiles = server.getTileSet();
            System.out.println("--Recieved Tiles--");
            game[gameOn].startGame(tiles);
            while(!server.getEndRound()) {
                System.out.println("STARTING NEW ROUND");
                for (int i = 1; i <= tiles.length/2; i++) {
                    if(!makeMove(gameOn, (i * 2) - 1, 0))
                        break;
                    
                    if(!makeMove(gameOn, (i * 2), 1))
                        break;
                }
            }
        }
        
        public boolean makeMove(int gameOn, int turn, int tpm) {
            //System.out.println("MAKE MOVE " + turn + "GAME ON: " + gameOn);
            int opPlayerMove = (tpm == 0) ? 1 : 0;
            Tile nextTile = tiles[turn - 1];
            handlePlayerMove(nextTile, gameOn, turn, tpm);
            return handleOpMove(nextTile, gameOn, turn, opPlayerMove);
        }
        
        public void handlePlayerMove(Tile nextTile, int gameOn, int turn, int tpm){
            //System.out.println("Handle Player Move ");
            int[] playerMove; //Get Player Move
            playerMove = game[gameOn].getAIMove(nextTile, tpm);
            server.makeMove(playerMove, nextTile, tpm, turn);
            processMove(playerMove, nextTile, tpm, gameOn, false);
        }
        
        public boolean handleOpMove(Tile nextTile, int gameOn, int turn, int opm) {
            //System.out.println("Handle Op Move");
            int opPlayerMove = opm;
            int[] opMove; //Get Opponent Move
            //System.out.println("About To Get Op Move From Server");
            opMove = server.getOpMove(turn);
            //System.out.println("Finished Computer Move");
            if(opMove == null || opMove[0] == -999) {
                System.out.println("Opponent Forfeited!");
                return false;
            }
            processMove(opMove, nextTile, opPlayerMove, gameOn, true);
            //System.out.println("Computer Move Processed");
            return true;
        }

        public void processMove(int[] gameMove, Tile moveTile, int gameNum, int gameOn, boolean isComp) { //0 is Game A, 1 is Game B  
            //(String tile, int x, int y, int rotation, int tigerLocation, boolean crocodile, int game)
            //System.out.println("Process Move");
            Tile turnTile = moveTile;
            int x = gameMove[0];
            int y = gameMove[1];
            int rotation = gameMove[2];
            if (gameMove[5] == 1 || gameMove[5] == 2 || gameMove[5] == 3) { //The Player Passed
                if (gameMove[5] == 2) {
                    if(gameNum == 0 && game[gameOn].gameA.boardSpace[x][y].hasTile){
                        if(isComp)
                            game[gameOn].gameA.boardSpace[x][y].tile.removeOppTiger();
                        else
                            game[gameOn].gameA.boardSpace[x][y].tile.removeTiger();
                    }
                    if(gameNum == 1 && game[gameOn].gameB.boardSpace[x][y].hasTile){
                        if(isComp)
                            game[gameOn].gameB.boardSpace[x][y].tile.removeOppTiger();
                        else
                            game[gameOn].gameB.boardSpace[x][y].tile.removeTiger();
                    }  
                } else if (gameMove[5] == 3) {
                    if(gameNum == 0 && game[gameOn].gameA.boardSpace[x][y].hasTile){
                        if(isComp)
                            game[gameOn].gameA.boardSpace[x][y].tile.placeOppTiger(gameMove[3]);
                        else
                            game[gameOn].gameA.boardSpace[x][y].tile.placeTiger(gameMove[3]);
                    }
                    if(gameNum == 1 && game[gameOn].gameB.boardSpace[x][y].hasTile){
                        if(isComp)
                            game[gameOn].gameB.boardSpace[x][y].tile.placeOppTiger(gameMove[3]);
                        else
                            game[gameOn].gameB.boardSpace[x][y].tile.placeTiger(gameMove[3]);
                    }
                }
            } else { //The Player Didn't Pass
                if (gameMove[3] != 0) {
                    if(isComp){
                        turnTile.placeOppTiger(gameMove[3]);
                    }
                    else
                        turnTile.placeTiger(gameMove[3]);
                }
                if (gameMove[4] == 1) {
                    turnTile.placeCroc();
                }
                
                if(gameNum == 0) {
                    game[gameOn].gameA.addTile(x,y, turnTile);
                    while(game[gameOn].gameA.getTileRotation(x,y) != rotation) {
                        game[gameOn].gameA.rotateTile(x,y);
                    }
                    //System.out.println("On Board " + game[gameOn].gameA.boardSpace[gameMove[0]][gameMove[1]].tile.getRoto());
                }
                else if(gameNum == 1) {
                    game[gameOn].gameB.addTile(x,y, turnTile);
                    while(game[gameOn].gameB.getTileRotation(x,y) != rotation) {
                        game[gameOn].gameB.rotateTile(x,y);
                    }
                   //System.out.println("On Board " + game[gameOn].gameB.boardSpace[gameMove[0]][gameMove[1]].tile.getRoto());
                }
            }
        }
}
        
      