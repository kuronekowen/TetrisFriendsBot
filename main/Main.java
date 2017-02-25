package main;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;

import bot.MoveGenerator;
import bot.SimulatedGame;
import bot_old.Inputer;
import game.Tetrimino;
import game.RealGame;

public class Main {
	
	public static void main(String[] args) throws IOException, AWTException, InterruptedException {
		
		RealGame game = RealGame.getInstance();
		Inputer inputer = new Inputer();
		Robot robot = new Robot();
			
		while(true) {
			if (game.gameDetected()) {
				Tetrimino currentTetrimino = game.getCurrentTetrimino();
				Tetrimino nextTetrimino = game.getNextTetrimino();
							
				if (currentTetrimino != null) {
					
					// Get height of the tower before executing a move
					int heightBeforeMove = game.currentHeight();
			
					// Resetting variables
					SimulatedGame bestGame = null;
					int highestScore = 0;
					int bestGameHeightAfterFirstMove = 0;
					String bestMove = "";
					boolean isFirstMove = true;
					
//					// Iterate through all possible moves for the tetrimino
//					for (String move : new MoveGenerator().generateMoves(currentTetrimino)) {
//						
//						// Create a simulated game and execute that move
//						SimulatedGame sg = new SimulatedGame();
//						sg.add(currentTetrimino);
//						sg.simulateMove(move);
//						
//						int score = sg.calculateScore(); // Calculate the board state after simulating that move
//						
//						// Set as best move if it beats the current high score
//						if (isFirstMove || score > highestScore) {
//							bestMove = move;
//							highestScore = score;
//							bestGame = sg;
//							isFirstMove = false;
//						}
//					}
					
					for (String firstMove: new MoveGenerator().generateMoves(currentTetrimino)) {
						for (String secondMove : new MoveGenerator().generateMoves(nextTetrimino)) {
							
							SimulatedGame sg = new SimulatedGame();
							sg.add(currentTetrimino);
							sg.add(nextTetrimino);
							sg.simulateMove(firstMove);
							
							int heightAfterFirstMove = sg.currentHeight();
							
							sg.simulateMove(secondMove);
							
							int score = sg.calculateScore();
							
							// Set as best move if it beats the current high score
							if (isFirstMove || score > highestScore) {
								bestMove = firstMove;
								highestScore = score;
								bestGameHeightAfterFirstMove = heightAfterFirstMove;
								bestGame = sg;
								isFirstMove = false;
							}
						}
					}
					
					// Execute best move
					inputer.executeMoves(bestMove + "s");
					
					// Delays if a line was cleared (due to annoying clear animations)
					if (bestGame != null && bestGameHeightAfterFirstMove < heightBeforeMove) {
						robot.delay(500);
					}
									
				}							
			}			
			game.update();			
		}			
	}

}
