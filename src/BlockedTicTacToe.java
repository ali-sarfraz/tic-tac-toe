public class BlockedTicTacToe 
{
	private char[][] gameBoard;   													
	private int size;
	private int line;
	
	// Creates the game.
	public BlockedTicTacToe(int board_size, int inline, int max_levels)
	{
		size = board_size;
		line = inline;
		gameBoard = new char[size][size];  
		
		// Initialize the game board.
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				gameBoard[i][j] = ' ';   											
			}
		}
	}
	
	// Converts the game board to a string representation.
	private String makeString(char[][] board, int size)   						   
	{
		String config = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				config = config + board[i][j];
			}
		}
		
		return config;
	}
	
	// Creates new dictionary of a prime number size
	public TTTDictionary createDictionary() {
		TTTDictionary dictionary = new TTTDictionary(4817);
		return dictionary;
	}

	public int repeatedConfig(TTTDictionary configurations)
	{
		String config = makeString(gameBoard,size);
		TTTRecord record = configurations.get(config);
		
		if (record != null) {
			return record.getScore();
		}

		return -1;
	}
	
	public void insertConfig(TTTDictionary configurations, int score, int level)
	{
		String config = makeString(gameBoard,size);
		TTTRecord record = new TTTRecord(config,score,level);
		
		try	{
			configurations.put(record);
			
		} catch (DuplicatedKeyException e) {
			/* JAVA IMPLEMENTATION FOR DEALING WITH EXCEPTIONS */
		}
	}
	
	public void storePlay(int row, int col, char symbol) {
		gameBoard[row][col] = symbol;
	}
	
	public boolean squareIsEmpty (int row, int col)
	{
		if (gameBoard[row][col] == ' ') {
			return true;
		}
		
		return false;
	}
	
	public boolean wins (char symbol)
	{
		int counter = 0;
		int marker = line - 1; 
		
		/* The two for loops below are used to test all 4 possible winning techniques
		 * 1: Three simultaneous symbols on the same horizontal,
		 * 2: Three simultaneous symbols on the same vertical,
		 * 3: Three simultaneous symbols on the same diagonal going in the \ direction
		 * 4: Three simultaneous symbols on the same diagonal going in the / direction
		 */
		
		for (int row = 0; row < size; row++) {
			for(int col = 0; col < size; col++) {
				
				// 1: Check horizontally.
				if (col + marker < size) {
					counter = 0;
						
					for (int k = 0; k < line; k++) {
						if (gameBoard[row][col+k] == symbol) {
							counter++;
						}
					}
						if (counter == line) {
							return true;
						}
				}
				
				// 2: Check vertically.
				if (row + marker < size) {
					counter = 0;
					
					for (int k = 0; k < line; k++) {
						if (gameBoard[row+k][col] == symbol) {
							counter++;
						}
					}
					
					if (counter == line) {
						return true;
					}
					
				}
				
				// 3: Check diagonal down right.
				if ((col + marker) < size && (row + marker) < size) {
					counter = 0;
					
					for (int k = 0; k < line; k++) {
						if (gameBoard[row+k][col+k] == symbol) {
							counter++;
						}
					}
					
					if (counter == line) {
						return true;    								    
					}
				}
				
				// 4: Check diagonal down left.
				if ((col + marker) < size && (row - marker) >= 0) {
					counter = 0;
					
					for (int k = 0; k < line; k++) {
						if (gameBoard[row-k][col+k] == symbol) {
							counter++;
						}
					}
					
					if (counter == line) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isDraw() {
		int count = 0;
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (gameBoard[i][j] != ' ') {
					count++;
				}
			}
		}
		
		if (count == size * size) {
			return true;
		}
		
		return false;
	}
	
	public int evalBoard() {
		if ( wins('x') == true ) {
			return 0;      						
		} else if ( wins('o') == true) {
			return 3; 						  	
		} else if (isDraw() == true) {
			return 1;							
		} else return 2;	
	}
}
