package raining.ui.components;

import java.awt.Rectangle;

import javafx.scene.shape.Ellipse;

/**
 * This class is the cup object which the users play's the game with.
 * The cup is created with 3 ellipses parts.
 * @author asen
 *
 */
public class Cup {

	private static Ellipse[] parts;

	private static final int RADIUS_X = 10;
	private static final int RADIUS_Y = 50;
	private int[] partsX;

	private int[] partsY;

	public long highScore = 0;
	public Cup() {

		int h;
		initParts();

	}
/**
 * init all the cordinaties of the 2 arrays - and create the 3 parts of ellipses
 */
	private void initParts() {

		partsX = new int[3];
		partsY = new int[3];
		parts = new Ellipse[3];

		partsX[0] = 100;
		partsX[1] = 150;
		partsX[2] = 200;

		partsY[0] = 500;
		partsY[1] = 550;
		partsY[2] = 500;

		for (int i = 0; i < parts.length; i++) {
			parts[i] = new Ellipse(partsX[i], partsY[i], RADIUS_X, RADIUS_Y);

		}
		parts[1].setRadiusX(RADIUS_Y);
		parts[1].setRadiusY(RADIUS_X);

		
	}
/**
 * 
 * @return the parts of the cup
 */
	public Ellipse[] getParts() {

		return this.parts;
	}

	/**
	 * move left behaviour- move all the parts at the same time
	 * 	  @author asen
	 */
	public void moveLeft() {
		for (int i = 0; i < parts.length; i++) {
			if (partsX[i] <= -5) {
				return;
			}
		}
		for (int i = 0; i < parts.length; i++) {
			partsX[i] -= 5;
			parts[i].setCenterX(partsX[i]);
		}

	}

	
	/**
	 * move right behaviour- move all the parts at the same time
	 * 	  @author asen
	 */
	public void moveRight() {

		for (int i = 0; i < partsX.length; i++) {
			if (partsX[i] >= 600) {
				return;
			}
		}

		for (int i = 0; i < partsX.length; i++) {
			partsX[i] += 5;
			parts[i].setCenterX(partsX[i]);
		}

	}
/**
 * This method is called many many times for each rain drop,to check if is in the cup.
 * @param line
 */
	public void checkIfSingleRainIsInTheCup(RainDrop line) {

		if (line.getEndX() >= partsX[0] && line.getEndX() <= partsX[2] && line.getEndY() > 525) {

			line.clear();
			highScore++;
		}

	}
/**
 * get the HighScore at the end of the game
 * @return
 * 	  @author asen
 */
	public final long getHighScore() {
		return highScore;
	}

	public final void setHighScore(long highScore) {
		this.highScore = highScore;
	}
	
	
}
