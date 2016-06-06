package com.ash.bot;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import com.jacob.com.LibraryLoader;

import autoitx4java.AutoItX;

/**
 * @author Aaron Hilbig
 * 
 * Bot for macro automation using AutoIT and java.awt.Robot<br><br>
 * <h1>General help on how to use this:</h1><br>
 * Initialize & Set up for Key Listening:<br>
 * <code>Bot b = new Bot();<br>b.activateListener();</code>
 * <br><br>Setting a hotkey:<br>
 * <code>b.nativeMousePressed = (e) -> {System.out.println("Mouse pressed");};<br>
 * b.setHotkey(NativeKeyEvent.VC_F, (o) -> {System.out.println("F wurde gedrückt");});</code><br><br>
 * Using AutoIt:<br>
 * <code>String notepad = "Unbenannt";<br>
        String testString = "this is a test.";<br>
        b.perform().run("notepad.exe", "c:\\", AutoItX.SW_RESTORE);<br>
        b.perform().winActivate(notepad);<br>
        b.perform().winWaitActive(notepad);<br>
        b.perform().send(testString);<br>
        b.perform().winKill(notepad);<br>
        b.perform().toolTip("Just closed Notepad.", 10, 10);</code>
 * @author Aaron Hilbig
 *
 * 
 * 
 */
public class Bot implements NativeKeyListener, NativeMouseListener {
	
	/**
	 * Container for the AWT.Robot part of the bot. Used to execute Robot tasks.
	 */
	public Robot awt;
	
	/**
	 * Container for the AutoIt tasks. Used to execute AutoIt commands (See AutoIt help-page)
	 */
	public AutoItX autoit;
	
	/**
	 * Saved Hotkeys from setHotkey().
	 */
	HashMap<String, Consumer<Object>> hotkeys = new HashMap<>();
	
	/**
	 * The Function called when the mouse is clicked.
	 * <br> Requires the Listener to be active (call activateListener())
	 */
	public Consumer<NativeMouseEvent> nativeMouseClicked;
	
	/**
	 * The Function called when the mouse is pressed.
	 * <br> Requires the Listener to be active (call activateListener())
	 */
	public Consumer<NativeMouseEvent> nativeMousePressed;
	
	/**
	 * The Function called when the mouse is released.
	 * <br> Requires the Listener to be active (call activateListener())
	 */
	public Consumer<NativeMouseEvent> nativeMouseReleased;
	
	/**
	 * The Function called when a key is pressed.
	 * <br> Requires the Listener to be active (call activateListener())
	 */
	public Consumer<NativeKeyEvent> nativeKeyPressed;
	
	/**
	 * The Function called when a key is released.
	 * <br> Requires the Listener to be active (call activateListener())
	 */
	public Consumer<NativeKeyEvent> nativeKeyReleased;
	
	/**
	 * The Function called when a key is typed.
	 * <br> Requires the Listener to be active (call activateListener())
	 */
	public Consumer<NativeKeyEvent> nativeKeyTyped;
	
	/**
	 * Constructor<br>
	 * Initalizes AWT.Robot (AutoDelay, AutoWaitForIdle), sets JACOB_DLL_PATH, starts AutoIt bridge.
	 * @throws AWTException
	 */
	public Bot() throws AWTException{
		File file = new File("src", "jacob-1.18-x64.dll"); //path to the jacob dll
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		awt = new Robot();
		awt.setAutoWaitForIdle(true);
		awt.setAutoDelay(10);
		autoit = new AutoItX();
		
		// Stop JNativeHook from Spamming
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}
	
