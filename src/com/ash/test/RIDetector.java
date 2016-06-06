package com.ash.test;

import java.awt.Color;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.ash.util.files.EZRGB;

@Deprecated
public class RIDetector {
	
	public static void main(String[] args) {
		
	}
	
	public BufferedImage img;
	
	public ArrayList<Rectangle> out;
	
	public EZRGB[][] cluster;
	
	public ArrayList<EZRGB> clusterList;
	
	public RIDetector(BufferedImage img){
		this.img = img;
		out = new ArrayList<>();
	}
	
	public void detect(){
		cluster = new EZRGB[img.getHeight()][img.getWidth()];
		clusterList = new ArrayList<>();
		int ht = img.getHeight();
		int wh = img.getWidth();
		for(int y=0;y<ht;y++){
			for(int x=0;x<wh;x++){
				Color c = new Color(img.getRGB(x, y));
				EZRGB crt = new EZRGB(c.getRed(), c.getGreen(), c.getBlue());
				clusterList.add(crt);
				if(clusterList.size()>1){
					EZRGB last = clusterList.get(clusterList.size() - 2);
					EZRGB above = null;
					if(clusterList.size()-1 - img.getWidth() >= 0){
						above = clusterList.get(clusterList.size()-1 - img.getWidth());
					}
					if(last.equals(crt)){ //2 px mit gleicher Farbe -> Linie
						
					} else if(above!=null && last.equals(above)){ //2 px mit gleicher Farbe übereinander -> vert. Linie
						
					} else { //Nix. Linie beenden
						
					}
				}
			}
		}
	}
	

}
