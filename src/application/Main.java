package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javazoom.jl.player.Player;
import raining.animations.GameOverAnimation;
import raining.dialog.RulesAndInformation;
import raining.exceptions.NoMoreRainDropsException;
import raining.souds.SoundManager;
import raining.ui.components.Cloud;
import raining.ui.components.Cup;
import raining.ui.components.RainDrop;
import raining.ui.components.Sun;

/**
 * The main class which is called by JVM to start executing. This class
 * initialise the UI components and the behaviour of the game. The game plays
 * with 3 Threads : 1 thread play the raining animation 2 thread play the
 * counter for which the player must catch all the rainingdrops 3 thread - the
 * main thread - which initialize all the components and handle all key inputs
 * for the game.
 * 
 * The Rules of the game is to catch all the raindrops for some Counter
 * seconds(per level) -if not (GAME OVER) if yes - play final animation - turn
 * the sun to YELLOW , and the clouds go away from the screen. When this
 * animation execute - the player will be asked if he wans to play the next
 * level or not. If he plays next level - the counter will be with less seconds
 * and more rain drops. And also contains Sound Manager - to play sounds in the
 * game process.- if the user exits with the X button - the Sound manager will
 * make sure to close the input stream.
 * 
 * @author asen
 *
 */
public class Main extends Application {

	public static final short WIDTH = 600;
	public static final short HEIGHT = 600;
	public int TIMER_SECONDS = 25;
	private static int NUMBER_OF_RAIN_DROPS = 500;

	private List<RainDrop> raindrops = new ArrayList<RainDrop>();

	private Cloud[] clouds;
	private Sun sun;
	private Cup cup;

	private Runnable timer;
	private Thread timerThread;
	Runnable gameLoop;
	private Random randomNumbers;
	private Pane pane = new Pane();

	private Text counterText;

	private boolean theGameisOver = false;

	private Button startGame, exit, nextLevel, rules;

	private int level = 1;

	private SoundManager soundPlayer;
	private Text gameOver;

	/**
	 * method start initialize all the components and puts them in Scene and
	 * show to the user
	 */
	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("Catch the rain");
		randomNumbers = new Random();

		pane = new Pane();

		sun = new Sun();
		pane.getChildren().add(sun);

		clouds = Cloud.getClouds();
		pane.getChildren().addAll(clouds);

		pane.getChildren().add(createTextCounter());

		RainDrop.setRaining(true);

		for (int i = 0; i < NUMBER_OF_RAIN_DROPS; i++) {
			RainDrop raindrop = new RainDrop((double) randomNumbers.nextInt(600), (double) randomNumbers.nextInt(600));
			raindrop.setStroke(Color.BLUE);
			raindrops.add(raindrop);
			pane.getChildren().add(raindrop);
		}

		pane.setTranslateX(0);
		pane.setTranslateY(0);
		pane.setMinWidth(WIDTH);
		pane.setMinHeight(HEIGHT);
		pane.setId("background");

		pane.getChildren().addAll(createCup());
		Scene scene = new Scene(pane, WIDTH, HEIGHT);

		scene.setOnKeyPressed(keyInput -> {
			keyBoardInput(keyInput);
		});

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		initButtons();

		stage.setResizable(false);

		stage.setScene(scene);
		stage.show();

		gameOver = new Text();

