package com.ash.bot;

import com.ash.input.InputInterface;

@Deprecated
public class Test {
	
	public static void main(String[] args) throws Exception {
		
		InputInterface in = new InputInterface("Geile function","Gib hier zahlen");
		in.addIntegerInput("Menge");
		in.addIntegerInput("Fehler");
		in.open();
		
		double menge = Double.valueOf(("" + in.result.get("Menge") + ".0"));
		double fehler = Double.valueOf(("" + in.result.get("Fehler") + ".0"));
		double anzahl_wiederholungen = (Math.log(menge)/Math.log(2));
		System.out.println("anzahl_wiederholungen = " + anzahl_wiederholungen);
		double summe = 0;
		int lock = 0;
		boolean b = false;
		for(int i=0; i<anzahl_wiederholungen+1; i++){
			double summeDavor = summe;
			double addition = 0;
			if(Math.pow(2, i) > fehler*2 && !b){
				lock = i;
				b = true;
				System.out.print("Lock: " + lock + "");
				addition = Math.pow(2, i);
			} else if(Math.pow(2, i) > fehler*2 && b){
				addition = Math.pow(2, lock);
			} else
				addition = Math.pow(2, i);
			//(Math.pow(2, i) > fehler*2 ? fehler*lock : Math.pow(2, i));
			
			summe += (Math.pow(2, i) > fehler*2 ? fehler*2 : Math.pow(2, i));
			System.out.println("\t\t" + summeDavor + " + " + addition + "\t= " + summe);
		}
		System.out.println("== " + summe);
	}
	
}
