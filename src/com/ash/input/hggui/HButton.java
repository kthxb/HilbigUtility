package com.ash.input.hggui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import com.ash.util.files.EZRGB;
import com.ash.util.files.FileManager;
import com.ash.util.files.SoundManager;

/**
 * Special designed button component for use with {@link HWindow}.
 * Recommended syntax:
 * <pre> HButton button = new HButton("Title",width,height,bgcolor,new Font("Arial",1,fontsize),this); //'this' referrs to the HWindow parent.
 * button.setHoverColor(new EZRGB(100,100,255).toColor()); //define hover color.
 * button.addTo(getSurface()); //Add it to the window</pre>
 * @author Aaron Hilbig
 *
 */
@SuppressWarnings("serial")
public class HButton extends Component {
	
	private String text;
	private Rectangle bounds;
	private boolean enabled = true;
	private boolean hover = false;
	private boolean animation = false;
	private boolean border = false;
	private Font font = new Font("Arial",1,11);
	private Color color, hoverColor, currentColor;
	private Color disabledColor = Color.GRAY;
	public TextCanvas drawable;
	private HButtonMouseListener listener;
	
	public boolean isAnimation() {
		return animation;
	}

	public void setAnimation(boolean animation) {
		this.animation = animation;
	}

	public Color getCurrentColor() {
		if(!isEnabled()){
			return disabledColor;
		} else if(isAnimation()){
//			System.out.println("Animation is running, returning currentColor");
			return currentColor;
		}
		return isAnimation() ? currentColor : isEnabled() ? isHover() ? hoverColor : color : disabledColor;
		//return currentColor;
	}
	/*public Color getBGColor() {
		return isAnimation() ? currentColor : isEnabled() ? isHover() ? hoverColor : color : disabledColor;
		//return currentColor;
	}*/

	public void setCurrentColor(Color currentColor) {
		this.currentColor = currentColor;
	}

	public Canvas getDrawable() {
		return drawable;
	}

	public void setDrawable(TextCanvas drawable) {
		this.drawable = drawable;
	}

	public HButtonMouseListener getListener() {
		return listener;
	}

	public void setListener(HButtonMouseListener listener) {
		this.listener = listener;
	}
	
	public HButton(String text) {
		this.text = text;
		this.bounds = new Rectangle(0,0,100, 100);
		this.enabled = true;
		this.color = Color.RED;
		this.disabledColor = Color.GRAY;
		this.hoverColor = color.brighter();
		
		initializeActionListener();
		initializeCanvas();
	}
	
	public HButton(String text, int width, int height, Color color) {
		this.text = text;
		this.bounds = new Rectangle(0,0,width, height);
		this.enabled = true;
		this.color = color;
		this.disabledColor = Color.GRAY;
		this.hoverColor = color.brighter();
		initializeActionListener();
		initializeCanvas();
	}
	
	public HButton(String text, int width, int height, Color color, Font f, HWindow parent) {
		this.text = text;
		this.bounds = new Rectangle(0,0,width, height);
		this.enabled = true;
		this.color = color;
		this.disabledColor = Color.GRAY;
		this.hoverColor = color.brighter();
		this.font = f;
		this.parent = parent;
		initializeActionListener();
		initializeCanvas();
	}
	
	public HButton(String text, int width, int height, Color color, Font f) {
		this.text = text;
		this.bounds = new Rectangle(0,0,width, height);
		this.enabled = true;
		this.color = color;
		this.disabledColor = Color.GRAY;
		this.hoverColor = color.brighter();
		this.font = f;
		initializeActionListener();
		initializeCanvas();
	}
	
	public HButton(String text, Rectangle bounds, Color color) {
		this.text = text;
		this.bounds = bounds;
		this.enabled = true;
		this.color = color;
		this.disabledColor = Color.GRAY;
		this.hoverColor = color.brighter();
		initializeActionListener();
		initializeCanvas();
	}
	
