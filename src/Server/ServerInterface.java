package Server;

import Board.Tile;
import java.io.BufferedInputStream;

import java.io.IOException; 
import java.io.DataOutputStream; 
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;  
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerInterface {
    public String lastMsg = "";
    public String recieved = "";
    boolean endOfRound = false;
    public String[] recievedSplit;
    
    public String IP;
    public int port;
    
    //TOKENS
    String tpass;
    String username;
    String password;
    String challenges;
    String cid; //Challenge ID
    String pid; //Current Player ID
    String game1ID = "";
    String game2ID = "";
    String curGameID = "";
    ArrayList<String> moves = new ArrayList<>();
    int numOfMatches = 0;
    private boolean listening = false;;
    
    Tile[] stack;
    
    Socket socket;
    BufferedInputStream input;
    InputStreamReader inRead;
    DataOutputStream output;
    
    
    Thread listenServerReply = new Thread(new Runnable() {   
        @Override
        public void run(){            
        listening = true;
                    while(listening) {
                        String textLine = "";
                        int character = 0;
                    try {
                        while((character = inRead.read()) != 13){
                            char value = (char)character;
                            textLine = textLine + value;
                            
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ServerInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        while(textLine != null && textLine.equals(lastMsg) == false){
                                    textLine = textLine.replaceAll("(\\r|\\n)", "");
                                    System.out.println("Server: " + textLine);
                                    if(textLine.substring(0,3).equals("GAM"))
                                        moves.add(textLine);
                                    if(textLine.substring(0,5).equals("END O")){
                                        endOfRound = true;
                                    }
                                    if(textLine.length() >= 11)
                                        if(textLine.substring(0,11).equals("MATCH BEGIN"))//That from the server causes a bug
                                            break;
                                        else
                                            lastMsg = textLine; 
                                    else
                                            lastMsg = textLine; //So we just disregard it                     
                                    
                        }
                    
                    }
                   
        }
    });
    
    public void startClient(String IP, int port){
        try {  
            socket = new Socket(InetAddress.getByName(IP), port);
            input = new BufferedInputStream(socket.getInputStream()); 
            output = new DataOutputStream(socket.getOutputStream()); 
            inRead = new InputStreamReader(input, "US-ASCII");
            listenServerReply.start();
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    public void waitFor(String string) {
        while(!recieved.equals(string)){
            recieved = lastMsg;
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        recievedSplit = recieved.trim().split("\\s++");
    }
    
    public void waitForSub(String substring){
        while(recieved.length() < substring.length()) {
            recieved = lastMsg;
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while(!recieved.substring(0, substring.length()).equals(substring)){
            recieved = lastMsg;
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        recievedSplit = recieved.trim().split("\\s++");
    }
    
    public void write(String message){
        try {
            output.writeBytes(message + "\n");
            //output.writeUTF(message);
            System.out.println("Client: " + message);
        } catch (IOException ex) {
            Logger.getLogger(ServerInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    public void joinGame(String tpass, String username, String password){
        System.out.println("--Waiting To Join Game--");
        waitFor("THIS IS SPARTA!");
        write("JOIN " + tpass);
        waitFor("HELLO!");
        write("I AM " + username + " " + password);
        
        
    }
    
    public String getplayerID() {
        waitForSub("WELCOME");
        pid = recievedSplit[1];
        return pid;
    }
    
    public String getchallengeID() {
        waitForSub("NEW CHALLENGE");
        numOfMatches = Integer.parseInt(recievedSplit[6]);
        cid = recievedSplit[2];
        return cid;
    }

    public int getNumberOfMatches() {
        return numOfMatches;
    }

    public Tile[] getTileSet() {
        waitForSub("THE REMAINING");
        endOfRound = false;
        return processTiles(recieved);
    }

    public Tile getNextTile() {
        waitForSub("MAKE YOUR");
        String NTID = recievedSplit[5];
        if (game1ID.equals(""))
            game1ID = NTID;
        else if (!game1ID.equals(NTID) && game2ID.equals(""))
            game2ID = NTID;
        curGameID = NTID;
        return TileConvertor.serverStringToTile(recievedSplit[12]);
    }

    public void makeMove(int[] nextMove, Tile currentTile, int game, int move){
        if(endOfRound)
            return;
        if(nextMove[2] == 270)
            nextMove[2] = 90;
        else if(nextMove[2] == 90)
            nextMove[2] = 270;
        
        String tileString = TileConvertor.tileToServerString(currentTile);
        String finalString = "";
        String moveString = " MOVE " + Integer.toString(move) + " ";
        if (nextMove[5] != 0) { //The player has to pass
            switch (nextMove[5]) {
                case 1:
                    //Pass No Tiger
                    finalString = "TILE " + tileString + " UNPLACEABLE PASS";
                    break;
                case 2:
                    //Pick Up Tiger Pass
                    finalString = "TILE " + tileString + " UNPLACEABLE RETRIEVE TIGER AT " + (nextMove[0] - 75) + " " + (nextMove[1] - 75);
                    break;
                case 3:
                    //Put Down Tiger Pass
                    finalString = "TILE " + tileString + " UNPLACEABLE ADD ANOTHER TIGER TO " + (nextMove[0] - 75) + " " + (nextMove[1] - 75);
                    break;
                default:
                    break;
            }

        } else { //The player doesn't pass
            if (nextMove[3] == 0 && nextMove[4] == 0) { //No tigers or crocs
                finalString = "PLACE " + tileString + " AT " + (nextMove[0] - 75) + " " + (nextMove[1] - 75) + " " + nextMove[2] + " NONE";
            } else if (nextMove[4] == 1) { //Place Crocodile
                finalString = "PLACE " + tileString + " AT " + (nextMove[0] - 75) + " " + (nextMove[1] - 75) + " " + nextMove[2] + " CROCODILE";
            } else if (nextMove[3] != 0) { //Place Tiger
                finalString = "PLACE " + tileString + " AT " + (nextMove[0] - 75) + " " + (nextMove[1] - 75) + " " + nextMove[2] + " TIGER " + nextMove[3];
            }
        }
        finalString = moveString + finalString;
        System.out.println();
        waitForSub("MAKE YOUR");
        if(move == 1)
            game1ID = recievedSplit[5];
        if(move % 2 == 1)
            write("GAME " + game1ID + finalString);
        else
            write("GAME " + game2ID + finalString);
    }
    
    public int[] getOpMove(int turn) {
        while(moves.size() != (turn * 2)){
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String gameID = (turn % 2 == 0) ? game2ID : game1ID; //Look At This Tommorrow
        String[] moveString1 = moves.get(moves.size()-1).trim().split("\\s++");
        String[] moveString2 = moves.get(moves.size()-2).trim().split("\\s++");
        if(turn == 1) {
            if(game2ID.equals("")){
                if(moveString1[1].equals(game1ID))
                    game2ID = moveString2[1];
                if(moveString2[1].equals(game1ID))
                    game2ID = moveString1[1];
            }
        }
        String oppoMove = "";
        if(moveString1[1].equals(gameID)) {
            oppoMove = moves.get(moves.size()-2);
        } else {
            oppoMove = moves.get(moves.size()-1);
        }
        return processOpMove(oppoMove);
    }
    
    public boolean getEndRound() {
        return endOfRound;
    }
    
    public int[] processOpMove(String computerMove){
        //System.out.println("Started Process Function");
        String[] splitMoves = computerMove.trim().split("\\s++");
        switch (splitMoves[6]) {
            case "PLACED":
            if(splitMoves[11].equals("90")){    //Our version of the game uses clockwise
                splitMoves[11] = "270";         //Rotation. This Corrects That
            } else if(splitMoves[11].equals("270")) {
                splitMoves[11] = "90";
            }
            switch (splitMoves[12]) {
                case "NONE":
                    return new int[] {(Integer.parseInt(splitMoves[9]) + 75), (Integer.parseInt(splitMoves[10]) + 75),
                        (Integer.parseInt(splitMoves[11])), 0, 0, 0};
                case "CROCODILE":
                    return new int[] {(Integer.parseInt(splitMoves[9]) + 75), (Integer.parseInt(splitMoves[10]) + 75),
                        (Integer.parseInt(splitMoves[11])), 0, 1, 0};
                case "TIGER":
                    return new int[] {(Integer.parseInt(splitMoves[9]) + 75), (Integer.parseInt(splitMoves[10]) + 75),
                        (Integer.parseInt(splitMoves[11])),(Integer.parseInt(splitMoves[13])) , 0, 0};
                default:
                    break;
            }
            
        break;
            case "TILE":
                if(splitMoves[9].equals("PASSED")){
                    return new int[] {0, 0, 0, 0, 0, 1};
                } else if(splitMoves[9].equals("RETRIEVED")) {
                    return new int[] {Integer.parseInt(splitMoves[12] + 75), (Integer.parseInt(splitMoves[13]) + 75), 0,0,0, 2};
                } else if(splitMoves[9].equals("ADDED")) {
                    return new int[] {Integer.parseInt(splitMoves[13]) + 75, (Integer.parseInt(splitMoves[14]) + 75), 0,0,0, 3};
                }   break;
            case "FORFEITED:":
                return new int[] {-999, 0, 0, 0, 0, 0};
            default:
                break;
        }
        return null; 
    }
    
    public Tile[] processTiles(String string) {
        int leftBracket = 0;
        for (int i = 0; i < 40; i++) { //Looks through for left bracket
            if (string.charAt(i) == '[') {
                leftBracket = i;
            }
        }
        string = string.substring(leftBracket + 1, string.length() - 1);//Removes Everything But The Tiles
        ArrayList<Tile> tiles = new ArrayList<>();
        String tempTile = "";
        for(int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' '){
                tempTile += string.charAt(i);
            }
            if (tempTile.length() == 5) {
                tiles.add(TileConvertor.serverStringToTile(tempTile));
                tempTile = "";
            }
        }
        Tile[] tileStackArray = tiles.toArray(new Tile[tiles.size()]);
        return tileStackArray;
    }
}