	public boolean activateListener(){
		try {
			
			GlobalScreen.registerNativeHook();
			
		}  catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			return false;
		} catch(Exception e){
			e.printStackTrace();
		}
		GlobalScreen.addNativeKeyListener(this);
		GlobalScreen.addNativeMouseListener(this);
		return true;
	}
	
	public boolean activateListener(Consumer<NativeMouseEvent> nativeMouseClicked,
			Consumer<NativeMouseEvent> nativeMousePressed,
			Consumer<NativeMouseEvent> nativeMouseReleased,
			Consumer<NativeKeyEvent> nativeKeyPressed,
			Consumer<NativeKeyEvent> nativeKeyReleased,
			Consumer<NativeKeyEvent> nativeKeyTyped){
		try {
			
			GlobalScreen.registerNativeHook();
			
		}  catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			return false;
		} catch(Exception e){
			e.printStackTrace();
		}
		GlobalScreen.addNativeKeyListener(this);
		GlobalScreen.addNativeMouseListener(this);
		
		this.nativeMouseClicked = nativeMouseClicked;
		this.nativeMousePressed = nativeMousePressed;
		this.nativeMouseReleased = nativeMouseReleased;
		this.nativeKeyPressed = nativeKeyPressed;
		this.nativeKeyReleased = nativeKeyReleased;
		this.nativeKeyTyped = nativeKeyTyped;
		
		return true;
	}
	
	public Point getMousePos(){
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	public void leftClick(){
		awt.mousePress(InputEvent.BUTTON1_MASK);
		awt.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	public void leftClickHold(){
		awt.mousePress(InputEvent.BUTTON1_MASK);
	}
	
	public void leftClickRelease(){
		awt.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	/**
	 * Moves the mouse smoothly with speed speed.
	 * @param x
	 * @param y
	 * @param speed
	 */
	public void moveMouse(int x, int y, int speed){
		int posX = (int) getMousePos().getX();
		int posY = (int) getMousePos().getY();
		int diffX = x - posX;
		int diffY = y - posY;
		int newPosX = posX;
		int newPosY = posY;
		if(posX != x || posY != y){
			while(posX != x || posY != y){
				if(posX != x){
					boolean modif = false;
					if(diffX > 0){
						if(diffX - speed < 0){
							//newPosX -= -1 * (speed - diffX);
							if(diffX != 0)
								newPosX--;
							System.out.println("" + speed + " / " + diffX);
						} else {
							newPosX -= speed;
							modif = true;
						}
					} 
					if(diffX < 0 && !modif){
						if(diffX + speed > 0){
							//newPosX += -1 * (speed - diffX);
							if(diffX != 0)
								newPosX++;
							System.out.println("" + speed + " / " + diffX);
						} else {
							newPosX += speed;
							modif = true;
						}
					} 
					if(diffX == 0 && !modif){
						newPosX = posX;
						modif = true;
					}
				}
				if(posY != y){
					boolean modif = false;
					if(diffY > 0){
						if(diffY - speed < 0){
							//newPosY -= -1 * (speed - diffY);
							if(diffY != 0)
								newPosY--;
							System.out.println("" + speed + " / " + diffY);
						} else {
							newPosY -= speed;
							modif = true;
						}
					} 
					if(diffY < 0 && !modif){
						if(diffY + speed > 0){
							//newPosY += -1 * (speed - diffY);
							if(diffY != 0)
							newPosY++;
							System.out.println("" + speed + " / " + diffY);
						} else {
							newPosY += speed;
							modif = true;
						}
					} 
					if(diffY == 0 && !modif){
						newPosY = posY;
						modif = true;
					}
				}
				
				awt.mouseMove(newPosX, newPosY);
				posX = (int) getMousePos().getX();
				posY = (int) getMousePos().getY();
				diffX = posX - x;
				diffY = posY - y;
				newPosX = posX;
				newPosY = posY;
			}
		}
	}
	
	public void rightClick(){
		awt.mousePress(InputEvent.BUTTON3_MASK);
		awt.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	
	public boolean deactivateListener(){
		try {
			
			GlobalScreen.removeNativeKeyListener(this);
			GlobalScreen.removeNativeKeyListener(this);
			GlobalScreen.unregisterNativeHook();
			
		}  catch (NativeHookException ex) {
			System.err
					.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			return false;
		} catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * Lets the AutoIt-Bot select a window.<br>
	 * (Required for internal operations)
	 * @param title
	 */
	public void focusWindow(String title){
		autoit.winActivate(title);
	}
	
	public Consumer<NativeKeyEvent> getNativeKeyPressed() {
		return nativeKeyPressed;
	}
	
	public Consumer<NativeKeyEvent> getNativeKeyReleased() {
		return nativeKeyReleased;
	}

	public Consumer<NativeKeyEvent> getNativeKeyTyped() {
		return nativeKeyTyped;
	}

	public Consumer<NativeMouseEvent> getNativeMouseClicked() {
		return nativeMouseClicked;
	}

	public Consumer<NativeMouseEvent> getNativeMousePressed() {
		return nativeMousePressed;
	}

	public Consumer<NativeMouseEvent> getNativeMouseReleased() {
		return nativeMouseReleased;
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if(nativeKeyPressed!=null){
			nativeKeyPressed.accept(e);
		}
		try {
			hotkeys.get(NativeKeyEvent.getKeyText(e.getKeyCode())).accept(e);
		} catch(Exception exc){}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if(nativeKeyReleased!=null){
			nativeKeyReleased.accept(e);
		}
	}
	
	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		if(nativeKeyTyped!=null){
			nativeKeyTyped.accept(e);
		}
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		if(nativeMouseClicked!=null){
			nativeMouseClicked.accept(e);
		}
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		if(nativeMousePressed!=null){
			nativeMousePressed.accept(e);
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		if(nativeMouseReleased!=null){
			nativeMouseReleased.accept(e);
		}
	}

	public AutoItX perform(){
		return autoit;
	}

	public Robot performAWT(){
		return awt;
	}
	
	public BufferedImage makeScreenshot(Rectangle r){
		return performAWT().createScreenCapture(r);
	}

	/**
	 * Set a hotkey that, when pressed, executes the Consumer with the KeyEvent as argument
	 * <br> Call activateListener() first.
	 * 
	 * @param i = NativeKeyEvent-KeyCode!
	 * @param c
	 */
	public void setHotkey(int i, Consumer<Object> c){
		hotkeys.put(NativeKeyEvent.getKeyText(i), c);
	}

	public void setNativeKeyPressed(Consumer<NativeKeyEvent> nativeKeyPressed) {
		this.nativeKeyPressed = nativeKeyPressed;
	}

	public void setNativeKeyReleased(Consumer<NativeKeyEvent> nativeKeyReleased) {
		this.nativeKeyReleased = nativeKeyReleased;
	}

	public void setNativeKeyTyped(Consumer<NativeKeyEvent> nativeKeyTyped) {
		this.nativeKeyTyped = nativeKeyTyped;
	}

	public void setNativeMouseClicked(Consumer<NativeMouseEvent> nativeMouseClicked) {
		this.nativeMouseClicked = nativeMouseClicked;
	}

	public void setNativeMousePressed(Consumer<NativeMouseEvent> nativeMousePressed) {
		this.nativeMousePressed = nativeMousePressed;
	}

	public void setNativeMouseReleased(Consumer<NativeMouseEvent> nativeMouseReleased) {
		this.nativeMouseReleased = nativeMouseReleased;
	}
	
}