	public void updateCanvas(){
		drawable.setBackground(getCurrentColor());
		drawable.repaint();
//		System.out.println("Updating canvas with " + getCurrentColor());
		//drawable.setBounds(getBounds());
	}
	
	private void initializeCanvas(){
		drawable = new TextCanvas(text, Color.white, font);
		((TextCanvas) drawable).setBorder(hasBorder());
		((TextCanvas) drawable).setUsedFont(font);
		drawable.setBackground(getCurrentColor());
		drawable.setBounds(getBounds());
		drawable.addMouseListener(listener);
		currentColor = color;
//		alwaysFadeColorBack();
		updateCanvas();
	}
	
	private void initializeActionListener(){
		listener = new HButtonMouseListener(this);
		//addMouseListener(new HButtonMouseListener());
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateCanvas();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getDisabledColor() {
		return disabledColor;
	}

	public void setDisabledColor(Color disabledColor) {
		this.disabledColor = disabledColor;
	}
	
	public void addActionListener(ActionListener a){
		this.addActionListener(a);
	}
	
	public Color getHoverColor() {
		return hoverColor;
	}

	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
	}

	public boolean isHover() {
		return hover;
	}

	public void setHover(boolean hover) {
		this.hover = hover;
	}
	
	private Thread currentAnimationThread = null;
	private Consumer<MouseEvent> onClick;
	
	/*public void alwaysFadeColorBack(){
		System.out.println("alwaysFadeColorBack");
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					System.out.println("isEnabled()=" + isEnabled() + "\tisAnimation()=" + isAnimation() + "\tisHover()=" + isHover());
					if(!getCurrentColor().equals(color) && !isHover() && isEnabled()){
						setAnimation(true);
						System.out.println("setAnimation(true);");
						while(!getCurrentColor().equals(color) && !isHover() && isEnabled()){
							EZRGB color1 = new EZRGB(getCurrentColor().getRed(), getCurrentColor().getBlue(), getCurrentColor().getBlue());
							EZRGB color2 = new EZRGB(color.getRed(), color.getBlue(), color.getBlue());
							if(color1.r!=color2.r)
								color1.r = color1.r + color1.r > color2.r ? -1 : 1;
							if(color1.g!=color2.g)
								color1.g = color1.g + color1.g > color2.g ? -1 : 1;
							if(color1.b!=color2.b)
								color1.b = color1.b + color1.b > color2.b ? -1 : 1;
							setCurrentColor(color1.toColor());
							updateCanvas();
						}
						setAnimation(false);
					}
					if(!getCurrentColor().equals(color) && isEnabled() && !isAnimation() && !isHover()){
						setCurrentColor(color);
						updateCanvas();
						System.out.println("isEnabled()&&!isAnimation()");
					} 
					if(!getCurrentColor().equals(hoverColor) && isEnabled() && !isAnimation() && isHover()){
						setCurrentColor(hoverColor);
						updateCanvas();
						System.out.println("isEnabled()&&isHover()");
					} 
					if(!getCurrentColor().equals(disabledColor) && !isAnimation() && !isEnabled()){
						setCurrentColor(disabledColor);
						updateCanvas();
						System.out.println("!isEnabled()");
					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}*/
	
