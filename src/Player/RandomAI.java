package Player;
import Board.*;
import java.util.Random;

public class RandomAI extends Player {
	public int[] makeNextMove(int[][] validMoves, Tile tile, Coordinates board) {
		if(validMoves.length == 0) {
                    return new int[] {0,0,0,0,0,1};
                }
                int[] move = new int[6];//x,y,rotation,tiger,croc(0=no place)(1-9=section),pass
		int numMoves = validMoves.length;
                Random rand = new Random();
                int randomMove = rand.nextInt(validMoves.length);
                move[0] = validMoves[randomMove][0];
		move[1] = validMoves[randomMove][1];
		move[2] = validMoves[randomMove][2];
		move[3] = 0;//no tiger
		move[4] = 0 ;//no croc
		move[5] = 0;//dont pass
		return move;
        }     
}