package edu.iastate.cs472.proj1;
/**
 * 
 * @author Amith Kopparapu Venkata Boja
 *
 */
public class AlphaBetaSearch {
	static final int
	    EMPTY = 0,
	    RED = 1,
	    RED_KING = 2,
	    BLACK = 3,
	    BLACK_KING = 4;
	
    private CheckersData board;

    // An instance of this class will be created in the Checkers.Board
    // It would be better to keep the default constructor.

    public void setCheckersData(CheckersData board) {
        this.board = board;
    }

    // Todo: You can implement your helper methods here
    /**
     * 
     * A function to determine the min-max value for a given state. 
     * @param b
     * 		the current state of the board on the checkers board
     * @return
     * 		the utility value of the current state
     */
    public int utility(CheckersData b, CheckersMove black, CheckersMove red) {
    	int value = 0;
    	for(int row =0; row < b.board.length; row++) {
    		for(int col = 0; col < b.board[0].length; col++) {
    			if(b.board[row][col]== RED_KING) {
    				if(red != null && red.isJump()) {
        				value-=1;
        			}
    				value -=2;
    			}else if(b.board[row][col]== BLACK_KING) {
    				if(black != null && black.isJump()) {
        				value+=1;
        			}
    				value +=2;
    			}else if(b.board[row][col]== RED) {
    				if(red != null && red.isJump()) {
        				value-=1;
        			}
    				value--;
    			}else if(b.board[row][col]== BLACK) {
    				if(black != null && black.isJump()) {
        				value+=1;
        			}
    				value++;
    			}
    			
    		}
    	}
    	
    	return value;
    }
    
    /**
     * the value that determines the beta value for a state in the alpha-beta algorithm
     * @param d
     * 		the state to search from in the game tree
     * @param alpha
     * 		the current alpha value of the state
     * @param beta
     * 		the current beta value of the state
     * @param depth
     * 		the depth in the game tree
     * @return
     */
    public int minValue(CheckersData d, int alpha, int beta, int depth) {
    	CheckersMove[] moves = d.getLegalMoves(1);
        if ( moves == null || d.getLegalMoves(3) == null )
            return utility(d, null, null);
    	if(depth <6) {
    		depth++;
	        int value = Integer.MAX_VALUE;
	        for (CheckersMove action : moves) {
	        	CheckersData data = new CheckersData(d.board);
		        data.makeMove(action);
		        value = Math.min(value, maxValue(data, alpha, beta, depth));
		        depth++;
	            if (value <= alpha)
	                return value;
	            beta = Math.min(beta, value); 
	        }
	        return value;
        }
        return utility(d, d.getLegalMoves(3)[0], moves[0]);
    }
    
    /**
     * the value that determines the alpha value for a state in the alpha-beta algorithm
     * @param d
     * 		the state to search from in the game tree
     * @param alpha
     * 		the current alpha value of the state
     * @param beta
     * 		the current beta value of the state
     * @param depth
     * 		the depth in the game tree
     * @return
     */
    public int maxValue(CheckersData d, int alpha, int beta,int depth) {
    	CheckersMove[] moves = d.getLegalMoves(3);
    	if (d.getLegalMoves(1) == null || moves == null )
            return utility(d,null,null);
    	if(depth <6) {
    		depth++;
	        int value = Integer.MIN_VALUE;
	        for (CheckersMove action : moves) {
	        	CheckersData data = new CheckersData(d.board);
	        	data.makeMove(action);
	        	value = Math.max(value, minValue(data, alpha, beta, depth));
	            if (value >= beta)
	                return value;
	            alpha = Math.max(alpha, value);
	        }
	        return value;
    	}
    	return utility(d,moves[0],d.getLegalMoves(1)[0]);
    }
    /**
     *  You need to implement the Alpha-Beta pruning algorithm here to
     * find the best move at current stage.
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        // The checker board state can be obtained from this.board,
        // which is a int 2D array. The numbers in the `board` are
        // defined as
        // 0 - empty square,
        // 1 - red man
        // 2 - red king
        // 3 - black man
        // 4 - black king
//        System.out.println(board.toString());
//        System.out.println();
        if(legalMoves == null || legalMoves.length== 0) {
        	return null;
        }
        CheckersMove result = null;
        int resultValue = Integer.MIN_VALUE;
        int depth = 0;
        //System.out.println("Start:");
        for (CheckersMove action : legalMoves) {
        	CheckersData data = new CheckersData(board.board);
        	data.makeMove(action);
            int value = minValue(
                    data, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
           
        }
        return result;
        // Here, we simply return the first legal move for demonstration.
        //return legalMoves[0];
    }
}
