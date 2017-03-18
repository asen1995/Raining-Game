package raining.ui.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Cloud extends Ellipse {

	public static Cloud[] clouds;

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

	public static void resetCloudPosition() {
		initClouds();
	}

	public static Cloud[] getClouds() {
		if (clouds == null) {
			initClouds();
		}
		return clouds;
	}

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
