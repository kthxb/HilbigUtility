package com.ash.test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;

import com.ash.util.files.FileManager;
import com.ash.util.files.FileManagerDeprecated;
import com.ash.util.math.EZMath;
import com.ash.util.math.Random;

/**
 * Example for xml marshalling
 * @author Aaron Hilbig
 *
 */
@XmlRootElement(namespace = "Weapon")
@Deprecated
public class Weapon {
	
	public static void main(String[] args) {
		Weapon.loadResource(FileManager.getFileWithDialog("Resource", true, null, null));
		
		JAXB.marshal(Weapon.loadedWeapons.get(0), FileManager.getFileWithDialog("Resource", false, null, null));
		Weapon w = JAXB.unmarshal(FileManager.getFileWithDialog("Resource", true, null, null), Weapon.class);
		
		System.out.println(w.v_name[0]);
	}
	
	public static final int MAX_ATKDMG = 100;
	public static final int MAX_DEF = 100;
	public static final int MAX_THRES_VARIETY = 10;
	public static String resourceName;
	public static ArrayList<Weapon> varWeapons = new ArrayList<Weapon>();
	public static ArrayList<String> possibleOrigins = new ArrayList<String>();
	public static ArrayList<String> possibleCultures = new ArrayList<String>();
	public static ArrayList<String> possibleActiveEffects = new ArrayList<String>();
	public static ArrayList<String> possiblePassiveEffects = new ArrayList<String>();
	private static PrintStream log = System.out;
	
	public static ArrayList<Weapon> loadedWeapons = new ArrayList<Weapon>();
	
	private int attackDamage, attackVar, defensive, defensiveVar;
	private String name, origin, culture, description;
	private String activeEffect, passiveEffect;
	private WeaponType weaponType;
	private WeaponMaterial weaponMaterial;
	private WeaponElement weaponElement;
	private Rarity rarity;
	private boolean allowVar;
	private String[] v_name;
	private String[] v_origin;
	private String[] v_culture;
	private String[] v_activeEffect;
	private String[] v_passiveEffect;
	private int rarityVar;
	public int getRarityVar() {
		return rarityVar;
	}

	public void setRarityVar(int rarityVar) {
		this.rarityVar = rarityVar;
	}

	private Weapon superiorWeapon;
	
	/**
	 * Generate specific weapon
	 * @param attackDamage
	 * @param defensive
	 * @param name
	 * @param origin
	 * @param culture
	 * @param description
	 * @param activeEffect
	 * @param passiveEffect
	 * @param weaponType
	 * @param weaponMaterial
	 * @param weaponElement
	 */
	public Weapon(int attackDamage, int defensive, String name, String origin,
			String culture, String description, String activeEffect, String passiveEffect, WeaponType weaponType,
			WeaponMaterial weaponMaterial, WeaponElement weaponElement, Rarity rarity) {
		this.attackDamage = attackDamage;
		this.defensive = defensive;
		this.name = name;
		this.origin = origin;
		this.culture = culture;
		this.description = description;
		this.activeEffect = activeEffect;
		this.passiveEffect = passiveEffect;
		this.weaponType = weaponType;
		this.weaponMaterial = weaponMaterial;
		this.weaponElement = weaponElement;
		this.rarity = rarity;
		allowVar = false;
	}
	
	/**
	 * Generate Weapon with variety
	 * 
	 * @param attackDamage
	 * @param attackVar
	 * @param defensive
	 * @param defensiveVar
	 * @param name
	 * @param origin
	 * @param culture
	 * @param description
	 * @param activeEffect
	 * @param passiveEffect
	 * @param weaponType
	 * @param weaponMaterial
	 * @param weaponElement
	 */
	public Weapon(int attackDamage, int attackVar, int defensive, int defensiveVar, String[] name, String[] origin,
			String[] culture, String description, String[] activeEffect, String[] passiveEffect, WeaponType weaponType,
			WeaponMaterial weaponMaterial, WeaponElement weaponElement, Rarity rarity, int rarityVar) {
		this.attackDamage = attackDamage;
		this.attackVar = attackVar;
		this.defensive = defensive;
		this.defensiveVar = defensiveVar;
		this.v_name = name;
		this.v_origin = origin;
		this.v_culture = culture;
		this.description = description;
		this.v_activeEffect = activeEffect;
		this.v_passiveEffect = passiveEffect;
		this.weaponType = weaponType;
		this.weaponMaterial = weaponMaterial;
		this.weaponElement = weaponElement;
		this.rarity = rarity;
		this.rarityVar = rarityVar;
		allowVar = true;
		varWeapons.add(this);
//		possibleActiveEffects.addAll(arrayToArrayList(v_activeEffect));
//		possibleCultures.addAll(arrayToArrayList(v_culture));
//		possibleOrigins.addAll(arrayToArrayList(v_origin));
//		possiblePassiveEffects.addAll(arrayToArrayList(v_passiveEffect));
		postConstructor();
	}
	
