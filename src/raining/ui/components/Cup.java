package raining.ui.components;

import java.awt.Rectangle;

import javafx.scene.shape.Ellipse;

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

	public Ellipse[] getParts() {

		return this.parts;
	}

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

	public void checkIfSingleRainIsInTheCup(RainDrop line) {

		if (line.getEndX() >= partsX[0] && line.getEndX() <= partsX[2] && line.getEndY() > 525) {

			line.clear();
			highScore++;
		}

	}

	public final long getHighScore() {
		return highScore;
	}

	public final void setHighScore(long highScore) {
		this.highScore = highScore;
	}
	
	
}