	public void fadeColor(Color c, int delay){
		setAnimation(true);
		//Color startColor = getHoverColor();
		/*if(false && currentAnimationThread!=null){
			try {
				currentAnimationThread.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		
		currentAnimationThread = new Thread(new Runnable(){
			@Override
			public void run() {
				
				/*EZRGB c = new EZRGB(hoverColor);
				EZRGB v = new EZRGB(color);
				
				for(float f = 0; f<1.05F; f+=0.05F){
					Color mix = c.fadeColorHue(v, f).toColor();
					drawable.setBackground(mix);
					drawable.repaint();
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				drawable.setBackground(color);
				drawable.repaint();*/
				
				/*
				for(int i = 0; i<=10; i++){
					
					Color x = color, y = getHoverColor();
					float blending = i / 10;
	
					float inverse_blending = 1 - blending;
	
					float red =   x.getRed()   * blending   +   y.getRed()   * inverse_blending;
					float green = x.getGreen() * blending   +   y.getGreen() * inverse_blending;
					float blue =  x.getBlue()  * blending   +   y.getBlue()  * inverse_blending;
					
					//note that if i pass float values they have to be in the range of 0.0-1.0 
					//and not in 0-255 like the ones i get returned by the getters.
					Color blended = new Color (red / 255, green / 255, blue / 255);
					setCurrentColor(blended);
					//System.out.println("" + x + y + blended);
					updateCanvas();
				}*/
				/*for(int i = 0; i<=255; i++){
					setCurrentColor(new EZRGB(255 - i, 0, 0).toColor());
					updateCanvas();
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}*/
				
				/*Color c1 = getHoverColor();
				Color c2 = c;
				for(int i = 0; i<=255; i++){
					if(c1.equals(c2)) break;
					EZRGB color1 = new EZRGB(c1.getRed(), c1.getBlue(), c1.getBlue());
					EZRGB color2 = new EZRGB(c2.getRed(), c2.getBlue(), c2.getBlue());
					if(color1.r!=color2.r)
						color1.r = color1.r + color1.r > color2.r ? -1 : 1;
					if(color1.g!=color2.g)
						color1.g = color1.g + color1.g > color2.g ? -1 : 1;
					if(color1.b!=color2.b)
						color1.b = color1.b + color1.b > color2.b ? -1 : 1;
					c1 = color1.toColor();
					setCurrentColor(c1);
					updateCanvas();
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				
				
				float gHue = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[0];
			    float bHue = Color.RGBtoHSB(getHoverColor().getRed(), getHoverColor().getGreen(), getHoverColor().getBlue(), null)[0];
			    for (int i = 0; i < 100; i++) {
			        setCurrentColor(Color.getHSBColor(gHue + (i * (bHue - gHue) / 100), 1, 1));
			        System.out.println("Colerino: " + Color.getHSBColor(gHue + (i * (bHue - gHue) / 100), 1, 1));
			        updateCanvas();
			        try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }*/
			    
			    
				/*for(int i = 0; i<=255; i++){
					setCurrentColor(new EZRGB(255 - i, 0, 0).toColor());
					updateCanvas();
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}*/
				
				/*Color currentColor = startColor;
				while(!currentColor.equals(c)){
					//int r = getBackground().getRed();
					//int g = getBackground().getGreen();
					//int b = getBackground().getBlue();
					
					int red = (int)(0.1 * c.getRed() + 
			                (1 - 0.1) * startColor.getRed());
			        int green = (int)(0.1 * c.getGreen() + 
			                (1 - 0.1) * startColor.getGreen());
			        int blue = (int)(0.1 * c.getBlue() + 
			                (1 - 0.1) * startColor.getBlue());
			        // set our new color appropriately
			        currentColor = new Color(red, green, blue);
			        // force a repaint to display our oval with its new color
			        setBackground(currentColor);
			        updateCanvas();
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("fade");
				}*/
				setAnimation(false);
			}
		});
		currentAnimationThread.start();
	}
	
	public void onClick(Consumer<MouseEvent> e){
		this.onClick = e;
	}
	
	public boolean hasBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	@SuppressWarnings("serial")
	class TextCanvas extends Canvas {
		
		String text;
		Color textcolor;
		private Font f;
		private boolean border = false;
		
		public TextCanvas(String text, Color textcolor, Font f){
			super();	
			this.text = text;
			this.textcolor = textcolor;
			this.setUsedFont(f);
		}
		
		public TextCanvas(String text, Color textcolor){
			super();	
			this.text = text;
			this.textcolor = textcolor;
			this.setUsedFont(new Font("Arial", 1, 11));
		}
		
		public void paint(Graphics g){
			g.setColor(textcolor);
			Rectangle bds = getBounds();
			int middle = bds.width / 2;
			int middleHeight = bds.height / 2;
			int width = g.getFontMetrics().stringWidth(text);
			g.setFont(f);
			g.drawString(text, middle - width / 2, middleHeight + g.getFont().getSize()/2);
			if(hasBorder()){
				g.setColor(Color.white);
				g.drawRect(1, 1, bds.width-3, bds.height-3);
			}
			g.setColor(new Color(255, 255, 255, 100));
			//g.fillOval(0, -5, getBounds().width, 10);
		}

		public boolean hasBorder() {
			return border;
		}

		public void setBorder(boolean border) {
			this.border = border;
		}

		public Font getFont() {
			return f;
		}

		public void setUsedFont(Font f) {
			this.f = f;
		}
		
	}

	private class HButtonMouseListener implements MouseListener {
		
		private HButton ref;

		public HButtonMouseListener(HButton hButton) {
			ref = hButton;
			click = new SoundManager(FileManager.filenameToPackage("com.ash.input.hggui", "click.wav"));
			accept = new SoundManager(FileManager.filenameToPackage("com.ash.input.hggui", "accept.wav"));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(!isEnabled()) return;
			System.out.println("Click! " + ref.getText() + "\t" + e.getButton());
			
			if(parent!=null) parent.onButtonClick(ref.getText(), e);
			
			if(ref.onClick!=null)
				ref.onClick.accept(e);
			
			if(e.getButton()==1){
				accept = new SoundManager(FileManager.filenameToPackage("com.ash.input.hggui", "accept.wav"));
				accept.play();
			}
		}
		
		private SoundManager click, accept;

		@Override
		public void mouseEntered(MouseEvent e) {
			if(!isEnabled()) return;
			ref.setHover(true);
			ref.setColor(ref.getHoverColor());
			ref.updateCanvas();
			click = new SoundManager(FileManager.filenameToPackage("com.ash.input.hggui", "click.wav"));
			click.play();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(!isEnabled()) return;
			ref.fadeColor(ref.color, 10);
			//ref.setColor(ref.getColor());
			ref.setHover(false);
			ref.setColor(ref.getColor());
			ref.updateCanvas();
			
			ref.setAnimation(true);
			/*
			Thread t = new Thread(new Runnable(){
				@Override
				public void run() {
					Color c1 = ref.getHoverColor();
					Color c2 = ref.getCurrentColor();
					for(int i = 0; i<=255; i++){
						if(c1.equals(c2)) break;
						EZRGB color1 = new EZRGB(c1.getRed(), c1.getBlue(), c1.getBlue());
						EZRGB color2 = new EZRGB(c2.getRed(), c2.getBlue(), c2.getBlue());
						if(color1.r!=color2.r)
							color1.r = color1.r + color1.r > color2.r ? -1 : 1;
						if(color1.g!=color2.g)
							color1.g = color1.g + color1.g > color2.g ? -1 : 1;
						if(color1.b!=color2.b)
							color1.b = color1.b + color1.b > color2.b ? -1 : 1;
						c1 = color1.toColor();
						ref.setCurrentColor(c1);
						ref.updateCanvas();
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("..");
					}
					ref.setAnimation(false);
				}
			});
			t.start();*/
			ref.setAnimation(false);
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
		
	}
	
	public void resetHover(){
		fadeColor(color, 10);
		//ref.setColor(ref.getColor());
		setHover(false);
		setColor(getColor());
		updateCanvas();
	}
	
	/**
	 * Use instead of {@code Container.add(HButton)}
	 * @param c
	 */
	public void addTo(Container c){
		c.add(drawable);
	}
	
	public HWindow parent;
	
	/**
	 * Use instead of {@code Container.add(HButton)}
	 * @param c
	 */
	public void addToWindow(HWindow w){
		w.getSurface().add(drawable);
		parent = w;
	}
	
	/*public void drawComponent(Graphics g){
		
	}*/
	
}
