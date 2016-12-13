package bot;

import java.util.ArrayList;
import java.util.List;

import game.TetrisGame;
import game.Tile;
import game.TileState;

/**
 * This is an abstract class meant to be extended.
 * 
 * It contains methods which determines certain information about the current board state,
 * which will be used by child classes in order to determine the best move.
 * 
 * @author Andy
 *
 */
public abstract class Algorithm {
	protected TetrisGame game;
	
	public Algorithm() {
		game = TetrisGame.getInstance();
	}
	
	/**
	 * Method which must be implemented by child classes.
	 * 
	 * It is the method which determines the best move for the
	 * current piece. 
	 */
	public abstract String execute();
	
	/**
	 * This method finds the first available gap of the specified width and height in
	 * the Tetris Game.
	 * 
	 * @param gapWidth Width of the desired gap
	 * @param gapHeight Height of the desired gap
	 * @return the Tile object in the top-left corner of the found gap. Returns null if no gap has been found.
	 */
	protected List<Tile> findGap(int gapWidth, int gapHeight) {
		
		List<Tile> gaps = new ArrayList<Tile>();
		
		Tile currentTile = null;
		
		// Iterate through all game tiles
		for (int i = 0; i < game.height(); i++) {
			for (int j = 0; j < game.width(); j++) {
				currentTile = game.get(i, j);
				
				// Check if currentTile is empty
				if (currentTile.getState().equals(TileState.EMPTY)) {
					
					// Check if currentTile is left most tile OR the tile to its left is filled
					// If it is, then it is next to the wall, and is the 'start' of a gap.
					if ((j - 1) == -1 || game.get(i, j - 1).getState().equals(TileState.FILLED)) {
						
						// Initial values. 
						int gap = 0;
						int tempJ = j;
						Tile tempTile = currentTile;
						
						// Loop until it reaches the end of the row
						while (tempJ < game.width()) {
							tempTile = game.get(i, tempJ);
							
							// Check if tempTile is empty. Increment gap and tempJ values if it is.
							if (tempTile.getState().equals(TileState.EMPTY)) {
								gap++;
								tempJ++;
							}
							
							// Otherwise it is filled, so break loop as now we have the gap size.
							else {
								break;
							}
						}
						
						// Check if the correct gap width has been found
						// If it has, the following block of code will check if each horizontal gap column
						// has the correct gap height
						if (gap == gapWidth) {
							
							// Temporarily set to true. The loop sets it to false as soon as one of the columns 
							// does not have the correct height gap.
							boolean gapFound = true;
							
							// Iterate through the horizontal gaps
							for (tempJ = j; tempJ < (j + gap); tempJ++) {
								
								int verticalGap = 0;
								int tempI = i;
								
								// Loop through the rows of the column to check height gap
								while (tempI < game.height()) {
									tempTile = game.get(tempI, tempJ);
									
									// Check if empty
									if (tempTile.getState().equals(TileState.EMPTY)) {
										verticalGap++;
										tempI++;
									}
									
									else {
										break;
									}
								}
								
								// Sets gapFound to false if vertical gap size does not match the desired gap height.
								if (verticalGap != gapHeight) {
									gapFound = false;
									break;
								}
							}
							
							if (gapFound) {
								gaps.add(currentTile);
								break;
							}
						}			
					}
				}
			}
		}
		
		return gaps;
	}
	
	protected String determineHorizontalMovement(int startX, int endX) {
		
		return "";
	}
	
	


}
