package Player;


import java.util.ArrayList;
import Board.*;
import Server.TileConvertor;
import java.util.Random;

public class AI extends Player {
	public boolean singleAdjTileAbove(Coordinates board, int[][] validMoves, int i){
		if(board.boardSpace[validMoves[i][0]][validMoves[i][1]+1].hasTile
			&& !board.boardSpace[validMoves[i][0]][validMoves[i][1]-1].hasTile
			&& !board.boardSpace[validMoves[i][0]-1][validMoves[i][1]].hasTile
			&& !board.boardSpace[validMoves[i][0]+1][validMoves[i][1]].hasTile){
			return true;
		}
		else
			return false;
	}
	public boolean singleAdjTileBelow(Coordinates board, int[][] validMoves, int i){
		if(!board.boardSpace[validMoves[i][0]][validMoves[i][1]+1].hasTile 
			&& board.boardSpace[validMoves[i][0]][validMoves[i][1]-1].hasTile									
			&& !board.boardSpace[validMoves[i][0]-1][validMoves[i][1]].hasTile
			&& !board.boardSpace[validMoves[i][0]+1][validMoves[i][1]].hasTile){
			return true;
		}
		else
			return false;
	}
	public boolean singleAdjTileLeft(Coordinates board, int[][] validMoves, int i){
		if(!board.boardSpace[validMoves[i][0]][validMoves[i][1]+1].hasTile 
			&& !board.boardSpace[validMoves[i][0]][validMoves[i][1]-1].hasTile
			&& board.boardSpace[validMoves[i][0]-1][validMoves[i][1]].hasTile
			&& !board.boardSpace[validMoves[i][0]+1][validMoves[i][1]].hasTile){
			return true;
		}
		else
			return false;
	}
	public boolean singleAdjTileRight(Coordinates board, int[][] validMoves, int i){
		if(!board.boardSpace[validMoves[i][0]][validMoves[i][1]+1].hasTile 
			&& !board.boardSpace[validMoves[i][0]][validMoves[i][1]-1].hasTile
			&& !board.boardSpace[validMoves[i][0]-1][validMoves[i][1]].hasTile
			&& board.boardSpace[validMoves[i][0]+1][validMoves[i][1]].hasTile){
			return true;
		}
		else
			return false;
	}
	public boolean allLakeTile(Tile tile){
		if(tile.t.getEdge()==1 && tile.l.getEdge()==1 && tile.r.getEdge()==1 && tile.b.getEdge()==1){
			return true;
		}
		else
			return false;
	}
        public int[] makeNextMove(int[][] validMoves, Tile tile, Coordinates board){
            if(TileConvertor.tileToServerString(tile).equals("TJTT-") || TileConvertor.tileToServerString(tile).equals("TJTJ-")
                    || TileConvertor.tileToServerString(tile).equals("LJLJ-") || TileConvertor.tileToServerString(tile).equals("TTTT-")) {
                return new int[] {validMoves[0][0], validMoves[0][1], validMoves[0][2], 0, 0, 0};
            }
            try {
            int[] possibleMove = calculateNextMove(validMoves, tile, board);
            return possibleMove;
            } catch (IndexOutOfBoundsException e) {
                if( validMoves.length != 0 ) {
                    int[] move = new int[6];
                    Random rand = new Random();
                    int randomMove = rand.nextInt(validMoves.length);
                    move[0] = validMoves[randomMove][0];
                    move[1] = validMoves[randomMove][1];
                    move[2] = validMoves[randomMove][2];
                    move[3] = 0;//no tiger
                    move[4] = 0;//no croc
                    move[5] = 0;//dont pass
                    return move;
                }
                
            }
            System.out.println("Returned Null?");
            return null;
        }
        
