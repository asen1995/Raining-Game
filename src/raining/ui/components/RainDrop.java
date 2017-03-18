package raining.ui.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import raining.exceptions.NoMoreRainDropsException;

/**
 * With this class we create a single rainDrop for the game - which represents
 * line
 * 
 * @author asen
 *
 */
public class RainDrop extends Line {

	private double currentY;

	public static short numberOfLines = 0;

	private boolean alive;

	/**
	 * 
	 * constructor for every raindrop - every raindrop's height is 20
	 * 
	 * @param startPointX
	 * @param startPointY
	 * @author asen
	 */
	public RainDrop(double startPointX, double startPointY) {
		alive = true;

		setStartX(startPointX);
		setEndX(startPointX);
		currentY = startPointY;

		setStartY(currentY);
		setEndY(currentY + 20);
		setFill(Color.BLUE);

	}

	/**
	 * reset a single rainDrop.
	 * 
	 * @param startPointX
	 * @param startPointY
	 * @author asen
	 */
	public void reset(double startPointX, double startPointY) {
		alive = true;
		numberOfLines++;
		setStartX(startPointX);
		setEndX(startPointX);
		currentY = startPointY;

		setStartY(currentY);
		setEndY(currentY + 20);
	}

	/**
	 * when the raindrop drop in the cup - is called the clear method - which
	 * clear the raindrop from the screen
	 * 
	 * @author asen
	 */
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

	/**
	 * decrement the number of alive raindrop
	 * 
	 * @author asen
	 */
	public static void decrement() {
		numberOfLines--;
	}

	/**
	 * this method move a single rain drop to the bottom - if is more then the
	 * height of the screen-then the raindrop returns in the cloud and repeat -
	 * till its catch by the cup.
	 * 
	 * @throws NoMoreRainDropsException
	 *             - if we have zero rainDrops alive
	 */
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
