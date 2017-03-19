package raining.dialog;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Rules UI stage.
 * 
 * @author asen
 *
 */
public class RulesAndInformation extends Stage {

	/**
	 * creates the stage
	 */
	public RulesAndInformation() {
		setTitle("Raining game - Rules");

		Scene scene = new Scene(createContent(), 300, 300);

		setScene(scene);
		setResizable(false);
	}

	/**
	 * create the content
	 * 
	 * @return
	 */
	private Parent createContent() {
		Text text = new Text();
		text.setText("Catch all the rain drops\n before the timer experies.\n"
				+ "With each level up the rain drops\n becomes more and more, and the \ntimer seconds become less\n"
				+ "The cup is moving with \n the buttons LEFT and RIGHT\n or A - D .\n\n"
				+ "Developed by: Asen Nikolaev\nBulgaria.");
		text.setFont(new Font(20));
		text.setTranslateY(20);
		Pane pane = new Pane(text);
		return pane;
	}

	/**
	 * call show() method to display the stage on the screen.
	 */
	public void showTheRules() {
		show();
	}
}
