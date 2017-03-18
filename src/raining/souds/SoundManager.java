package raining.souds;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * SoundManager maneges the game by play the sound that is needed.
 * 
 * @author asen
 *
 */
public class SoundManager {

	private static final String RAIN_DROPS_SOUND_URL = "sounds/RainDrops.mp3";
	private static final String NO_MORE_RAIN_SOUND_URL = "sounds/nonRaining.mp3";
	private static final String GAME_OVER_SOUND = "sounds/game_over.mp3";

	private Player player;

	public void playCloudGoAwaySound() {
		playAudio(NO_MORE_RAIN_SOUND_URL);
	}

	public void playRainingDropsSound() {

		playAudio(RAIN_DROPS_SOUND_URL);

	}

	public void playGameOverSound() {
		playAudio(GAME_OVER_SOUND);
	}

	/**
	 * this method is called by either two methods to play one of the two mp3
	 * files
	 * 
	 * @param audio
	 *            - is one of the two final String paths declered in the
	 *            begining of the class
	 * @author asen
	 */
	private void playAudio(String audio) {

		stopThePlayer();
		new Thread(new Runnable() {

			@Override
			public void run() {

				try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(new File(audio)))) {

					player = new Player(reader);
					player.play();

				}

				catch (FileNotFoundException e) {
					player.close();
				} catch (IOException e) {
					player.close();
				} catch (JavaLayerException e) {
					player.close();
				}
			}
		}).start();
	}

	/*
	 * this methods stops the player if he play some of the sounds
	 */
	public void stopThePlayer() {
		if (player != null) {
			player.close();
		}
	}

}
