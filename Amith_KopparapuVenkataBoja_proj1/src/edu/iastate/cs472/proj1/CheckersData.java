package edu.iastate.cs472.proj1;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 *  @author Amith Kopparapu Venkata Boja
 */
public class CheckersData {

  /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;


    int[][] board;  // board[r][c] is the contents of row r, column c.


    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }
    
    CheckersData(int[][] b){
    	board = new int[8][8];
    	for(int row =0; row < b.length; row++) {
    		for(int col = 0; col < b[0].length; col++) {
    			board[row][col] = b[row][col];
    		}
    	}
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == 0) {
                    sb.append(" ");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }

    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame() {
    	for(int row =0; row < board.length; row++) {
    		for(int col = 0; col < board[0].length; col++) {
    			board[row][col] = EMPTY;
    		}
    	}
    	for(int row =0; row < 3; row++) {
    		for(int col = 0; col < board[0].length; col++) {
    			if(row % 2 == col % 2) {
    				board[row][col] = BLACK;
    			}
    		}
    	}
    	for(int row = board.length-1; row > board.length-4; row--) {
    		for(int col = 0; col < board[0].length; col++) {
    			if(row % 2 == col % 2) {
    				board[row][col] = RED;
    			}
    		}
    	}
    }


    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }


    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     * @return  true if the piece becomes a king, otherwise false
     */
    boolean makeMove(CheckersMove move) {
        return makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     * @return        true if the piece becomes a king, otherwise false
     */
    boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Todo: update the board for the given move.
        // You need to take care of the following situations:
        // 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
        // 2. if this move is a jump, remove the captured piece
        // 3. if the piece moves into the kings row on the opponent's side of the board, crowned it as a king
    	if(Math.abs(toRow - fromRow) == 2) { // is a jump
    		if((toRow - fromRow)<0 && (board[fromRow][fromCol] == RED || (board[fromRow][fromCol] == RED_KING && toRow - fromRow < 0 ))) {
    			if(toCol - fromCol > 0) {
    				board[fromRow-1][fromCol+1]= EMPTY;
    			}else {
    				board[fromRow-1][fromCol-1]= EMPTY;
    			}
    		}
    		if(board[fromRow][fromCol] == RED_KING && (toRow - fromRow)>0) {
    			if(toCol - fromCol > 0) {
    				board[fromRow+1][fromCol+1]= EMPTY;
    			}else {
    				board[fromRow+1][fromCol-1]= EMPTY;
    			}
    		}
    		if((toRow - fromRow)>0 && (board[fromRow][fromCol] == BLACK || (board[fromRow][fromCol] == BLACK_KING && toRow - fromRow > 0 ))) {
    			if(toCol - fromCol > 0) {
    				board[fromRow+1][fromCol+1]= EMPTY;
    			}else {
    				board[fromRow+1][fromCol-1]= EMPTY;
    			}
    		}
    		if((toRow - fromRow)<0 && board[fromRow][fromCol] == BLACK_KING) {
    			if(toCol - fromCol > 0) {
    				board[fromRow-1][fromCol+1]= EMPTY;
    			}else {
    				board[fromRow-1][fromCol-1]= EMPTY;
    			}
    		}
    		
    	}
    	if(board[fromRow][fromCol] == RED && toRow == 0) { // if a red coin reaches the opponents base
    		board[toRow][toCol] = RED_KING;
    		board[fromRow][fromCol] = EMPTY;
    		return true;
    	}else if(board[fromRow][fromCol] == BLACK && toRow == board.length-1) { // if a black coin reaches the opponents base
    		board[toRow][toCol] = BLACK_KING;
    		board[fromRow][fromCol] = EMPTY;
    		return true;
    	}else {
    		board[toRow][toCol] = board[fromRow][fromCol];
    		board[fromRow][fromCol] = EMPTY;
    		return false;
    	}
    }

    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    CheckersMove[] getLegalMoves(int player) {
    	ArrayList<CheckersMove> jumpMoves = new ArrayList<CheckersMove>();
    	ArrayList<CheckersMove> regularMoves = new ArrayList<CheckersMove>();
    	
    	int jumpIndex = 0, regIndex = 0;
    	
    	if(player ==  BLACK) {
    		for(int row = 0; row < board.length; row ++) {
    			for(int col = 0; col < board[0].length; col++) {
    				if(board[row][col]== BLACK || board[row][col]== BLACK_KING) { // the two ways any black coin can go down in a game
    					CheckersMove m;
    					if(row < board.length-1  && col < board[0].length-1 && board[row + 1][col + 1]==EMPTY) {
    						m = new CheckersMove(row, col, row + 1, col + 1);
    						regularMoves.add(m);
    						regIndex++;
    					}else if(row < board.length-2 && col < board[0].length-2 && board[row + 2][col + 2]==EMPTY && board[row + 1][col + 1]!=BLACK && board[row + 1][col + 1]!=BLACK_KING && board[row + 1][col + 1]!=EMPTY) {
    						m = new CheckersMove(row, col, row + 2, col + 2);
    						jumpMoves.add(m);
    						jumpIndex++;
    					}
    					
    					if(row < board.length-1 && col > 0 && board[row + 1][col - 1]==EMPTY) {
    						m = new CheckersMove(row, col, row + 1, col - 1);
    						regularMoves.add(m);
    						regIndex++;
    					}else if(row < board.length-2 && col > 1 && board[row + 2][col - 2]==EMPTY && board[row + 1][col - 1]!=BLACK && board[row + 1][col - 1]!=BLACK_KING && board[row + 1][col - 1]!=EMPTY) {
    						m = new CheckersMove(row, col, row + 2, col - 2);
    						jumpMoves.add(m);
    						jumpIndex++;
    					}
    					
    				}
    				if(board[row][col]== BLACK_KING) { // the two ways only a black king can go in the game i.e. up
    					CheckersMove m;
    					if(row > 0 && col > 0 && board[row - 1][col - 1]==EMPTY) {
    						m = new CheckersMove(row, col, row - 1, col - 1);
    						regularMoves.add(m);
    						regIndex++;
    					}else if(row > 1 && col > 1 && board[row - 2][col - 2]==EMPTY && board[row - 1][col - 1]!=BLACK && board[row - 1][col - 1]!=BLACK_KING && board[row - 1][col - 1]!=EMPTY) {
    						m = new CheckersMove(row, col, row - 2, col - 2);
    						jumpMoves.add(m);
    						jumpIndex++;
    					}
    					
    					if(row > 0 && col < board[0].length-1 && board[row - 1][col + 1]==EMPTY) {
    						m = new CheckersMove(row, col, row - 1, col + 1);
    						regularMoves.add(m);
    						regIndex++;
    					}else if(row > 1 && col < board[0].length-2 && board[row - 2][col + 2]==EMPTY && board[row - 1][col + 1]!=BLACK && board[row - 1][col + 1]!=BLACK_KING && board[row - 1][col + 1]!=EMPTY) {
    						m = new CheckersMove(row, col, row - 2, col + 2);
    						jumpMoves.add(m);
    						jumpIndex++;
    					}
    					
    				}
    				
    			}
    		}
    	}else if(player == RED) { // similar set up as black, but for red coins
    		for(int row = board.length-1; row >= 0; row --) {
    			for(int col = board[0].length -1 ; col >= 0 ; col--) {
    				if(board[row][col]== RED || board[row][col]== RED_KING) {
    					CheckersMove m;
    					if(row > 0 && col > 0 && board[row - 1][col - 1]==EMPTY) {
    						m = new CheckersMove(row, col, row - 1, col - 1);
    						regularMoves.add(m);
    						regIndex++;
    					}else if(row > 1 && col > 1 && board[row - 2][col - 2]==EMPTY && board[row - 1][col - 1]!=RED && board[row - 1][col - 1]!=RED_KING && board[row - 1][col - 1]!=EMPTY) {
    						m = new CheckersMove(row, col, row - 2, col - 2);
    						jumpMoves.add(m);
    						jumpIndex++;
    					}
    					
    					if(row > 0 && col < board[0].length-1 && board[row - 1][col + 1]==EMPTY) {
    						m = new CheckersMove(row, col, row - 1, col + 1);
    						regularMoves.add(m);
    						regIndex++;
    					}else if(row > 1 && col < board[0].length-2 && board[row - 2][col + 2]==EMPTY && board[row - 1][col + 1]!=RED && board[row - 1][col + 1]!=RED_KING && board[row - 1][col + 1]!=EMPTY) {
    						m = new CheckersMove(row, col, row - 2, col + 2);
    						jumpMoves.add(m);
    						jumpIndex++;
    					}
    					
    				}
    				if(board[row][col]== RED_KING) {
    					CheckersMove m;
    					if(row < board.length-1  && col < board[0].length-1 && board[row + 1][col + 1]==EMPTY) {
    						m = new CheckersMove(row, col, row + 1, col + 1);
    						regularMoves.add(m);
    						regIndex++;
    					}else if(row < board.length-2 && col < board[0].length-2 && board[row + 2][col + 2]==EMPTY && board[row + 1][col + 1]!=RED && board[row + 1][col + 1]!=RED_KING && board[row + 1][col + 1]!=EMPTY) {
    						m = new CheckersMove(row, col, row + 2, col + 2);
    						jumpMoves.add(m);
    						jumpIndex++;
    					}
    					
    					if(row < board.length-1 && col > 0 && board[row + 1][col - 1]==EMPTY) {
    						m = new CheckersMove(row, col, row + 1, col - 1);
    						regularMoves.add(m);
    						regIndex++;
    					}else if(row < board.length-2 && col > 1 && board[row + 2][col - 2]==EMPTY && board[row + 1][col - 1]!=RED && board[row + 1][col - 1]!=RED_KING && board[row + 1][col - 1]!=EMPTY) {
    						m = new CheckersMove(row, col, row + 2, col - 2);
    						jumpMoves.add(m);
    						jumpIndex++;
    					}
    					
    				}
    			}
    		}
    	}
    	
    	if(jumpIndex > 0) { // check if jump moves exist
    		CheckersMove[] temp = new CheckersMove[jumpMoves.size()];
    		temp = jumpMoves.toArray(temp);
    		return temp;
    	}else if(regIndex > 0) {// check if regular moves exist
    		CheckersMove[] temp = new CheckersMove[regularMoves.size()];
    		temp = regularMoves.toArray(temp);
    		return temp;
    	}else {
    		return null;
    	}
    }


    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
    	ArrayList<CheckersMove> jumpMoves = new ArrayList<CheckersMove>();
    	int jumpIndex = 0;
    	if(player == BLACK) {
    		if(board[row][col]== BLACK || board[row][col]== BLACK_KING) { // jump moves for any black coin in the game
				CheckersMove m;
				if(row < board.length-2 && col < board[0].length-2 && board[row + 2][col + 2]==EMPTY && board[row + 1][col + 1]!=BLACK && board[row + 1][col + 1]!=BLACK_KING && board[row + 1][col + 1]!=EMPTY) {
					m = new CheckersMove(row, col, row + 2, col + 2);
					jumpMoves.add(m);
					jumpIndex++;
				}
				
				if(row < board.length-2 && col > 1 && board[row + 2][col - 2]==EMPTY && board[row + 1][col - 1]!=BLACK && board[row + 1][col - 1]!=BLACK_KING && board[row + 1][col - 1]!=EMPTY) {
					m = new CheckersMove(row, col, row + 2, col - 2);
					jumpMoves.add(m);
					jumpIndex++;
				}
				
			}
			if(board[row][col]== BLACK_KING) { // additional jump moves only the black king has in the game
				CheckersMove m;
				if(row > 1 && col > 1 && board[row - 2][col - 2]==EMPTY && board[row - 1][col - 1]!=BLACK && board[row - 1][col - 1]!=BLACK_KING && board[row - 1][col - 1]!=EMPTY) {
					m = new CheckersMove(row, col, row - 2, col - 2);
					jumpMoves.add(m);
					jumpIndex++;
				}
				
				if(row > 1 && col < board[0].length-2 && board[row - 2][col + 2]==EMPTY && board[row - 1][col + 1]!=BLACK && board[row - 1][col + 1]!=BLACK_KING && board[row - 1][col + 1]!=EMPTY) {
					m = new CheckersMove(row, col, row - 2, col + 2);
					jumpMoves.add(m);
					jumpIndex++;
				}
				
			}
    	}else if(player == RED) { // similar to black moves lookup but for red
    		if(board[row][col]== RED || board[row][col] == RED_KING) {
				CheckersMove m;
				if(row > 1 && col > 1 && board[row - 2][col - 2]==EMPTY && board[row - 1][col - 1]!=RED && board[row - 1][col - 1]!=RED_KING && board[row - 1][col - 1]!=EMPTY) {
					m = new CheckersMove(row, col, row - 2, col - 2);
					jumpMoves.add(m);
					jumpIndex++;
				}
				
				if(row > 1 && col < board[0].length-2 && board[row - 2][col + 2]==EMPTY && board[row - 1][col + 1]!=RED && board[row - 1][col + 1]!=RED_KING  && board[row - 1][col + 1]!=EMPTY) {
					m = new CheckersMove(row, col, row - 2, col + 2);
					jumpMoves.add(m);
					jumpIndex++;
				}
				
			}
			if(board[row][col]== RED_KING) {
				CheckersMove m;
				if(row < board.length-2 && col < board[0].length-2 && board[row + 2][col + 2]==EMPTY && board[row + 1][col + 1]!=RED && board[row + 1][col + 1]!=RED_KING && board[row + 1][col + 1]!=EMPTY) {
					m = new CheckersMove(row, col, row + 2, col + 2);
					jumpMoves.add(m);
					jumpIndex++;
				}
				
				if(row < board.length-2 && col > 1 && board[row + 2][col - 2]==EMPTY && board[row + 1][col - 1]!=RED && board[row + 1][col - 1]!=RED_KING && board[row + 1][col - 1]!=EMPTY) {
					m = new CheckersMove(row, col, row + 2, col - 2);
					jumpMoves.add(m);
					jumpIndex++;
				}
				
			}
		}
    	
    	if(jumpIndex > 0) {// check if jump moves exist
    		CheckersMove[] temp = new CheckersMove[jumpMoves.size()];
    		temp = jumpMoves.toArray(temp);
    		return temp;
    	}else {
    		return null;
    	}
    }

}