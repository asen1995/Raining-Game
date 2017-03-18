package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import raining.exceptions.NoMoreRainDropsException;
import raining.ui.components.Cloud;
import raining.ui.components.Cup;
import raining.ui.components.RainDrop;
import raining.ui.components.Sun;



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

	private Button startGame, exit, nextLevel;

	private int level = 1;

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
		initThreadsForTheGame();

	}

	private void initButtons() {
		startGame = new Button("Start Game");

		startGame.setTranslateX(200);

		startGame.setTranslateY(200);
		startGame.setMinSize(250, 50);
		startGame.setFont(new Font(30));
		startGame.setOnAction(el -> {
			level++;
			setVisibility(false, startGame, exit);
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
		setVisibility(true, startGame, exit);
		pane.getChildren().addAll(startGame, exit, nextLevel);

	}

	private Text createTextCounter() {
		counterText = new Text("");
		counterText.setTranslateX(300);
		counterText.setTranslateY(50);
		counterText.setFont(new Font(30));
		counterText.setFill(Color.YELLOW);
		return counterText;
	}

	private void initThreadsForTheGame() {

		System.out.println(raindrops.size());

		gameLoop = new Runnable() {

			@Override
			public void run() {

				Cloud.resetCloudPosition();
				sun.turnBlack();
				theGameisOver = false;
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
								System.out.println("Array index ....");
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
						System.out.println("The Player catch all the rain drops,so BREAK !");
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
					JOptionPane.showMessageDialog(null,
							"GAME OVER \n High score  = " + cup.getHighScore() + "\n at level " + level);
					System.exit(1);
				}
			}

		};

	}

	private void setVisibility(boolean visibility, Button... buttons) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setVisible(visibility);
		}
	}

	private static final int NUMBER_OF_MORE_RAINS_BY_LEVEL = 200;

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

		System.out.println("broi na kapkite sa " + RainDrop.numberOfLines);

		Thread th = new Thread(gameLoop);
		th.start();
		timerThread = new Thread(timer);
		timerThread.start();

	}

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

	private void resetSingleRain(RainDrop rainDrop) {
		if (rainDrop != null) {
			rainDrop.reset((double) randomNumbers.nextInt(600), (double) randomNumbers.nextInt(600));
		}

	}

	private void colorTextByCount(int count) {
		if (count > 3)
			counterText.setFill(Color.YELLOW);
		else
			counterText.setFill(Color.RED);
	}

	private boolean moveballe = true;

	private void keyBoardInput(javafx.scene.input.KeyEvent keyInput) {

		if (moveballe) {

			switch (keyInput.getCode()) {
			case LEFT: {
				cup.moveLeft();
				break;

			}
			case RIGHT: {
				cup.moveRight();
			}
			default:
				break;
			}
		}
	}

	private Ellipse[] createCup() {
		cup = new Cup();

		return cup.getParts();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