	public Weapon() {}

	private void postConstructor(){
		for(String s : arrayToArrayList(v_activeEffect))
			if(!possibleActiveEffects.contains(s))
				possibleActiveEffects.add(s);
		for(String s : arrayToArrayList(v_culture))
			if(!possibleCultures.contains(s))
				possibleCultures.add(s);
		for(String s : arrayToArrayList(v_origin))
			if(!possibleOrigins.contains(s))
				possibleOrigins.add(s);
		for(String s : arrayToArrayList(v_passiveEffect))
			if(!possiblePassiveEffects.contains(s))
				possiblePassiveEffects.add(s);
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private static ArrayList<String> testForDubs(ArrayList<String> sar){
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		while(sar.iterator().hasNext()){
			String s = sar.iterator().next();
			if(hm.get(s)==null)
				hm.putIfAbsent(s, 1);
			else if(hm.get(s)>0)
				hm.put(s, hm.get(s)+1);
		}
		for(String s : hm.keySet())
			log.println(s+":"+hm.get(s));
		return sar;
	}
	
	public static enum Rarity {
		Normal, Magic, Rare, Legendary, Divine;
		
		@Override
		public String toString(){
			switch(this){
			case Normal:return "Normal";
			case Magic:return "Magic";
			case Rare:return "Rare";
			case Legendary:return "Legendary";
			case Divine:return "Divine";
			default:return "Normal";
			}
		}
		
		public static Rarity stringToType(String s){
			for(int i=0;i<5;i++)
				if(getByID(i).toString().equals(s))
					return getByID(i);
			return null;
		}
		
		public static Rarity getByID(int i){
			if(i>5) i = 5;
			else if(i<0) i = 0;
			switch(i){
			case 0:return Normal;
			case 1:return Magic;
			case 2:return Rare;
			case 3:return Legendary;
			case 4:return Divine;
			default:return null;
			}
		}

		public static Rarity getRandom() {
			return getByID(Random.getRandom(0, 4));
		}

		public int getID() {
			switch(this){
			case Normal:return 0;
			case Magic:return 1;
			case Rare:return 2;
			case Legendary:return 3;
			case Divine:return 4;
			default:return 0;
			}
		}
	}

	public static enum WeaponType {
		Axe(0), Sword(5), Spear(4), Bow(1), Longsword(3), Dagger(2), Wand(6);
		
		private int id;
		
		WeaponType(int id){
			this.setID(id);
		}
		
		@Override
		public String toString(){
			switch(this){
			case Axe:return "Axe";
			case Bow:return "Bow";
			case Dagger:return "Dagger";
			case Longsword:return "Longsword";
			case Spear:return "Spear";
			case Sword:return "Sword";
			case Wand:return "Wand";
			default:return "None";
			}
		}
		
		public static WeaponType stringToType(String s){
			for(int i=0;i<6;i++)
				if(getByID(i).toString().equals(s))
					return getByID(i);
			return null;
		}
		
		public static WeaponType getByID(int i){
			switch(i){
			case 0:return Axe;
			case 1:return Bow;
			case 2:return Dagger;
			case 3:return Longsword;
			case 4:return Spear;
			case 5:return Sword;
			case 6:return Wand;
			default:return null;
			}
		}

		public static WeaponType getRandom() {
			return getByID(Random.getRandom(0, 6));
		}

		public int getID() {
			return id;
		}

		public void setID(int id) {
			this.id = id;
		}
	}
	
	public static enum WeaponMaterial {
		Wood(5), Stone(4), Iron(2), Bronze(0), Steel(3), Gold(1), Glass(6);
		
		@Override
		public String toString(){
			switch(this){
			case Bronze:return "Bronze";
			case Gold:return "Gold";
			case Iron:return "Iron";
			case Steel:return "Steel";
			case Stone:return "Stone";
			case Wood:return "Wood";
			case Glass:return "Glass";
			default:return "None";
			}
		}
		
		private int ID;
		
		private WeaponMaterial(int ID){
			this.setID(ID);
		}
		
		public static WeaponMaterial stringToType(String s){
			for(int i=0;i<6;i++)
				if(getByID(i).toString().equals(s))
					return getByID(i);
			return null;
		}
		
		public static WeaponMaterial getByID(int i){
			switch(i){
			case 0:return Bronze;
			case 1:return Gold;
			case 2:return Iron;
			case 3:return Steel;
			case 4:return Stone;
			case 5:return Wood;
			case 6:return Glass;
			default:return null;
			}
		}

		public static WeaponMaterial getRandom() {
			return getByID(Random.getRandom(0, 6));
		}

		public int getID() {
			return ID;
		}

		public void setID(int iD) {
			ID = iD;
		}
	}
	
	public static enum WeaponElement {
		Fire(1), Ice(2), Poison(4), Stone(5), Light(3), Dark(0), Earth(6), Water(7), Air(8);
		
		@Override
		public String toString(){
			switch(this){
			case Dark:return "Dark";
			case Fire:return "Fire";
			case Ice:return "Ice";
			case Light:return "Light";
			case Poison:return "Poison";
			case Stone:return "Stone";
			case Earth:return "Earth";
			case Water:return "Water";
			case Air:return "Air";
			default:return "None";
			}
		}
		
		private int ID;
		
		private WeaponElement(int ID){
			this.setID(ID);
		}

		public static WeaponElement stringToType(String s){
			for(int i=0;i<7;i++){log.println(getByID(i).toString() + "!=" + s + "?");
				if(getByID(i).toString().equals(s))
					return getByID(i);}
			return null;
		}
		
		public static WeaponElement getRandom() {
			return getByID(Random.getRandom(0, 8));
		}
		
		public static WeaponElement getByID(int i){
			switch(i){
			case 0:return Dark;
			case 1:return Fire;
			case 2:return Ice;
			case 3:return Light;
			case 4:return Poison;
			case 5:return Stone;
			case 6:return Earth;
			case 7:return Water;
			case 8:return Air;
			default:return null;
			}
		}

		public int getID() {
			return ID;
		}

		public void setID(int iD) {
			ID = iD;
		}
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getAttackVar() {
		return attackVar;
	}

	public void setAttackVar(int attackVar) {
		this.attackVar = attackVar;
	}

	public int getDefensive() {
		return defensive;
	}

	public void setDefensive(int defensive) {
		this.defensive = defensive;
	}

	public int getDefensiveVar() {
		return defensiveVar;
	}

	public void setDefensiveVar(int defensiveVar) {
		this.defensiveVar = defensiveVar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getCulture() {
		return culture;
	}

	public void setCulture(String culture) {
		this.culture = culture;
	}

	public String getDescription() {
		return description;
	}
	
	public void setStringToDescription(String s) {
		setDescription(s.replaceAll(",", ";"));
	}
	
	public String getDescriptionComma() {
		return description.replaceAll(";", ",");
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActiveEffect() {
		return activeEffect;
	}

	public void setActiveEffect(String activeEffect) {
		this.activeEffect = activeEffect;
	}

	public String getPassiveEffect() {
		return passiveEffect;
	}

	public void setPassiveEffect(String passiveEffect) {
		this.passiveEffect = passiveEffect;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}

	public WeaponMaterial getWeaponMaterial() {
		return weaponMaterial;
	}

	public void setWeaponMaterial(WeaponMaterial weaponMaterial) {
		this.weaponMaterial = weaponMaterial;
	}

	public WeaponElement getWeaponElement() {
		return weaponElement;
	}

	public void setWeaponElement(WeaponElement weaponElement) {
		this.weaponElement = weaponElement;
	}

	public boolean isAllowVar() {
		return allowVar;
	}

	public void setAllowVar(boolean allowVar) {
		this.allowVar = allowVar;
	}

	public String[] getV_name() {
		return v_name;
	}

	public void setV_name(String[] v_name) {
		this.v_name = v_name;
	}

	public String[] getV_origin() {
		return v_origin;
	}

	public void setV_origin(String[] v_origin) {
		this.v_origin = v_origin;
	}

	public String[] getV_culture() {
		return v_culture;
	}

	public void setV_culture(String[] v_culture) {
		this.v_culture = v_culture;
	}

	public String[] getV_activeEffect() {
		return v_activeEffect;
	}

	public void setV_activeEffect(String[] v_activeEffect) {
		this.v_activeEffect = v_activeEffect;
	}

	public String[] getV_passiveEffect() {
		return v_passiveEffect;
	}

	public void setV_passiveEffect(String[] v_passiveEffect) {
		this.v_passiveEffect = v_passiveEffect;
	}
	
	public static Weapon generateSpecRandom(String name, String origin, String culture, String description, Rarity rarity){
		return new Weapon(Random.getRandom(0, MAX_ATKDMG), Random.getRandom(-MAX_THRES_VARIETY, MAX_THRES_VARIETY), name, origin, culture, description, "None", "None", WeaponType.getRandom(), WeaponMaterial.getRandom(), WeaponElement.getRandom(), rarity);
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	public Weapon getSuperiorWeapon() {
		return superiorWeapon;
	}

	public void setSuperiorWeapon(Weapon superiorWeapon) {
		this.superiorWeapon = superiorWeapon;
	}

	@Override
	public String toString() {
		if(this.isAllowVar())
			return "Weapon [attackDamage=" + attackDamage + ", attackVar=" + attackVar + ", defensive=" + defensive
					+ ", defensiveVar=" + defensiveVar + ", name=" + name + ", origin=" + origin + ", culture=" + culture
					+ ", description=" + description + ", activeEffect=" + activeEffect + ", passiveEffect=" + passiveEffect
					+ ", weaponType=" + weaponType + ", weaponMaterial=" + weaponMaterial + ", weaponElement="
					+ weaponElement + ", rarity=" + rarity + ", allowVar=" + allowVar + ", v_name="
					+ Arrays.toString(v_name) + ", v_origin=" + Arrays.toString(v_origin) + ", v_culture="
					+ Arrays.toString(v_culture) + ", v_activeEffect=" + Arrays.toString(v_activeEffect)
					+ ", v_passiveEffect=" + Arrays.toString(v_passiveEffect) + ", rarityVar=" + rarityVar + "]";
		else
			return "Weapon \""+ name +"\" [attackDamage=" + attackDamage + ", defensive=" + defensive
					+ ", name=" + name + ", origin=" + origin + ", culture=" + culture
					+ ", description=" + description + ", activeEffect=" + activeEffect + ", passiveEffect=" + passiveEffect
					+ ", weaponType=" + weaponType + ", weaponMaterial=" + weaponMaterial + ", weaponElement="
					+ weaponElement + ", rarity=" + rarity + "]";
	}
	
	public Weapon generateRandomFromThis(){
		if(this.isAllowVar())
			return new Weapon(EZMath.cantBeNegative(attackDamage + Random.getRandom(-attackVar, attackVar)), EZMath.cantBeNegative(defensive + Random.getRandom(-defensiveVar, defensiveVar)), v_name[Random.getRandom(0, v_name.length-1)], v_origin[Random.getRandom(0, v_origin.length-1)], v_culture[Random.getRandom(0, v_culture.length-1)], description, v_activeEffect[Random.getRandom(0, v_activeEffect.length-1)], v_passiveEffect[Random.getRandom(0, v_passiveEffect.length-1)], weaponType, weaponMaterial, weaponElement, Rarity.getByID(rarity.getID() + Random.getRandom(-rarityVar, rarityVar))).c_setSuperiorWeapon(this);
		else
			return this;
	}
	
	private Weapon c_setSuperiorWeapon(Weapon weapon) {
		this.setSuperiorWeapon(weapon);
		return this;
	}

	public int evaluate(){
		int i = getAttackDamage() + getDefensive();
		Weapon c = this.getSuperiorWeapon();
		int supAV = c.getAttackVar();
		int supDV = c.getDefensiveVar();
		int myAV = this.getAttackDamage() - c.getAttackDamage();
		int myDV = this.getDefensive() - c.getDefensive();
		//log.println(supAV + "," + supDV + ", " + myAV + ", " + myDV);
		if(Random.isNear(EZMath.betrag(myAV), supAV, 0.7F)){
			if(myAV>0) i+=myAV*1.5;
			if(myAV<0) i-=myAV*1.5;
		}
		if(Random.isNear(EZMath.betrag(myDV), supDV, 0.7F)){
			if(myDV>0) i+=myDV*1.5;
			if(myDV<0) i-=myDV*1.5;
		}
		return i;
	}
	
	public static Weapon generateRandomDrop(ArrayList<Weapon> list){
		int rand = Random.getRandom(0, list.size()-1);
		return list.get(rand).generateRandomFromThis();
	}
	
	public static Weapon generateRandomDrop(){
		return generateRandomDrop(varWeapons);
	}
	
	public static String[] makeArray(String... s){
		return s;
	}
	
	private static ArrayList<String> arrayToArrayList(String[] s){
		ArrayList<String> a = new ArrayList<String>();
		for(int i=0;i<s.length;i++)
			a.add(s[i]);
		return a;
	}
	
	public static Weapon stringToObject(String s){
		Weapon o = new Weapon();
		String allParams = s.substring(s.indexOf("[")+1, s.indexOf("]")-1);
		String[] params = allParams.split(",");
		try{o.attackDamage = Integer.parseInt(params[0].trim().split("=")[1]);}catch(Exception e){}
		try{o.defensive = Integer.parseInt(params[1].trim().split("=")[1]);}catch(Exception e){}
		try{o.name = params[2].trim().split("=")[1];}catch(Exception e){}
		try{o.origin = params[3].trim().split("=")[1];}catch(Exception e){}
		try{o.culture = params[4].trim().split("=")[1];}catch(Exception e){}
		try{o.description = params[5].trim().split("=")[1];}catch(Exception e){}
		try{o.activeEffect = params[6].trim().split("=")[1];}catch(Exception e){}
		try{o.passiveEffect = params[7].trim().split("=")[1];}catch(Exception e){}
		try{o.weaponType = WeaponType.stringToType(params[8].trim().split("=")[1]);}catch(Exception e){}
		try{o.weaponMaterial = WeaponMaterial.stringToType(params[9].trim().split("=")[1]);}catch(Exception e){}
		try{o.weaponElement = WeaponElement.stringToType(params[10].trim().split("=")[1]);}catch(Exception e){}
		try{o.rarity = Rarity.stringToType(params[11].trim().split("=")[1]);}catch(Exception e){}
		return o;
	}
	
	public static Weapon stringToVarObject(String s){
		//Weapon o = new Weapon();
		String s0 = s.replaceFirst("Weapon \\[", "");
		log.println(s0);
		String[] s1 = s0.split("=");
		ArrayList<String> allAttributes = new ArrayList<String>();
		for(int i=1; i<s1.length; i++){
			if(s1[i].startsWith("[")){
				allAttributes.add(s1[i].substring(0, s1[i].lastIndexOf("]")+1).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", ","));
				log.println(allAttributes.get(allAttributes.size()-1));
				continue;
			}
			allAttributes.add(s1[i].split(",")[0].replaceAll("\\]", ""));
			log.println(allAttributes.get(allAttributes.size()-1));
		}
		Weapon w = new Weapon(Integer.parseInt(allAttributes.get(0)), Integer.parseInt(allAttributes.get(1)), Integer.parseInt(allAttributes.get(2)), Integer.parseInt(allAttributes.get(3)), allAttributes.get(15).split(","), allAttributes.get(16).split(","), allAttributes.get(17).split(","), allAttributes.get(7), allAttributes.get(18).split(","),allAttributes.get(19).split(","), WeaponType.stringToType(allAttributes.get(10)), WeaponMaterial.stringToType(allAttributes.get(11)), WeaponElement.stringToType(allAttributes.get(12)), Rarity.stringToType(allAttributes.get(13)), Integer.parseInt(allAttributes.get(20)));
		w.setName(allAttributes.get(4));
		return w;
	}
	
	public static void loadResource(String s){
		if(s==""){
			resourceName = "No resource opened";
			return;
		}
		loadedWeapons.clear();
		String loaded = FileManagerDeprecated.getFromTextFile(s);
		String[] lines = loaded.split("\n");
		resourceName = s.split("\\\\")[s.split("\\\\").length-1];
		for(int i=0;i<lines.length;i++)
			try{loadedWeapons.add(stringToVarObject(lines[i]));}catch(Exception e){}
	}
	
	public static Weapon getRandomWeaponWithPowerBelow(int i){
		Weapon w = generateRandomDrop(loadedWeapons);
		while(w.evaluate()>i)
			w = generateRandomDrop(loadedWeapons);
		return w;
	}
	
	public static Weapon getRandomWeaponWithPowerAbove(int i){
		Weapon w = generateRandomDrop(loadedWeapons);
		while(w.evaluate()<i)
			w = generateRandomDrop(loadedWeapons);
		return w;
	}
}
