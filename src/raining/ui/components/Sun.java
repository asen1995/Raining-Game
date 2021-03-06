package raining.ui.components;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
/**
 * This is the sun UI components - which can be only yellow or black , depends of the state of the game
 * @author asen
 *
 */
public class Sun extends Ellipse {

	private Color colorOfSun;

	public Sun() {
		colorOfSun = Color.BLACK;
		setCenterX(550.0f);
		setCenterY(90.0f);
		setRadiusX(50.0f);
		setRadiusY(50.0f);
		setFill(Color.BLACK);

	}

	public boolean turnYellow() {
		setFill(Color.YELLOW);
		return true;
	}

	public void turnBlack() {
		setFill(Color.BLACK);
	}

	public boolean isTheSunYellow() {
		return colorOfSun == Color.YELLOW;
	}
}
