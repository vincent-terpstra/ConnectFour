package connectfour;


/**
 * @File	Board.java
 * @Author	Vincent Terpstra
 * @Date   	Sept 20, 2019
 * @Class ConnectFour.java 
 * 		Board for a connect four game,
 * 		Implements AI 
 * 			BacktrackAI
 * 			RandomAI
 */
public class Board {
	
	static final int EMPTY = 2, PLAYER = 0, AI = 1;
	private int[][] pieces = new int[7][6];
	private int[] lowest = new int[7];
	
	private int piecesPlayed;
	
	/**
	 * @return random column that can add a piece
	 */
	int randomColumn(){
		if(endGame()) return -1;
		int col;
		while(!isOpen(col = (int)(Math.random() * 7))); //; intentional
		
		System.out.println("Adding random"  + col);
		return col;
	}
	
	boolean endGame(){
		return piecesPlayed == 42;
	}
	
	boolean isOpen(int col){
		return pieces[col][5] == EMPTY;
	}
	
	/**
	 * BackTrackAI with depth of 0
	 * returns a playable column
	 * 
	 * wraps around the recursive algorithm BackTrackAI
	 * remembers some common cases for the start of the game
	 * if BackTrackAI has no good moves, will play to not lose
	 */
	
	int calls;
	int BackTrackAI(int type){
		calls = 0;
		int PLAYER = type + 1 % 2;
		int AI = type;
		//remember these starting moves to save on compute power
		if(piecesPlayed <= 1){
			return 3;
		} else if(piecesPlayed == 3 && check(3, 0, PLAYER)){
			if(check(4, 0, PLAYER)){
				return 2;
			} else if(check(2, 0, PLAYER)){
				return 4;
			}	
		}
		
		
		int col = BackTrackAI(AI, 0);
		
		// DEBUG INFO
		if(col != -1)
		if(checkWin(col, lowest[col], AI)){
			System.out.print("Winning "+ col);
		} else {
			System.out.print("Here works " + col);
		}
		System.out.println(" Calls " + calls);
		
		//No good moves; play to not lose
		if(col == -1){
			for( col = 0; col < 7; col++){
				if(isOpen(col)){
					int row = lowest[col]++;
					//pieces[col][row] = AI; don't need to set the piece for findWin(PLAYER)
					
					int plr = findWin(PLAYER);
					
					lowest[col]--;
					//pieces[col][row] = EMPTY;
					
					if(plr == -1) //not a losing move
						return col;
				}
			}
			return randomColumn(); //Give up
			
		} else {
			return col;
		}
	}
	
	private boolean check(int col, int row, int type){
		return pieces[col][row] == type;
	}
	
	/**
	 * A AI to calculate a best possible move
	 * Uses a backtracking Algorithm to map the board
	 * 
	 * NOTE: this algorithm may ignore basic situations where it will force a win eg. _AA__
	 * NOTE: I have difficulties beating this Algorithm PvAI
	 * NOTE: This AI has no HEURISTIC except for winning
	 * 
	 * @param player current player
	 * @param depth how many recursions
	 * @return
	 */
	final int MAXDEPTH = 15; //needs to be odd (end on opponent's turn)
	static final int[] BIAS = {3, 4, 2, 1, 5, 0, 6};
	
	private int BackTrackAI(int player, final int depth){
		if(piecesPlayed + depth == 42 || depth == MAXDEPTH) //end case
			return -1;
		
		calls++;
		
		//find places where a piece will win
		int win = -1;
		if(piecesPlayed + depth >= 6)
			win = findWin(player);
		
		if(win != -1)
			return win;
		
		//Second find moves where the opponent will lose
		for(int idx = 0; idx < 7; idx++){
			int col = BIAS[idx]; //centre the first check
			if(isOpen(col)){
				//play the piece
				int row = lowest[col]++;
				pieces[col][row] = player;
				
				int blockVal = BackTrackAI((player + 1) % 2, depth + 1);
				
				//remove piece
				lowest[col] --;
				pieces[col][row] = EMPTY;
				
				if(blockVal == -1) //next player has no good moves
					return col;
			}
		}
		return -1; //game can find no winning moves for this piece
	}
	
	int findWin(int player){
		for(int col = 0; col < 7; col++){
			if(isOpen(col) && checkWin(col, lowest[col], player))
				return col;
		}
		return -1;
	}
		
	/**
	 * Function to add a piece to the board
	 * @return the row added
	 */
	int addPiece(int column, int type){
		if(piecesPlayed == 42 || !isOpen(column)) return -1;
		
		int row = lowest[column]++;
		pieces[column][row] = type;
		++piecesPlayed;
		
		return row;
	}
	
	
	/**
	 * Function to check if the last piece played won/ended the game
	 */
	boolean checkWin(int col, int row, int type){
		   return 
				sum(col, row, type,  1, 0) + sum(col, row, type, -1,  0) > 2 || //horizontal 
				sum(col, row, type,  1, 1) + sum(col, row, type, -1, -1) > 2 || //diagonal up
				sum(col, row, type, -1, 1) + sum(col, row, type,  1, -1) > 2 || //diagonal down
				sum(col, row, type, 0,-1) > 2; //vertical
	}
	
	/**
	 * @return Total pieces in a line that are the same as the last piece
	 */
	private final int sum(int column, int row, int type, int dColumn, int dRow){
		int sum = 0;
		while(!(   (column += dColumn) < 0 || column > 6 
				|| (row += dRow) < 0 || row > 5) 
				&& pieces[column][row] == type) sum++;
		return sum;
	}
	
	public void reset(){
		piecesPlayed = 0;
		for(int[] col : pieces){
			for(int i = 0; i < col.length; i++){
				col[i] = EMPTY;
			}
		}
		
		for(int i = 0; i < lowest.length; i++){
			lowest[i] = 0;
		}
	}
}