		pane.getChildren().addAll(gameOver);
		stage.setOnCloseRequest(request -> {
			onPressXButton();
		});
		initThreadsForTheGame();

	}

	/**
	 * make sure the music input stream close
	 * 
	 * @author asen
	 */
	private void onPressXButton() {
		if (soundPlayer != null) {
			soundPlayer.stopThePlayer();
		}

	}

	/**
	 * init only buttons and all of them click behaviours
	 * 
	 * @author asen
	 */
	private void initButtons() {
		startGame = new Button("Start Game");

		startGame.setTranslateX(200);

		startGame.setTranslateY(200);
		startGame.setMinSize(250, 50);
		startGame.setFont(new Font(30));
		startGame.setOnAction(el -> {
			level++;
			setVisibility(false, startGame, exit, rules);
			changeLevel(level);

		});

		exit = new Button("EXIT GAME");
		exit.setTranslateX(200);

		exit.setTranslateY(300);
		exit.setMinSize(250, 50);
		exit.setFont(new Font(30));
		exit.setOnAction(el -> {
			System.exit(1);
		});

		nextLevel = new Button("NEXT LEVEL");
		nextLevel.setFont(new Font(30));
		nextLevel.setTranslateX(200);

		nextLevel.setTranslateY(200);
		nextLevel.setMinSize(250, 50);
		nextLevel.setVisible(false);
		nextLevel.setOnAction(click -> {
			level++;
			setVisibility(false, nextLevel, exit);
			changeLevel(level);
			moveballe = true;
			nextLevel.setText("NEXT LEVEL " + level);
		});

		rules = new Button("Rules");

		rules.setTranslateX(200);

		rules.setTranslateY(400);
		rules.setMinSize(250, 50);
		rules.setFont(new Font(30));
		setVisibility(true, startGame, exit, rules);

		rules.setOnAction(rules -> {
			new RulesAndInformation().show();
		});
		pane.getChildren().addAll(startGame, exit, nextLevel, rules);

	}

	/**
	 * create text object with yellow color
	 * 
	 * @return
	 */
	private Text createTextCounter() {
		counterText = new Text("");
		counterText.setTranslateX(300);
		counterText.setTranslateY(50);
		counterText.setFont(new Font(30));
		counterText.setFill(Color.YELLOW);
		return counterText;
	}

	/**
	 * here i init one thread for the raining animation , and another for the
	 * counter
	 * 
	 * 
	 * gameLoop -sets cloud position, then turn the sun to BLACK color,and play
	 * the raining animation - for every rain call
	 * cup.checkIfSingleRainIsInTheCup(raindrops.get(i)) - which check if this
	 * raindrop is in the cup - if it is - then destroy it. this thread handel
	 * user define exception NoMoreRainDropsException - which is thrown when we
	 * have no more rain drops to catch.
	 * 
	 * 
	 * the second thread is timer thread - which count the seconds remaining -
	 * if is zero - GAME OVER.
	 * 
	 * @author asen
	 */
	private void initThreadsForTheGame() {

		gameLoop = new Runnable() {

			@Override
			public void run() {

				Cloud.resetCloudPosition();
				sun.turnBlack();
				theGameisOver = false;

				if (soundPlayer == null) {
					soundPlayer = new SoundManager();
				}
				soundPlayer.playRainingDropsSound();
				try {
					while (true) {

						for (int i = 0; i < raindrops.size(); i++) {

							try {
								if (raindrops.get(i) == null) {
									raindrops.remove(i);
								}

								raindrops.get(i).move();

								cup.checkIfSingleRainIsInTheCup(raindrops.get(i));
							} catch (ArrayIndexOutOfBoundsException e) {
								continue;
							}

						}

						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							continue;
						}
					}
				}

				catch (NoMoreRainDropsException e) {
					soundPlayer.playCloudGoAwaySound();
					theGameisOver = true;
					moveballe = false;
					counterText.setText("");
					sun.turnYellow();
					Cloud.destroyClouds();

					setVisibility(true, nextLevel, exit);
					if (timerThread.isAlive()) {
						try {
							timerThread.join();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}

		};

		timer = new Runnable() {

			@Override
			public void run() {

				theGameisOver = false;
				for (int count = TIMER_SECONDS; count > 0; count--) {

					if (theGameisOver) {
						counterText.setText("");
						break;
					}

					colorTextByCount(count);
					counterText.setText(Integer.toString(count));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						continue;
					}

				}

				if (!sun.isTheSunYellow() && !theGameisOver) {
					RainDrop.setRaining(false);
					soundPlayer.playGameOverSound();
					gameOver.setText("GAME OVER \nHigh score " + cup.getHighScore());
					try {
						// this method returns Thread and i call the join method
						// to simply wait
						// till the animation(thread) is over.
						GameOverAnimation.playAnimation(gameOver).join();
					} catch (InterruptedException e) {

					}

					System.exit(1);
				}
			}

		};

	}

	/**
	 * method to set visibility to all the buttons we want .
	 * 
	 * @param visibility
	 *            - true or false
	 * @param buttons
	 *            - some buttons specified
	 * @author asen
	 */
	private void setVisibility(boolean visibility, Button... buttons) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setVisible(visibility);
		}
	}

	private static final int NUMBER_OF_MORE_RAINS_BY_LEVEL = 200;

	/**
	 * This method change the level of the game,add more raindrops per level and
	 * start two new thread.
	 * 
	 * @param level
	 * @author asen
	 */
	private void changeLevel(int level) {

		cup.setHighScore(cup.getHighScore() + raindrops.size());

		TIMER_SECONDS = analizeLevelSpeedByLevel(level);

		for (int i = 0; i < NUMBER_OF_MORE_RAINS_BY_LEVEL; i++) {
			RainDrop newRain = new RainDrop((double) randomNumbers.nextInt(600), (double) randomNumbers.nextInt(600));
			newRain.setStroke(Color.BLUE);
			raindrops.add(newRain);
			pane.getChildren().add(newRain);
		}

		for (int i = 0; i < raindrops.size(); i++) {

			if (raindrops.get(i) == null) {
				raindrops.remove(i);
				RainDrop.decrement();
			} else {

				resetSingleRain(raindrops.get(i));
			}
		}

		Thread th = new Thread(gameLoop);
		th.start();
		timerThread = new Thread(timer);
		timerThread.start();

	}

	/**
	 * this method analize the level number , and return the speed for the
	 * specific level.
	 * 
	 * @param level
	 *            - which level is next
	 * @return number of seconds for the counter on this level
	 * @author asen
	 */
	private int analizeLevelSpeedByLevel(int level) {
		if (level > 0 && level < 4) {
			return 25;
		}

		if (level > 3 && level < 7) {
			return 20;
		}
		if (level > 6 && level < 10) {
			return 15;
		}
		if (level > 9 && level < 14) {
			return 10;
		}

		return 8;
	}

	/**
	 * this method reset single rain - if it was destroyed
	 * 
	 * @param rainDrop
	 */
	private void resetSingleRain(RainDrop rainDrop) {
		if (rainDrop != null) {
			rainDrop.reset((double) randomNumbers.nextInt(600), (double) randomNumbers.nextInt(600));
		}

	}

	/**
	 * this method is called every 1 second - if timer is at more then 3 - the
	 * color is set to YELLOW if is less then 3 ( 1-3 )- is it RED.
	 * 
	 * @param count
	 *            - timers count.
	 * @author asen
	 */
	private void colorTextByCount(int count) {
		if (count > 3)
			counterText.setFill(Color.YELLOW);
		else
			counterText.setFill(Color.RED);
	}

	private boolean moveballe = true;

	/**
	 * handels the user input and move the cup left or Right
	 * 
	 * @param keyInput
	 * @author asen
	 */

	private void keyBoardInput(javafx.scene.input.KeyEvent keyInput) {

		if (moveballe) {

			switch (keyInput.getCode()) {
			case LEFT: {
				cup.moveLeft();
				break;

			}
			case RIGHT: {
				cup.moveRight();
				break;
			}

			case A: {
				cup.moveLeft();
				break;
			}

			case D: {
				cup.moveRight();
				break;
			}
			default:
				break;
			}
		}
	}

	/**
	 * this methods create cup ass array of Elipses
	 * 
	 * @return array of ellipses
	 * @author asen
	 */
	private Ellipse[] createCup() {
		cup = new Cup();

		return cup.getParts();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
