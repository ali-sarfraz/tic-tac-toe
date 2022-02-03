import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PlayTTT extends JFrame {
	static final long serialVersionUID = 0;
	private final char COMPUTER = 'o';
	private final char HUMAN = 'x';
	private final char BLOCKED = 'b';

	private JButton[][] gameDisplay;
	private BlockedTicTacToe t;
	private int max_level;
	private TTTDictionary configurations;
	private int numBlockedPositions;
	private int[] blockedPositions;
	private ClickHandler handler;

	/*
	 * Constructor. Creates a panel to represent the game board and destroys the
	 * panel when its window is closed.
	 */
	public PlayTTT(int size, int to_win, int depth, int num, int[] b) {
		Container c = getContentPane();
		c.setLayout(new GridLayout(size, size));
		gameDisplay = new JButton[size][size];
		Icon emptySquare = new ImageIcon("images/empty.gif");
		handler = new ClickHandler(size);

		/* Board is represented as a grid of clickable buttons */
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				gameDisplay[i][j] = new JButton("", emptySquare);
				gameDisplay[i][j].setEnabled(true);
				add(gameDisplay[i][j]);
				gameDisplay[i][j].addActionListener(handler);
			}

		max_level = depth;
		numBlockedPositions = num;
		blockedPositions = b;
		t = new BlockedTicTacToe(size, to_win, depth);

		for (int i = 0; i < numBlockedPositions; ++i) {
			int row = blockedPositions[i] / size;
			int col = blockedPositions[i] % size;
			gameDisplay[row][col].setIcon(new ImageIcon("images/blocked1.gif"));
			gameDisplay[row][col].paint(gameDisplay[row][col].getGraphics());

			t.storePlay(row, col, BLOCKED);
		}
	}

	/*
	 * To run the program type: java PlayTTT size to_win, where size is the size
	 * of the board and to_win is the number of symbols in line needed to win
	 * the game.
	 */
	public static void main(String[] args) {
		int size = 0, adjacent_to_win = 0, depth = 0;
		int numBlocked;
		int[] blocked;

		/* Check that the number of arguments is the correct one */
		if (args.length < 3) {
			System.out.println("Usage: java PlayTTT board-size symbols-inline-to-win depth blocked-positions");
			System.exit(0);
		}

		try {
			size = Integer.parseInt(args[0]);

			/*
			 * Number of positions marked by the same player in the same row,
			 * column, or diagonal, required to win
			 */
			adjacent_to_win = Integer.parseInt(args[1]);
			depth = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.out.println("Invalid program argument");
			System.exit(0);
		}

		/* Read the set of blocked positions and stored in array "blocked" */
		numBlocked = args.length - 3;
		blocked = new int[numBlocked];
		for (int i = 0; i < numBlocked; ++i) {
			blocked[i] = Integer.parseInt(args[3 + i]) - 1;
			if ((blocked[i] < 0) || (blocked[i] >= size * size)) {
				System.out.println("Invalid board position " + blocked[i]);
				System.exit(0);
			}
		}

		/* Create the game board and start the game */
		JFrame f = new PlayTTT(size, adjacent_to_win, depth, numBlocked, blocked);
		f.setSize(size * 100, size * 100);
		f.setVisible(true);
		
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		
		if (Math.random() > 0.5) 
			((PlayTTT)f).handler.displayComputerPlay();
	}

	/*
	 * Panel to represent the game board. It contians methods for detecting the
	 * position selected by the human player.
	 */
	private class ClickHandler implements ActionListener {
		private int board_size;
		private boolean game_ended = false;

		/* Constructor. Save board size in instance variable */
		public ClickHandler(int size) {
			board_size = size;
		}

		/*
		 * When the user has selected a play, this method is invoked to process
		 * the selected play
		 */
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() instanceof JButton) {
				int row = -1, col = -1;

				if (game_ended)
					System.exit(0);

				/* Find out which position was selected by the player */
				for (int i = 0; i < board_size; i++) {
					for (int j = 0; j < board_size; j++)
						if (event.getSource() == gameDisplay[i][j]) {
							row = i;
							col = j;
							break;
						}
					if (row != -1)
						break;
				}

				if (t.squareIsEmpty(row, col)) {
					/* Valid play, mark it on the board */
					gameDisplay[row][col].setIcon(new ImageIcon("images/human.gif"));
					gameDisplay[row][col].paint(gameDisplay[row][col].getGraphics());

					t.storePlay(row, col, HUMAN);
					if (t.wins(HUMAN))
						endGame("Human wins");
					else {
						if (t.isDraw())
							endGame("Game is a draw");
						else 
							displayComputerPlay();
					}
				} else
					System.out.println("Invalid play");
			}
		}
		
		private void displayComputerPlay() {
			PosPlay pos;
			
			pos = computerPlay(COMPUTER, -1, 4, 0);
			t.storePlay(pos.getRow(), pos.getCol(), COMPUTER);
			gameDisplay[pos.getRow()][pos.getCol()].setIcon(new ImageIcon("images/computer.gif"));
			if (t.wins(COMPUTER))
				endGame("Computer wins");
			else if (t.isDraw())
				endGame("Game is a draw");
		}

		/* Explore the game tree and choose the best move for the computer */
		private PosPlay computerPlay(char symbol, int highest_score, int lowest_score, int level) {
			char opponent;
			PosPlay reply;

			int bestRow = -1;
			int bestColumn = -1;

			int value;
			int lookupVal;

			if (level == 0)
				configurations = t.createDictionary();

			if (symbol == COMPUTER) {
				opponent = HUMAN;
				value = -1;
			} else {
				opponent = COMPUTER;
				value = 4;
			}
			
			// Scan entries of the game board in random order.
			int row, column;
			row = (int)(Math.random() * board_size);

			for (int r = 0; r < board_size; r++) {
				column = (int)(Math.random() * board_size);
				for (int c = 0; c < board_size; c++) {
					if (t.squareIsEmpty(row, column)) {
						t.storePlay(row, column, symbol);
						if (t.wins(symbol) || t.isDraw() || (level >= max_level))
							reply = new PosPlay(t.evalBoard(), row, column);
						else {
							lookupVal = t.repeatedConfig(configurations);
							if (lookupVal != -1)
								reply = new PosPlay(lookupVal, row, column);
							else {
								reply = computerPlay(opponent, highest_score, lowest_score, level + 1);
								t.insertConfig(configurations, reply.getScore(), 0);
							}
						}
						t.storePlay(row, column, ' ');

						if ((symbol == COMPUTER && reply.getScore() > value)
								|| (symbol == HUMAN && reply.getScore() < value)) {
							bestRow = row;
							bestColumn = column;
							value = reply.getScore();

							if (symbol == COMPUTER && value > highest_score)
								highest_score = value;
							else if (symbol == HUMAN && value < lowest_score)
								lowest_score = value;

							if (highest_score >= lowest_score)
								return new PosPlay(value, bestRow, bestColumn);
						}

					}
					column = (column + 1) % board_size;
				}
				row = (row + 1) % board_size;
			}
			return new PosPlay(value, bestRow, bestColumn);
		}

		/* Prompt the user for a key to terminate the game */
		private void endGame(String mssg) {
			System.out.println(mssg);
			System.out.println("");
			System.out.println("Click on board to terminate game");
			game_ended = true;
		}
	}
}
