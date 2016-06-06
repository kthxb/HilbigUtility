package com.ash.util.math;

public class IntEdit implements Comparable<IntEdit> {
	
	public static void main(String[] args) {
		IntEdit i = new IntEdit(1203);
		System.out.println(i.sub(0, 4));
		System.out.println(i.intAt(1));
		i.replace(203, 399);
		System.out.println(i);
		i.trim();
		System.out.println(i.hashCode());
	}
	
	private int value;
	
	public void setValue(int value) {
		this.value = value;
	}

	private String string;
	
	public IntEdit(int i){
		value = i;
		string = "" + i;
	}
	
	public void replace(int i, int o){
		string = string.replace("" + i, "" + o);
		applyChanges();
	}
	
	public int sub(int i, int o){
		String temp = string.substring(i,o);
		return Integer.parseInt(temp);
	}
	
	public int intAt(int index){
		return Integer.parseInt("" + string.charAt(index));
	}
	
	public boolean startsWith(int i){
		return string.startsWith("" + i);
	}
	
	public boolean endsWith(int i){
		return string.endsWith("" + i);
	}
	
	public boolean contains(int i){
		return string.contains("" + i);
	}
	
	public int indexOf(int i){
		return string.indexOf("" + i);
	}
	
	public void trim(){
		String[] singles = string.split("");
		int firstIndex = 0;
		boolean done = false;
		for(String s : singles){
			if(!done && s.equals("0")){
				firstIndex++;
			} else {
				done = true;
			}
		}
		string = string.substring(firstIndex);
		applyChanges();
	}
	
	private void applyChanges(){
		value = Integer.parseInt(string);
	}
	
	public int value(){
		return value;
	}

	@Override
	public String toString() {
		return "" + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntEdit other = (IntEdit) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public int compareTo(IntEdit i) {
		return this.value - i.value;
	}

}
