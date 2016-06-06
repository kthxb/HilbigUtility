package com.ash.util.files;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;

import com.ash.util.math.EZMath;

/**
 * Object for allround color conversion and handling.
 * 
 * @author Aaron Hilbig
 *
 */
public class EZRGB {
	
	public static void main(String[] args) {
		String s = FileManager.getTextfileFromPackage("com.ash.util.files", "file.txt");
		System.out.println(s);
	}
	
	public static String colors;
	
	public int r, g, b;
	
	public EZRGB(String name){
		EZRGB temp = getColorByName(name);
		
		if(temp==null)
			throw new IllegalArgumentException("Couldn't find specified color: \"" + name + "\"");
		
		this.r = temp.r;
		this.g = temp.g;
		this.b = temp.b;
	}
	
	public EZRGB(Color c){
		new EZRGB(c.getRed(),c.getGreen(),c.getBlue());
	}
	
	public EZRGB(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		
		if(colors==null){
			try { colors = FileManager.getTextfileFromPackage("com.ash.util.files", "colors");}catch(Exception e){colors = colorNotFound;}
		}
	}
	
	public static EZRGB fromColor(Color c){
		return new EZRGB(c.getRed(),c.getGreen(),c.getBlue());
	}
	
	public Color toColor(){
		return new Color(r, g, b);
	}
	
	@Override
	public String toString() {
		return "EZRGB [r=" + r + ", g=" + g + ", b=" + b + "]";
	}

	public static Color getColorRGB(int r, int g, int b) {
		float[] c = Color.RGBtoHSB(r, g, b, null);
		return Color.getHSBColor(c[0], c[1], c[2]);
	}
	
	public String getColorName(){
		String[] index = colors.split("\n");
		String lastName = null;
		for(String s : index){
			if(s.startsWith("#") || s.startsWith("hsl"))
				continue;
			if(!s.startsWith("rgb")){
				lastName = s;
			} else if(s.replace("rgb(", "").replace(")", "").equals(r + "," + g + "," + b))
					return lastName;
		}
		return null;
	}
	
	public String getColorName(int rangeOfVariation){
		
		String colorName = getColorName();
		
		if(colorName!=null) return colorName;
		
		String[] index = colors.split("\n");
		String lastName = null;
		for(String s : index){
			if(s.startsWith("#") || s.startsWith("hsl"))
				continue;
			if(!s.startsWith("rgb")){
				lastName = s;
			} else {
				String[] colors = s.replace("rgb(", "").replace(")", "").split(",");
				int r0 = Integer.parseInt(colors[0]);
				int g0 = Integer.parseInt(colors[1]);
				int b0 = Integer.parseInt(colors[2]);
				boolean b1 = EZMath.inRange(r0, r, rangeOfVariation);
				boolean b2 = EZMath.inRange(g0, g, rangeOfVariation);
				boolean b3 = EZMath.inRange(b0, b, rangeOfVariation);
				if(b1 && b2 && b3)
					return lastName;
			}
		}
		return null;
	}
	
