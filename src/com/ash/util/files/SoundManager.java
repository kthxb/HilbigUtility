package com.ash.util.files;

import java.io.File;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/**
 * Plays sounds. Very basic.
 * 
 * @author Aaron Hilbig
 *
 */
public class SoundManager {
	
	/*public static void main(String[] args) {
		SoundManager s = new SoundManager(FileManager.getFileWithDialog("", true, null, null));
		s.play();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		s.stop();
	}*/
	
	private Clip clip;
	private File f;
	
	public SoundManager(String url){
		try {
			clip = AudioSystem.getClip();
			f = new File(url);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void play(){
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(f);
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try {
			clip.stop();
			clip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
