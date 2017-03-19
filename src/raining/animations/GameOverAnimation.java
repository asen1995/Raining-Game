package raining.animations;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This class represents the Game Over animation in the game.
 * 
 * @author asen
 *
 */
public class GameOverAnimation {

	private static byte fontSize = 3;

	/**
	 * Create new thread - which manipulate text object and increase his size
	 * till from small text to some big font size.
	 * 
	 * @param text
	 *            - recieves text and create animation with him.
	 * @return the Thread of the animation , so the class invoke this method ,
	 *         can invoke the join() method of the thread to wait till is over.
	 */
	public static Thread playAnimation(Text text) {
		text.setTranslateX(500);
		text.setTranslateY(300);

		text.setFill(Color.RED);
		text.setFont(new Font(fontSize));
		text.setStroke(Color.BLACK);
		Thread animation = new Thread(new Runnable() {

			@Override
			public void run() {

				while (text.getFont().getSize() < 52) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						continue;
					}

					text.setTranslateX(text.getTranslateX() - 7);
					fontSize += 1.5;
					text.setFont(new Font(fontSize));
				}
			}
		});

		animation.start();
		return animation;
	}

}