	public static EZRGB getColorByName(String name){
		if(colors==null){
			try { colors = FileManager.getTextfileFromPackage("com.ash.util.files", "colors");}catch(Exception e){colors = colorNotFound;}
		}
		String[] index = colors.split("\n");
		boolean exit = false;
		for(String s : index){
			if(s.startsWith("#") || s.startsWith("hsl"))
				continue;
			if(!s.startsWith("rgb") && s.equals(name)){
				exit = true;
			} else if(exit) {
				String[] color = s.replace("rgb(", "").replace(")", "").split(",");
				return new EZRGB(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
			}
		}
		return null;
	}
	
	public static void printAvailableColors(){
		if(colors==null){
			try { colors = FileManager.getTextfileFromPackage("com.ash.util.files", "colors");}catch(Exception e){colors = colorNotFound;}
		}
		int i = 1;
		String[] index = colors.split("\n");
		System.out.print("Available colors:\n\t");
		for(String s : index){
			if(!s.startsWith("#") && !s.startsWith("hsl") && !s.startsWith("rgb")){
				System.out.print(i%5!=0?s + ", ":s + ",\n\t");
				i++;
			}
		}
	}
	
	public static enum SWT_COLORS {
		
		BLACK(SWT.COLOR_BLACK),
		BLUE(SWT.COLOR_BLUE),
		CYAN(SWT.COLOR_CYAN),
		GRAY(SWT.COLOR_GRAY),
		GREEN(SWT.COLOR_GREEN),
		WHITE(SWT.COLOR_WHITE),
		MAGENTA(SWT.COLOR_MAGENTA),
		YELLOW(SWT.COLOR_YELLOW),
		RED(SWT.COLOR_RED);
		
		public int value;
		
		private SWT_COLORS(int i){
			value = i;
		}
	}
	
	public float[] toFloatArray(){
		return new float[]{RGBtoFloat(r),RGBtoFloat(g),RGBtoFloat(b),1.0F};
	}
	
	public static float RGBtoFloat(int rgb255){
		return (rgb255 / 255.0F);
	}
	
	public static int floatToRGB(float rgbFloat){
		return (int) (rgbFloat * 255);
	}
	
	/*public static double RGBtoDouble(int rgb255){
		return rgb255 / 255;
	}
	
	public static int floatToRGB(double rgbFloat){
		return (int) (rgbFloat * 255);
	}*/
	
	public EZRGB fadeColorRGB(EZRGB fadeTo, float step){
		float revStep = 1 - step;
		float red = RGBtoFloat(r) * revStep + RGBtoFloat(fadeTo.r) * step;
		float green = RGBtoFloat(g) * revStep + RGBtoFloat(fadeTo.g) * step;
		float blue = RGBtoFloat(b) * revStep + RGBtoFloat(fadeTo.b) * step;
		return new EZRGB(floatToRGB(red),floatToRGB(green),floatToRGB(blue));
	}
	
	public float[] toHSB(){
		return Color.RGBtoHSB(r,g,b,null);
	}
	
	public EZRGB fadeColorHue(EZRGB fadeTo, float step){
		float gHue = toHSB()[0];
	    float bHue = fadeTo.toHSB()[0];
	    int i = (int) (step * 100);
        return fromColor((Color.getHSBColor(gHue + (i * (bHue - gHue) / 100), 1, 1)));
	}
	
	public EZRGB mixRGB(EZRGB color){
		return new EZRGB(r/2 + color.r/2, g/2 + color.g/2, b/2+color.b/2);
	}
	
	public EZRGB mixHSB(EZRGB color){
		float[] c = toHSB();
		float[] v = color.toHSB();
		return fromColor((Color.getHSBColor(c[0]/2 + v[0]/2, c[1]/2 + v[1]/2, c[2]/2 + v[2]/2)));
	}
	
//	public static void printAvailableColors2(){
//		if(colors==null){
//			try { colors = FileManager.getTextfileFromPackage("com.ash.util.files", "colors");}catch(Exception e){colors = colorNotFound;}
//		}
//		String[] index = colors.split("\n");
//		System.out.print("Available colors:\n\t");
//		for(String s : index){
//			if(!s.startsWith("#") && !s.startsWith("hsl")){
//				System.out.print(s + "\\n");
//			}
//		}
//	}
	
	public void display(){
		display(true,true,100,100);
	}
	
	public void display(boolean exit_on_close, boolean labels, int x, int y){
		JFrame f = new JFrame(toString());
		if(exit_on_close) f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(x,y,100,100);
		f.setUndecorated(true);
		f.setBackground(this.toColor());
		f.getContentPane().setBackground(this.toColor());
		f.setLayout(new BorderLayout());
		f.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				f.setVisible(false);
				if(exit_on_close) System.exit(0);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		if(labels){
			Label l = new Label("" + r);
			l.setForeground(negate().toColor());
			f.add(l, BorderLayout.WEST);
			
			Label l2 = new Label("" + g);
			l2.setForeground(negate().toColor());
			f.add(l2, BorderLayout.NORTH);
			
			Label l3 = new Label("" + b);
			l3.setForeground(negate().toColor());
			f.add(l3, BorderLayout.SOUTH);
		}
		f.setVisible(true);
	}
	
	public EZRGB negate(){
		return new EZRGB(255-r,255-g,255-b);
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	public String matchHue() throws Exception{
		
		int red = (int) r/50;
		int green = (int) g/50;
		int blue = (int) b/50;
		
		boolean r_g = r > g;
		boolean r_b = r > b;
		
		boolean g_r = g > r;
		boolean g_b = g > b;
		
		boolean b_r = b > r;
		boolean b_g = b > g;
		
		throw new Exception("matchHue() is deprecated");
	}
	
	public static final String colorNotFound = "red\nrgb(255,0,0)\ndarkred\nrgb(139,0,0)\nlightcoral\nrgb(240,128,128)\nfirebrick\nrgb(178,34,34)\nbrown\nrgb(165,42,42)\nindianred\nrgb(205,92,92)\nrosybrown\nrgb(188,143,143)\nmaroon\nrgb(128,0,0)\nmistyrose\nrgb(255,228,225)\nsalmon\nrgb(250,128,114)\ntomato\nrgb(255,99,71)\ndarksalmon\nrgb(233,150,122)\ncoral\nrgb(255,127,80)\norangered\nrgb(255,69,0)\nlightsalmon\nrgb(255,160,122)\nsienna\nrgb(160,82,45)\nchocolate\nrgb(210,105,30)\nsaddlebrown\nrgb(139,69,19)\nseashell\nrgb(255,245,238)\npeachpuff\nrgb(255,218,185)\nsandybrown\nrgb(244,164,96)\nlinen\nrgb(250,240,230)\nperu\nrgb(205,133,63)\nbisque\nrgb(255,228,196)\ndarkorange\nrgb(255,140,0)\nantiquewhite\nrgb(250,235,215)\ntan\nrgb(210,180,140)\nburlywood\nrgb(222,184,135)\nblanchedalmond\nrgb(255,235,205)\nnavajowhite\nrgb(255,222,173)\npapayawhip\nrgb(255,239,213)\nmoccasin\nrgb(255,228,181)\noldlace\nrgb(253,245,230)\nwheat\nrgb(245,222,179)\norange\nrgb(255,165,0)\nfloralwhite\nrgb(255,250,240)\ngoldenrod\nrgb(218,165,32)\ndarkgoldenrod\nrgb(184,134,11)\ncornsilk\nrgb(255,248,220)\ngold\nrgb(255,215,0)\nkhaki\nrgb(240,230,140)\nlemonchiffon\nrgb(255,250,205)\npalegoldenrod\nrgb(238,232,170)\ndarkkhaki\nrgb(189,183,107)\nbeige\nrgb(245,245,220)\nlightgoldenrodyellow\nrgb(250,250,210)\nivory\nrgb(255,255,240)\nlightyellow\nrgb(255,255,224)\nyellow\nrgb(255,255,0)\nolive\nrgb(128,128,0)\nyellowgreen\nrgb(154,205,50)\nolivedrab\nrgb(107,142,35)\ndarkolivegreen\nrgb(85,107,47)\ngreenyellow\nrgb(173,255,47)\nchartreuse\nrgb(127,255,0)\nlawngreen\nrgb(124,252,0)\nhoneydew\nrgb(240,255,240)\nlightgreen\nrgb(144,238,144)\npalegreen\nrgb(152,251,152)\ndarkseagreen\nrgb(143,188,143)\nlimegreen\nrgb(50,205,50)\nlime\nrgb(0,255,0)\nforestgreen\nrgb(34,139,34)\ngreen\nrgb(0,128,0)\ndarkgreen\nrgb(0,100,0)\nseagreen\nrgb(46,139,87)\nmediumseagreen\nrgb(60,179,113)\nmintcream\nrgb(245,255,250)\nspringgreen\nrgb(0,255,127)\nmediumspringgreen\nrgb(0,250,154)\naquamarine\nrgb(127,255,212)\nmediumaquamarine\nrgb(102,205,170)\nturquoise\nrgb(64,224,208)\nlightseagreen\nrgb(32,178,170)\nmediumturquoise\nrgb(72,209,204)\nazure\nrgb(240,255,255)\nlightcyan\nrgb(224,255,255)\npaleturquoise\nrgb(175,238,238)\ncyan\nrgb(0,255,255)\naqua\nrgb(0,255,255)\ndarkslategray\nrgb(47,79,79)\ndarkcyan\nrgb(0,139,139)\nteal\nrgb(0,128,128)\ndarkturquoise\nrgb(0,206,209)\ncadetblue\nrgb(95,158,160)\npowderblue\nrgb(176,224,230)\ndeepskyblue\nrgb(0,191,255)\nlightblue\nrgb(173,216,230)\nskyblue\nrgb(135,206,235)\nlightskyblue\nrgb(135,206,250)\nsteelblue\nrgb(70,130,180)\naliceblue\nrgb(240,248,255)\ndodgerblue\nrgb(30,144,255)\nlightslategray\nrgb(119,136,153)\nslategray\nrgb(112,128,144)\nlightsteelblue\nrgb(176,196,222)\ncornflowerblue\nrgb(100,149,237)\nroyalblue\nrgb(65,105,225)\nnavy\nrgb(0,0,128)\nmidnightblue\nrgb(25,25,112)\nmediumblue\nrgb(0,0,205)\nlavender\nrgb(230,230,250)\nghostwhite\nrgb(248,248,255)\nblue\nrgb(0,0,139)\ndarkblue\nrgb(0,0,139)\nslateblue\nrgb(106,90,205)\ndarkslateblue\nrgb(72,61,139)\nmediumslateblue\nrgb(123,104,238)\nmediumpurple\nrgb(147,112,219)\nrebeccapurple\nrgb(102,51,153)\nblueviolet\nrgb(138,43,226)\nindigo\nrgb(75,0,130)\ndarkorchid\nrgb(153,50,204)\ndarkviolet\nrgb(148,0,211)\nmediumorchid\nrgb(186,85,211)\nthistle\nrgb(216,191,216)\nplum\nrgb(221,160,221)\nviolet\nrgb(238,130,238)\nmagenta\nrgb(255,0,255)\nfuchsia\nrgb(255,0,255)\ndarkmagenta\nrgb(139,0,139)\npurple\nrgb(128,0,128)\norchid\nrgb(218,112,214)\nmediumvioletred\nrgb(199,21,133)\ndeeppink\nrgb(255,20,147)\nhotpink\nrgb(255,105,180)\npalevioletred\nrgb(219,112,147)\nlavenderblush\nrgb(255,240,245\ncrimson\nrgb(220,20,60)\npink\nrgb(255,192,203)\nlightpink\nrgb(255,182,193)\nsnow\nrgb(255,250,250)\nwhite\nrgb(255,255,255)\nwhitesmoke\nrgb(245,245,245)\ngainsboro\nrgb(220,220,220)\nlightgrey\nrgb(211,211,211)\nsilver\nrgb(192,192,192)\ndarkgray\nrgb(169,169,169)\ngray\nrgb(128,128,128)\ndimgray\nrgb(105,105,105)\nblack\nrgb(0,0,0)\n";
}
