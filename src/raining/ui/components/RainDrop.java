package raining.ui.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import raining.exceptions.NoMoreRainDropsException;

public class RainDrop extends Line {

	private double currentY;

	public static short numberOfLines=0;

	private boolean alive;

	public RainDrop(double startPointX, double startPointY) {
		alive = true;
	
		setStartX(startPointX);
		setEndX(startPointX);
		currentY = startPointY;

		setStartY(currentY);
		setEndY(currentY + 20);
		setFill(Color.BLUE);

	}

	public void reset(double startPointX, double startPointY) {
		alive = true;
		numberOfLines++;
		setStartX(startPointX);
		setEndX(startPointX);
		currentY = startPointY;

		setStartY(currentY);
		setEndY(currentY + 20);
	}

	public void clear() {
		setStartX(0);
		setStartY(0);
		setEndX(0);
		setEndY(0);
		currentY = -1;
		numberOfLines--;
		alive = false;
		
	}

	public final boolean isAlive() {
		return alive;
	}

	public final void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public static void decrement(){
		numberOfLines--;
	}

	public void move() throws NoMoreRainDropsException {

	
		if (numberOfLines == 0 || numberOfLines < 0) {
			throw new NoMoreRainDropsException();
		}

		if (currentY < 0) {
			return;
		}
		if (currentY > 600)
			currentY = 110;
		setStartY(currentY);
		currentY += 20;
		setEndY(currentY);
	}

}