	public int[] calculateNextMove(int[][] validMoves, Tile tile, Coordinates board) {
		int[] move = new int[6];//x,y,rotation,tiger,croc(0=no place)(1-9=section),pass
                int numMoves = validMoves.length;
		int dens = board.incompleteDens.size();
		//ArrayList<BoardSpace> stealList = new ArrayList<>();
		if (numMoves == 0) {
			if (numberOfTigers() > 0) {
				// check finished lakes for 1 opponent meeple and 1 owned meeple
				// place a meeple
				// else check roads place meeple there
				move[5]=1;//just pass for now
				return move;
			}
			else{
				move[5]=1;
				return move;//pass
			}
		}
		else if (numMoves == 1) {
			if(tile.getMiddle()==8){// return single move w/ tiger
				move[0]=validMoves[0][0];
				move[1]=validMoves[0][1];
				move[2]=validMoves[0][2];
				if(numberOfTigers() > 0){
					move[3]=5;//tiger in den
					board.incompleteDens.add(board.boardSpace[move[0]][move[1]]);//add den to list
				}
				else{
					move[3]=0;//no tiger in den, sad den
				}
				move[4]=0;//no croc
				move[5]=0;//dont pass
				return move;
			}
		}
		else {// numMoves is numerous and must be chosen carefully
			move[5]=0;//don't pass
			if(tile.getMiddle()==8){//if tile has a den
				move[0]=validMoves[0][0];
				move[1]=validMoves[0][1];
				move[2]=validMoves[0][2];
				if(numberOfTigers() > 0){
					move[3]=5;//tiger
					board.incompleteDens.add(board.boardSpace[move[0]][move[1]]);//add den coords to list
				}
				else
					move[3]=0;//no tiger playable
				move[4]=0;//no croc
				move[5]=0;//dont pass
				return move;
			}
			else {
				for(int i = 0; i <numMoves;i++){//checks each move for den adjacency or steal
					if(allLakeTile(tile)){
						if((singleAdjTileAbove(board, validMoves, i)
								&& !(board.boardSpace[validMoves[i][0]][validMoves[i][1]+1].getTile().m.getEdge()==1)
							||(singleAdjTileBelow(board, validMoves, i)
								&& !(board.boardSpace[validMoves[i][0]][validMoves[i][1]-1].getTile().m.getEdge()==1)
							||(singleAdjTileLeft(board, validMoves, i)
								&& !(board.boardSpace[validMoves[i][0]-1][validMoves[i][1]].getTile().m.getEdge()==1)
							||(singleAdjTileRight(board, validMoves, i)
								&& !(board.boardSpace[validMoves[i][0]+1][validMoves[i][1]].getTile().m.getEdge()==1)))))){
							//add (and has our tiger or doesn't have any tigers) and the function can be used 
							move[0]=validMoves[i][0];
							move[1]=validMoves[i][1];
							move[2]=validMoves[i][2];
							if(numberOfTigers() > 0){
								move[3]=1;//tiger at top left corner
							}
							else
								move[3]=0;//no tiger
							move[4]=0;//no croc
							move[5]=0;//dont pass
							return move;
						}
					}
					if(numberOfTigers() > 0){
						int lakeSides=0;
						int validSides=0;
						if(tile.t.getEdge()==1){
							lakeSides++;
						}
						if(!board.boardSpace[validMoves[i][0]][validMoves[i][1]+1].hasTile){
							validSides++;
						}
						if(tile.b.getEdge()==1){
							lakeSides++;
						}
						if(!board.boardSpace[validMoves[i][0]][validMoves[i][1]-1].hasTile){
							validSides++;
						}
						if(tile.l.getEdge()==1){
							lakeSides++;
						}
						if(!board.boardSpace[validMoves[i][0]-1][validMoves[i][1]].hasTile){
							validSides++;
						}
						if(tile.r.getEdge()==1){
							lakeSides++;
						}
						if(!board.boardSpace[validMoves[i][0]+1][validMoves[i][1]].hasTile){
							validSides++;
						}
						if(lakeSides == validSides){
							move[0] = validMoves[i][0];
							move[1] = validMoves[i][1];
							move[2] = validMoves[i][2];
							move[4] = 0;//no croc
							move[5] = 0;//dont pass
							if(tile.t.getEdge()==1){
								if(lakeSides==3){
									move[3]=1;
								}
								else
									move[3]=2;//earliest tiger at 2
                                                                        return move;
								}
								else if(tile.l.getEdge()==1){
									move[3]=4;
									return move;
								}
								else if(tile.r.getEdge()==1){
									move[3]=6;
									return move;
								}
								else if(tile.b.getEdge()==1){
									move[3]=8;
									return move;
								}	
						}
					else if(dens!=0){
						for(int j = 0; j<dens; j++){
							if(((board.incompleteDens.get(i).getX()==validMoves[i][0]+1
								||board.incompleteDens.get(i).getX()==validMoves[i][0]-1)//if valid move adjacent to any den in x
								||(board.incompleteDens.get(i).getY()==validMoves[i][1]+1//or y direction
								||board.incompleteDens.get(i).getY()==validMoves[i][1]-1))){//check for owned den, place adjacent to owned den
									move[0]=validMoves[i][0];
									move[1]=validMoves[i][1];
									move[2]=validMoves[i][2];
									move[3]=0;//no tiger
									move[4]=0;//no croc
									move[5]=0;//dont pass
						return move;
							}
						}
					}
					//look for unfinished lake w/o opponent tiger or croc
				}
			}
		//priority==dens(while up), lakes,  game trails, jungles
		//finish the unfinished->start new
				
                            }
                }
                if( validMoves.length != 0 ) {
                Random rand = new Random();
                int randomMove = rand.nextInt(validMoves.length);
                move[0] = validMoves[randomMove][0];
		move[1] = validMoves[randomMove][1];
		move[2] = validMoves[randomMove][2];
		move[3] = 0;//no tiger
		move[4] = 0;//no croc
		move[5] = 0;//dont pass
                }
                return move;
        }
        
}
