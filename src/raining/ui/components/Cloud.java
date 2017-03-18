package raining.ui.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
/**
 * create Cloud UI component from 3 ellipses 
 * @author asen
 *
 */
public class Cloud extends Ellipse {

	public static Cloud[] clouds;

	/**
	 * iit the 3 ellipses for the cloud and set the starting positions
	 *  @author asen
	 */
	private static void initClouds() {

		if (clouds == null) {
			clouds = new Cloud[3];
			for (int i = 0; i < clouds.length; i++) {

				clouds[i] = new Cloud();
			}

		}
		int centerX = 150;

		for (int i = 0; i < clouds.length; i++) {

			clouds[i].setCenterX(centerX);
			centerX += 170;
			clouds[i].setCenterY(50);
			clouds[i].setFill(Color.DARKBLUE);
			clouds[i].setRadiusX(150);
			clouds[i].setRadiusY(60);
		}
	}
/**
 * if we start new level - the cloud must be back at the starting position.
 *  @author asen
 */
	public static void resetCloudPosition() {
		initClouds();
	}

	/**
	 * return the cloud object as array of clouds
	 * @return
	 *  @author asen
	 */
	public static Cloud[] getClouds() {
		if (clouds == null) {
			initClouds();
		}
		return clouds;
	}
/**
 * this method create thread which play the "go away cloud " animation , and waits until is over.
 * @return
 *  @author asen
 */
	public static boolean destroyClouds() {

		Thread cloudsGoAwayThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (clouds[2].getCenterX() != -100) {
					for (int i = 0; i < clouds.length; i++) {
						clouds[i].setCenterX(clouds[i].getCenterX() - 10);

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							continue;

						}

					}
				}
			}
		});

		cloudsGoAwayThread.start();

		while (cloudsGoAwayThread.isAlive()) {

		}
		
		return true;
	}

	private Cloud() {

	}

}
